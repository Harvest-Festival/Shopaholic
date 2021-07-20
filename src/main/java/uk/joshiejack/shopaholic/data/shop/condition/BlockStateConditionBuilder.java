package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class BlockStateConditionBuilder extends ConditionBuilder {
    private final String tagName;
    private final String tagData;

    @SuppressWarnings("unused")
    public BlockStateConditionBuilder(String id, String tagName, String tagData) {
        super(id);
        this.tagName = tagName;
        this.tagData = tagData;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_block_state", "ID,State Name,State Value", CSVUtils.join(id, tagName, tagData));
    }
}
