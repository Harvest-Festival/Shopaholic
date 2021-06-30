package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;
import java.util.Map;

public class CompareCondition extends Condition {
    private Comparator compare_1;
    private Comparator compare_2;
    private boolean greater;
    private boolean lesser;
    private boolean equal;

    public CompareCondition() {}
    public Condition create(Row data, Map<String, Comparator> comparators) {
        CompareCondition validator = new CompareCondition();
        validator.lesser = data.isTrue("less_than");
        validator.equal = data.isTrue("equal_to");
        validator.greater = data.isTrue("greater_than");
        validator.compare_1 = comparators.get(data.get("comparator_1_id").toString());
        validator.compare_2 = comparators.get(data.get("comparator_2_id").toString());
        return validator;
    }

    @Deprecated
    @Override
    public Condition create(Row database, String id) {
        return this;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (compare_1 == null || compare_2 == null) return false;
        int value1 = compare_1.getValue(target);
        int value2 = compare_2.getValue(target);
        return ((greater && value1 > value2) || (lesser && value1 < value2) || (equal && value1 == value2));
    }
}
