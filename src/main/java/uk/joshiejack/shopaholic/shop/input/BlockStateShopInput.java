package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class BlockStateShopInput extends ShopInput<BlockState> {
    public BlockStateShopInput(BlockState id) {
        super(id);
    }

    public BlockStateShopInput(PacketBuffer buf) {
        super(fromString(buf.readUtf()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        BlockState state = target.getWorld().getBlockState(target.getPos());
        Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
        return property != null && state.getValue(property).toString().equals(value);
    }

    public static BlockState fromString(String data) {
        return Blocks.DIRT.defaultBlockState(); //TODO:
    }
}
