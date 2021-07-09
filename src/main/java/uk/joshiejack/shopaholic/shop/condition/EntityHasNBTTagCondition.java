package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class EntityHasNBTTagCondition extends AbstractHasNBTTagCondition<Entity> {
    protected TargetType predicateTarget;

    @Override
    protected AbstractHasNBTTagCondition<Entity> create() {
        return new EntityHasNBTTagCondition().setTarget(predicateTarget);
    }

    public AbstractHasNBTTagCondition<Entity> setTarget(TargetType type) {
        this.predicateTarget = type;
        return this;
    }

    @Override
    protected Predicate<Entity> createPredicate(CompoundNBT compoundnbt) {
        return (entity) -> {
            CompoundNBT data = entity.saveWithoutId(new CompoundNBT());
            if (entity instanceof ServerPlayerEntity) {
                ItemStack itemstack = ((ServerPlayerEntity) entity).inventory.getSelected();
                if (!itemstack.isEmpty()) {
                    data.put("SelectedItem", itemstack.save(new CompoundNBT()));
                }
            }

            return NBTUtil.compareNbt(compoundnbt, data, true);
        };
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        System.out.println("TESTING THOS?");
        return predicate.test(predicateTarget == TargetType.PLAYER ? target.getPlayer() : target.getEntity());
    }

    public enum TargetType {
        PLAYER, ENTITY
    }
}