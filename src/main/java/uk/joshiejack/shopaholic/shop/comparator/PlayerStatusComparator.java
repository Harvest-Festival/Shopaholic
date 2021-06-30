package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class PlayerStatusComparator implements Comparator {
    private String status;

    @Override
    public Comparator create(Row data) {
        PlayerStatusComparator comparator = new PlayerStatusComparator();
        comparator.status = data.get("status");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.getPlayer().getPersistentData().getCompound("PenguinStatuses").getInt(status);
    }
}