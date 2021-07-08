package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class AddComparator extends AbstractListComparator {
    @Override
    protected String getTableName() {
        return "comparator_add";
    }

    @Override
    protected String getFieldName() {
        return "comparator id";
    }

    @Override
   public Comparator create(Row database, String id) {
        return new AddComparator();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return comparators.stream().mapToInt(c -> c.getValue(target)).sum();
    }
}