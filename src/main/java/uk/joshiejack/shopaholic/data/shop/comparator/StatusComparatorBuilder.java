package uk.joshiejack.shopaholic.data.shop.comparator;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

@SuppressWarnings("unused")
public class StatusComparatorBuilder extends ComparatorBuilder {
    private final String type;
    private final String status;

    protected StatusComparatorBuilder(String type, String id, String status) {
        super(id);
        this.type = type;
        this.status = status;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry(String.format("comparator_%s_status", type), "ID,Status", CSVUtils.join(id, status));
    }
}
