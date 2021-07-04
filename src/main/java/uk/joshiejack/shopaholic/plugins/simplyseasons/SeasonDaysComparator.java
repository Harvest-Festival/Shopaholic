package uk.joshiejack.shopaholic.plugins.simplyseasons;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.simplyseasons.world.CalendarDate;

import javax.annotation.Nonnull;

public class SeasonDaysComparator implements Comparator {
    private int days;

    @Override
    public Comparator create(Row row) {
        SeasonDaysComparator comparator = new SeasonDaysComparator();
        comparator.days = row.getAsInt("days");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return (int) (days * CalendarDate.seasonLength(target.getWorld()));
    }
}
