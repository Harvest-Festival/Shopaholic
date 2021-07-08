package uk.joshiejack.shopaholic.shipping;

import net.minecraft.block.Block;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.shopaholic.Shopaholic;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShippingBlocks {
    public static final ITag.INamedTag<Block> SHIPPING_BIN = BlockTags.createOptional(new ResourceLocation(Shopaholic.MODID, "shipping_bin"));

    @SubscribeEvent
    public static void onRightClick(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        ItemStack stack = event.getItemStack();
        PlayerEntity player = event.getPlayer();
        Block block = world.getBlockState(event.getPos()).getBlock();
        if (block.is(SHIPPING_BIN)) {
            long value = ShippingRegistry.getValue(stack);
            if (value > 0) {
                if (!world.isClientSide) {
                    int count = player.isShiftKeyDown() ? stack.getCount() : 1;
                    ItemStack inserted = stack.copy();
                    inserted.setCount(count);
                    Market.get((ServerWorld) world).getShippingForPlayer(player).add(inserted);
                    stack.shrink(count);
                }
            }
        }
    }
}
