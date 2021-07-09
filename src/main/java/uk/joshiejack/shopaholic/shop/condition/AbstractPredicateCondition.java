package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;

import java.util.function.Predicate;

public abstract class AbstractPredicateCondition<T> implements Condition {
    protected Predicate<T> predicate;

    @Override
    public Condition create(Row data, String id) {
        return create(data.get("data").toString());
    }

    protected abstract AbstractPredicateCondition<T> create(String data);
}

