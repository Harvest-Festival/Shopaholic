package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class NamedConditionBuilder extends ConditionBuilder {
    protected final String name;
    protected final String type;

    public NamedConditionBuilder(String type, String id, String name) {
        super(id);
        this.type = type;
        this.name = name;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_%s", "ID,Name", CSVUtils.join(id, name));
    }
}
