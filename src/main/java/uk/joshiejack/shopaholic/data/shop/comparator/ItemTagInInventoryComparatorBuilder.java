package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class ItemTagInInventoryComparatorBuilder extends ComparatorBuilder {
    private ITag.INamedTag<Item> tag;

    protected ItemTagInInventoryComparatorBuilder(String id, ITag.INamedTag<Item> tag) {
        super(id);
        this.tag = tag;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("comparator_item_tag", "ID,Tag", CSVUtils.join(id, tag.getName().toString()));
    }
}
