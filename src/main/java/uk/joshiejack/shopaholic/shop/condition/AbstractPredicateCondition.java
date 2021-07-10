package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;

import java.util.function.Predicate;

public abstract class AbstractPredicateCondition<T> implements Condition {
    protected Predicate<T> predicate;

    @Override
    public Condition create(ShopLoadingData data, Row row, String id) {
        return create(row.get("data").toString());
    }

    protected abstract AbstractPredicateCondition<T> create(String data);
}

