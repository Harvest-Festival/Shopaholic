package uk.joshiejack.shopaholic.plugins.kubejs.util;

import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.player.PlayerJS;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

public class ShippingUtils {
    public static long value(ItemStackJS wrapper) {
        return ShippingRegistry.getValue(wrapper.getItemStack());
    }

    public static void ship(PlayerJS<?> playerWrapper, ItemStackJS stackWrapper, int count) {
        ItemStack stack = stackWrapper.getItemStack();
        Shipping shipping = Market.get((ServerWorld) playerWrapper.minecraftEntity.level).getShippingForPlayer(playerWrapper.minecraftPlayer);
        LazyOptional<IItemHandler> optional = stack.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        if (optional.isPresent()) {
            IItemHandler handler = optional.resolve().get();
            if (handler instanceof IItemHandlerModifiable) {
                IItemHandlerModifiable inventory = ((IItemHandlerModifiable) handler);
                for (int i = 0; i < inventory.getSlots(); i++) {
                    ItemStack inSlot = inventory.getStackInSlot(i);
                    long value = ShippingRegistry.getValue(inSlot);
                    if (value > 0) {
                        shipping.add(inSlot);
                        inventory.setStackInSlot(i, ItemStack.EMPTY);
                    }
                }

                return;
            }
        }

        ItemStack shipped = stack.copy();
        shipped.setCount(count);
        shipping.add(shipped);
        stack.shrink(count); //Shrink the actual stack
    }

    public static int getSoldCount(PlayerJS<?> playerW, ItemStackJS wrapper) {
        ItemStack stack = wrapper.getItemStack();
        PlayerEntity player = playerW.minecraftPlayer;
        return player.level.isClientSide ? Shipped.getCount(stack) :
                Market.get((ServerWorld) player.level).getShippingForPlayer(player).getCount(stack);
    }
}
