package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.shopaholic.data.shop.comparator.ComparatorBuilder;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public abstract class ConditionBuilder {
    public final String id;

    public ConditionBuilder(String id) {
        this.id = id;
    }

    public abstract void save(ShopaholicDatabase data);

    public static And and(String id, String... conditions) {
        return new And(id, conditions);
    }

    public static Compare compare(String id, ComparatorBuilder compare1, boolean lessThan, boolean equalTo, boolean greaterThan, ComparatorBuilder compare2) {
        return new Compare(id, compare1, lessThan, equalTo, greaterThan, compare2);
    }

    public static Named named(String id, String name) {
        return new Named(id, name);
    }
}
