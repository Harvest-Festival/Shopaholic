package uk.joshiejack.shopaholic.shipping;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.mutable.MutableBoolean;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.shopaholic.Shopaholic;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShippingMethods {
    public static final ITag.INamedTag<Block> SHIPPING_BIN_BLOCK = BlockTags.createOptional(new ResourceLocation(Shopaholic.MODID, "shipping_bin"));
    public static final ITag.INamedTag<Item> SHIPPING_BIN_ITEM = ItemTags.createOptional(new ResourceLocation(Shopaholic.MODID, "shipping_bin"));
    public static final ITag.INamedTag<EntityType<?>> SHIPPING_BIN_ENTITY = EntityTypeTags.createOptional(new ResourceLocation(Shopaholic.MODID, "shipping_bin"));

    private static boolean ship(PlayerEntity player, ItemStack stack) {
        World world = player.level;
        long value = ShippingRegistry.getValue(stack);
        if (value > 0) {
            if (!world.isClientSide) {
                int count = player.isShiftKeyDown() ? stack.getCount() : 1;
                ItemStack inserted = stack.copy();
                inserted.setCount(count);
                Market.get((ServerWorld) world).getShippingForPlayer(player).add(inserted);
                stack.shrink(count);
            }

            return true;
        }

        return false;
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().getBlockState(event.getPos()).is(SHIPPING_BIN_BLOCK)
                && ship(event.getPlayer(), event.getItemStack()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getEntity().getType().is(SHIPPING_BIN_ENTITY)
                && ship(event.getPlayer(), event.getItemStack()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getItemStack().getItem().is(SHIPPING_BIN_ITEM)) {
            MutableBoolean cancel = new MutableBoolean(false);
            PlayerHelper.getInventoryStream(event.getPlayer())
                    .filter(item -> !item.isEmpty() && item != event.getItemStack())
                    .forEach(item -> {
                        if (ship(event.getPlayer(), item))
                            cancel.setTrue();
                    });
            if (cancel.isTrue())
                event.setCanceled(true);
        }
    }
}
