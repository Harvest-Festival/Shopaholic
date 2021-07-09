package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.MutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.function.BiFunction;

public class StatusComparator implements MutableComparator {
    private final BiFunction<ShopTarget, String, Integer> comparator;
    private String status;

    public StatusComparator(BiFunction<ShopTarget, String, Integer> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Comparator create(Row data, String id) {
        StatusComparator comparator = new StatusComparator(this.comparator);
        comparator.status = data.get("status");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return comparator.apply(target, status);
    }
}