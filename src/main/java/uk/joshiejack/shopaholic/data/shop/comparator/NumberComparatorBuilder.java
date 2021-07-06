package uk.joshiejack.shopaholic.data.shop.comparator;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class NumberComparatorBuilder extends ComparatorBuilder {
    private int number;

    protected NumberComparatorBuilder(String id, int number) {
        super(id);
        this.number = number;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("comparator_number", "ID,Number", CSVUtils.join(id, number));
    }
}
