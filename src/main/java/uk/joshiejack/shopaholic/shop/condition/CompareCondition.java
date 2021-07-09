package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.Map;

public class CompareCondition implements Condition {
    private Comparator compare_1;
    private Comparator compare_2;
    private boolean greater;
    private boolean lesser;
    private boolean equal;

    public CompareCondition() {}

    //Specialised create method, passing all the comparators that registered
    public Condition create(Row data, Map<String, Comparator> comparators) {
        CompareCondition validator = new CompareCondition();
        validator.lesser = data.isTrue("less than");
        validator.equal = data.isTrue("equal to");
        validator.greater = data.isTrue("greater than");
        validator.compare_1 = comparators.get(data.get("comparator 1 id").toString());
        validator.compare_2 = comparators.get(data.get("comparator 2 id").toString());
        return validator;
    }

    @Deprecated
    @Override
    public Condition create(Row database, String id) {
        return this;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (compare_1 == null || compare_2 == null) return false;
        int value1 = compare_1.getValue(target);
        int value2 = compare_2.getValue(target);
        return ((greater && value1 > value2) || (lesser && value1 < value2) || (equal && value1 == value2));
    }
}
