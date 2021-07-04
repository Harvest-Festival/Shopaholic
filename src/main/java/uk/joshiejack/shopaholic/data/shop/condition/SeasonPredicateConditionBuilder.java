package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class SeasonPredicateConditionBuilder extends ConditionBuilder {
    private final String predicate;

    public SeasonPredicateConditionBuilder(String id, String predicate) {
        super(id);
        this.predicate = predicate;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_season", "ID,Season Predicate", CSVUtils.join(id, predicate));
    }
}