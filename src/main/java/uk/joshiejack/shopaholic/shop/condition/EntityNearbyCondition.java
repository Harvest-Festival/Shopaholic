package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class EntityNearbyCondition implements Condition {
    private EntityPredicate predicate;
    private double range;

    public EntityNearbyCondition() {
    }

    public EntityNearbyCondition(EntityType<?> type, double range) {
        this.range = range;
        this.predicate = new EntityPredicate().range(range).selector((e) -> e.getType() == type);
    }

    @Override
    public Condition create(Row data, String id) {
        return new EntityNearbyCondition(data.entity(), data.getAsDouble("range"));
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.getWorld().getNearbyEntities(LivingEntity.class, predicate, target.getPlayer(), target.getPlayer().getBoundingBox().inflate(range)).size() > 0;
    }
}