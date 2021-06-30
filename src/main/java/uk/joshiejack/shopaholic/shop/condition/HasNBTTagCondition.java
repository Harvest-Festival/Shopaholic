package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class HasNBTTagCondition extends Condition {
    private String key;
    private String value;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type != CheckType.SHOP_LISTING && target.input.hasTag(target, key, value);
    }

    @Override
    public Condition create(Row data, String id) {
        HasNBTTagCondition validator = new HasNBTTagCondition();
        validator.key = data.get("tag_name");
        validator.value = data.get("tag_data");
        return validator;
    }
}
