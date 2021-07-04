package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class HasNBTTagConditionBuilder extends ConditionBuilder {
    private final String tagName;
    private final String tagData;

    public HasNBTTagConditionBuilder(String id, String tagName, String tagData) {
        super(id);
        this.tagName = tagName;
        this.tagData = tagData;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_has_nbt_tag", "ID,Tag Name,Tag Data", CSVUtils.join(id, tagName, tagData));
    }
}
