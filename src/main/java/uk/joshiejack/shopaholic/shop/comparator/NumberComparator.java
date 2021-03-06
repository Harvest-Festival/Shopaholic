package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.MutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class NumberComparator implements MutableComparator {
    private int value;

    @Override
    public Comparator create(Row data, String id) {
        NumberComparator comparator = new NumberComparator();
        comparator.value = data.get("number");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return this.value;
    }
}