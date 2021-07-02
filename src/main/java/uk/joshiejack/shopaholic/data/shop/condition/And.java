package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Conditions
public class And extends ConditionBuilder {
    protected final List<String> conditions = new ArrayList<>();

    public And(String id, String[] conditions) {
        super(id);
        this.conditions.addAll(Arrays.asList(conditions));
    }

    @Override
    public void save(ShopaholicDatabase data) {
        conditions.forEach(condition ->
                data.addEntry("condition_and", "ID,Condition ID", CSVUtils.join(id, condition)));
    }
}
