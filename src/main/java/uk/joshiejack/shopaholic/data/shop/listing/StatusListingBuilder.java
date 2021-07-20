package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.comparator.ComparatorBuilder;

@SuppressWarnings("unused")
public class StatusListingBuilder extends SublistingBuilder<StatusListingBuilder> {
    private final String field;
    private final ComparatorBuilder comparator;

    public StatusListingBuilder(String type, String id, String field, ComparatorBuilder comparator) {
        super(type, id);
        this.field = field;
        this.comparator = comparator;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry(type + "es", "ID,Field,Comparator ID", CSVUtils.join(this.data, field, comparator.id));
        comparator.save(data);
    }
}

