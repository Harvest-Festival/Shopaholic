package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class NamedConditionBuilder extends ConditionBuilder {
    protected final String name;

    public NamedConditionBuilder(String id, String name) {
        super(id);
        this.name = name;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_named", "ID,Name", CSVUtils.join(id, name));
    }
}
