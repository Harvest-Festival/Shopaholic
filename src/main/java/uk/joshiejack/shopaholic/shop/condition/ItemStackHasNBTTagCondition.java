package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class ItemStackHasNBTTagCondition extends AbstractHasNBTTagCondition<ItemStack> {
    @Override
    protected AbstractHasNBTTagCondition<ItemStack> create() {
        return new ItemStackHasNBTTagCondition();
    }

    @Override
    protected Predicate<ItemStack> createPredicate(CompoundNBT compoundnbt) {
        return (stack) -> NBTUtil.compareNbt(compoundnbt, stack.getTag(), true);
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.getStack().hasTag() && predicate.test(target.getStack());
    }
}