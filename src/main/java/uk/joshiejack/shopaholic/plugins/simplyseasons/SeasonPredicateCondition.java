package uk.joshiejack.shopaholic.plugins.simplyseasons;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.simplyseasons.loot.SeasonPredicate;

import javax.annotation.Nonnull;

public class SeasonPredicateCondition implements Condition {
    private SeasonPredicate predicate;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return predicate.matches(target.getWorld(), target.getPos());
    }

    @Override
    public Condition create(ShopLoadingData data, Row row, String id) {
        SeasonPredicate predicate = SeasonPredicate.REGISTRY.get(row.get("season predicate").toString());
        SeasonPredicateCondition condition = new SeasonPredicateCondition();
        condition.predicate = predicate;
        return condition;
    }
}
