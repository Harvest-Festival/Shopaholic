package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class KubeJSConditionBuilder extends ConditionBuilder {
    public KubeJSConditionBuilder(String id) {
        super(id);
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_kubejs", "ID", CSVUtils.join(id));
    }
}
