package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class OrCondition extends AbstractListCondition {
    @Override
    protected String getTableName() {
        return "condition_or";
    }

    @Override
    protected String getFieldName() {
        return "or_condition";
    }

    @Deprecated
    @Override
    public Condition create(Row database, String id) {
        return new OrCondition();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return conditions.stream().anyMatch(condition -> condition.valid(target, type));
    }
}
