package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class NotCondition extends AbstractListCondition {
    @Override
    protected String getTableName() {
        return "condition_not";
    }

    @Override
    protected String getFieldName() {
        return "condition id";
    }

    @Deprecated
    @Override
    public Condition create(Row database, String id) {
        return new NotCondition();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return conditions.stream().noneMatch(condition -> condition.valid(target, type));
    }
}