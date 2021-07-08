package uk.joshiejack.shopaholic.data.shop.comparator;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class KubeJSComparatorBuilder extends ComparatorBuilder {
    protected KubeJSComparatorBuilder(String id) {
        super(id);
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("comparator_kubejs", "ID", CSVUtils.join(id));
    }
}
