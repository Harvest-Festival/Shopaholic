package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.block.Block;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
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
        TileEntity tile = target.getWorld().getBlockEntity(target.getPos());
        if (tile == null) return false;
        CompoundNBT serialized = tile.serializeNBT();
        return serialized.contains(key) && serialized.getString(key).equals(value);
    }
}