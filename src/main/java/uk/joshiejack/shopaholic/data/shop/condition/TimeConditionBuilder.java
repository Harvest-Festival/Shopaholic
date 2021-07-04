package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.util.RangedInteger;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.List;

//Conditions
public class TimeConditionBuilder extends ConditionBuilder {
    protected final List<RangedInteger> list = new ArrayList<>();

    public TimeConditionBuilder(String id) {
        super(id);
    }

    public TimeConditionBuilder withHours(int open, int close) {
        list.add(RangedInteger.of(open, close));
        return this;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        list.forEach(ri -> data.addEntry("condition_time", "ID,Open,Close", CSVUtils.join(id, ri.getMinInclusive(), ri.getMaxInclusive())));
    }
}
