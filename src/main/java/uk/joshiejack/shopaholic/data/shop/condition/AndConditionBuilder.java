package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Conditions
public class AndConditionBuilder extends ConditionBuilder {
    protected final List<ConditionBuilder> conditions = new ArrayList<>();

    @SuppressWarnings("unused")
    public AndConditionBuilder(String id, ConditionBuilder[] conditions) {
        super(id);
        this.conditions.addAll(Arrays.asList(conditions));
    }

    @Override
    public void save(ShopaholicDatabase data) {
        conditions.forEach(condition -> {
            condition.save(data);
            data.addEntry("condition_and", "ID,Condition ID", CSVUtils.join(id, condition.id));
        });
    }
}
