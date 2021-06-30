package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.network.PacketBuffer;
import net.minecraft.state.Property;
import uk.joshiejack.shopaholic.api.shops.ShopInput;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

public class ShopInputBlockState extends ShopInput<BlockState> {
    public ShopInputBlockState(BlockState id) {
        super(id);
    }

    public ShopInputBlockState(PacketBuffer buf) {
        super(fromString(buf.readUtf()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        BlockState state = target.world.getBlockState(target.pos);
        Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
        return property != null && state.getValue(property).toString().equals(value);
    }

    public static BlockState fromString(String data) {
        return Blocks.DIRT.defaultBlockState(); //TODO:
    }
}
