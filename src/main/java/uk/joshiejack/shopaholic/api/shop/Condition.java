package uk.joshiejack.shopaholic.api.shop;

import net.minecraft.entity.player.PlayerEntity;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

import javax.annotation.Nonnull;

public interface Condition {
    /**
     * Tests if the condition is met for a listing
     *
     * @param target        the shop target instance
     * @param department    the department that is open
     * @param listing       the listing that is being tested
     * @param type          the checking type, the result may vary
     * @return              if the condition is met
     */
    default boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        return valid(target, type);
    }

    /**
     * Tests if the condition is met for a department
     *
     * @param target        the shop target instance
     * @param type          the checking type, the result may vary
     * @return              if the condition is met
     */
    default boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return true;
    }

    /**
     * Called when a listing with this condition is purchased
     * some conditions need to manipulate player data so this is where you do that
     * @param player            the player who purchased
     * @param department        the department
     * @param listing           the listing they purchased
     */
    default void onPurchase(PlayerEntity player, @Nonnull Department department, @Nonnull Listing listing) {}

    /**
     * Used to create new instance of this comparator using data from the Penguin-Lib database system
     * @param row   the row data for this comparator
     * @param id    the id of this condition
     * @return  an instance of this comparator with the data applied
     *          some comparators are immutable so will just return themselves
     */
    Condition create(Row row, String id);

    /** Some conditions may want to let users add to them in separate rows
     *  And so a condition can appear twice, this is used to merge a new
     *  piece of data in to this existing condition. Not used often but handy!
     * @param row   the row data
     */
    default void merge(Row row) {}


    enum CheckType {
        SHOP_EXISTS, SHOP_IS_OPEN, SHOP_LISTING
    }
}
