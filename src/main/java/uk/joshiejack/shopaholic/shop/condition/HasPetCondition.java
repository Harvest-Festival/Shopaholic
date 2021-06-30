package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class HasPetCondition extends Condition {
    private Predicate<TameableEntity> predicate;

    public HasPetCondition() {}
    public HasPetCondition(EntityType<?> type) {
        this.predicate = (e) -> e.getType() == type;
    }

    @Override
    public Condition create(Row data, String id) {
        return new HasPetCondition(data.entity());
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        return target.getWorld().getLoadedEntitiesOfClass(TameableEntity.class, target.getPlayer().getBoundingBox().inflate(128D), predicate).stream()
                .anyMatch(e -> target.getPlayer().getUUID().equals(e.getOwnerUUID()));
    }
}