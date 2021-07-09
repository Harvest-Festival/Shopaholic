package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.block.Block;
import net.minecraft.inventory.container.INamedContainerProvider;
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
    public String getName(ShopTarget target) {
        TileEntity tile = target.getWorld().getBlockEntity(target.getPos());
        return !(tile instanceof INamedContainerProvider) ? super.getName(target) : ((INamedContainerProvider)tile).getDisplayName().getString();
    }
}