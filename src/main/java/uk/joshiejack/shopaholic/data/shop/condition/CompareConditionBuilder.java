package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.comparator.ComparatorBuilder;

public class CompareConditionBuilder extends ConditionBuilder {
    protected ComparatorBuilder compare1;
    protected ComparatorBuilder compare2;
    protected boolean lessThan;
    protected boolean equalTo;
    protected boolean greaterThan;

    public CompareConditionBuilder(String id, ComparatorBuilder compare1, boolean lessThan, boolean equalTo, boolean greaterThan, ComparatorBuilder compare2) {
        super(id);
        this.compare1 = compare1;
        this.compare2 = compare2;
        this.lessThan = lessThan;
        this.equalTo = equalTo;
        this.greaterThan = greaterThan;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_compare", "ID,Comparator 1 ID,Less Than,Equal To,Greater Than,Comparator 2 ID",
                CSVUtils.join(id, compare1.id, lessThan, equalTo, greaterThan, compare2.id));
        compare1.save(data);
        compare2.save(data);
    }
}
