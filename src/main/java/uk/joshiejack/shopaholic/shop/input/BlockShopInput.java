package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class BlockShopInput extends ShopInput<Block> {
    public BlockShopInput(Block block) {
        super(block);
    }

    public BlockShopInput(PacketBuffer buf) {
        super(buf.readRegistryIdSafe(Block.class));
    }

    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        BlockState state = target.getWorld().getBlockState(target.getPos());
        Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
        return property != null && state.getValue(property).toString().equals(value);
    }
}