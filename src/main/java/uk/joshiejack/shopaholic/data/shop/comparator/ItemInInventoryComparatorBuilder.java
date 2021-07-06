package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.item.Item;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class ItemInInventoryComparatorBuilder extends ComparatorBuilder {
    private Item item;

    protected ItemInInventoryComparatorBuilder(String id, Item item) {
        super(id);
        this.item = item;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("comparator_item", "ID,Item", CSVUtils.join(id, item.getRegistryName().toString()));
    }
}
