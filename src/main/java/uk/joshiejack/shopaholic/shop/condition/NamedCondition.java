package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class NamedCondition extends Condition {
    private String name;

    @Override
    public Condition create(Row data, String id) {
        NamedCondition validator = new NamedCondition();
        validator.name = data.name();
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.getInput().getName(target).equals(name);
    }
}
