package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;

public abstract class AbstractImmutableComparator extends Comparator {
    @Override
    public Comparator create(Row row, String id) {
        return this;
    }
}
