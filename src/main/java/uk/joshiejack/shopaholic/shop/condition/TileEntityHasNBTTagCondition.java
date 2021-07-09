package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class TileEntityHasNBTTagCondition extends AbstractHasNBTTagCondition<TileEntity> {
    @Override
    protected AbstractHasNBTTagCondition<TileEntity> create() {
        return new TileEntityHasNBTTagCondition();
    }

    @Override
    protected Predicate<TileEntity> createPredicate(CompoundNBT compoundnbt) {
        return (tile) -> NBTUtil.compareNbt(compoundnbt, tile.serializeNBT(), true);
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        TileEntity tile = target.getWorld().getBlockEntity(target.getPos());
        return tile != null && predicate.test(tile);
    }
}