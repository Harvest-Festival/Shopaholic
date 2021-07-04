package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class HasNBTTagCondition implements Condition {
    private String key;
    private String value;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type != CheckType.SHOP_LISTING && target.getInput().hasTag(target, key, value);
    }

    @Override
    public Condition create(Row data, String id) {
        HasNBTTagCondition validator = new HasNBTTagCondition();
        validator.key = data.get("tag name");
        validator.value = data.get("tag data");
        return validator;
    }
}
