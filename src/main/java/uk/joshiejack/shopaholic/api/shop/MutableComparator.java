package uk.joshiejack.shopaholic.api.shop;

import uk.joshiejack.penguinlib.data.database.Row;

public interface MutableComparator extends Comparator {
    /**
     * Used to create new instance of this comparator using data from the Penguin-Lib database system
     * @param row   the row data for this comparator
     * @return  an instance of this comparator with the data applied
     *          some comparators are immutable so will just return themselves
     */
    Comparator create(Row row, String id);

    /** Some comparators may want to let users add to them in separate rows
     *  And so a comparator can appear twice, this is used to merge a new
     *  piece of data in to this existing comparator. Not used often but handy!
     * @param row   the row data
     */
    default void merge(Row row) {};
}
