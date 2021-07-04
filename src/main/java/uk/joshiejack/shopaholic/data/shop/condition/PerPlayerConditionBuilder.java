package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class PerPlayerConditionBuilder extends ConditionBuilder {
    protected final int max;

    public PerPlayerConditionBuilder(String id, int max) {
        super(id);
        this.max = max;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_per_player", "ID,max", CSVUtils.join(id, max));
    }
}
