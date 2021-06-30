package uk.joshiejack.shopaholic.api.shops;

import uk.joshiejack.penguinlib.data.database.Row;

import javax.annotation.Nonnull;

public interface Comparator {
    /**
     * Used to create new instance of this comparator using data from the Penguin-Lib database system
     * @param row   the row data for this comparator
     * @return  an instance of this comparator with the data applied
     *          some comparators are immutable so will just return themselves
     */
    default Comparator create(Row row) { return this; }

    /**
     * Return the value of this comparator
     * Often things can be just true (1) or (0) false
     * @param target    the target object
     * @return  the value this is considered to have
     */
    int getValue(@Nonnull ShopTarget target);

    /** Some comparators may want to let users add to them in separate rows
     *  And so a comparator can appear twice, this is used to merge a new
     *  piece of data in to this existing comparator. Not used often but handy!
     * @param row   the row data
     */
    default void merge(Row row) {};
}
