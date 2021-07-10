package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.TameableEntity;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public class HasPetCondition implements Condition {
    private Predicate<TameableEntity> predicate;

    public HasPetCondition() {}
    public HasPetCondition(EntityType<?> type) {
        this.predicate = (e) -> e.getType() == type;
    }

    @Override
    public Condition create(ShopLoadingData loadingData, Row data, String id) {
        return new HasPetCondition(data.entity());
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.getWorld().getLoadedEntitiesOfClass(TameableEntity.class, target.getPlayer().getBoundingBox().inflate(128D), predicate).stream()
                .anyMatch(e -> target.getPlayer().getUUID().equals(e.getOwnerUUID()));
    }
}