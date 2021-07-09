package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class HasNBTTagConditionBuilder extends ConditionBuilder {
    private final String type;
    private final String tagData;

    public HasNBTTagConditionBuilder(String type, String id, String tagData) {
        super(id);
        this.type = type;
        this.tagData = tagData;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry(String.format("condition_%s_has_nbt_tag", type), "ID,Data", CSVUtils.join(id, tagData));
    }
}
