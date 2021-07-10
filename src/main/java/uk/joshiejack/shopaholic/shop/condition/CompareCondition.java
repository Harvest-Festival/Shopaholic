package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class CompareCondition implements Condition {
    private Comparator compare_1;
    private Comparator compare_2;
    private boolean greater;
    private boolean lesser;
    private boolean equal;

    public CompareCondition() {}

    @Deprecated
    @Override
    public Condition create(ShopLoadingData loadingData, Row data, String id) {
        CompareCondition validator = new CompareCondition();
        validator.lesser = data.isTrue("less than");
        validator.equal = data.isTrue("equal to");
        validator.greater = data.isTrue("greater than");
        validator.compare_1 = loadingData.comparators.get(data.get("comparator 1 id").toString());
        validator.compare_2 = loadingData.comparators.get(data.get("comparator 2 id").toString());
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (compare_1 == null || compare_2 == null) return false;
        int value1 = compare_1.getValue(target);
        int value2 = compare_2.getValue(target);
        return ((greater && value1 > value2) || (lesser && value1 < value2) || (equal && value1 == value2));
    }
}
