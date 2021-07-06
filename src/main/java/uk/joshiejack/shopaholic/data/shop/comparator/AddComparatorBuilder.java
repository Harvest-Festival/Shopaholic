package uk.joshiejack.shopaholic.data.shop.comparator;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Conditions
public class AddComparatorBuilder extends ComparatorBuilder {
    protected final List<ComparatorBuilder> comparators = new ArrayList<>();

    public AddComparatorBuilder(String id, ComparatorBuilder... comparators) {
        super(id);
        this.comparators.addAll(Arrays.asList(comparators));
    }

    @Override
    public void save(ShopaholicDatabase data) {
        comparators.forEach(comparator -> {
            comparator.save(data);
            data.addEntry("comparator_add", "ID,Comparator ID", CSVUtils.join(id, comparator.id));
        });
    }
}
