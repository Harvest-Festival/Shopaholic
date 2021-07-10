package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class AndCondition extends AbstractListCondition {
    @Override
    protected String getTableName() {
        return "condition_and";
    }

    @Override
    protected String getFieldName() {
        return "condition id";
    }

    @Override
    public Condition create(ShopLoadingData loadingData, Row database, String id) {
        return new AndCondition();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return conditions.stream().allMatch(condition -> condition.valid(target, type));
    }
}
