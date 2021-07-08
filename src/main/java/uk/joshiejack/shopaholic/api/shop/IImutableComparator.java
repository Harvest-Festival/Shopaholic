package uk.joshiejack.shopaholic.api.shop;

import uk.joshiejack.penguinlib.data.database.Row;

/** Implement this on comparators that are immutable **/
public interface IImutableComparator extends Comparator {
    default Comparator create(Row row, String id) { return this; }
}