package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ShippedComparatorBuilder extends ComparatorBuilder {
    private final List<Item> items = new ArrayList<>();
    private final List<ITag.INamedTag<Item>> tags = new ArrayList<>();

    public ShippedComparatorBuilder(String id) {
        super(id);
    }

    public ShippedComparatorBuilder countItem(Item item) {
        items.add(item);
        return this;
    }

    public ShippedComparatorBuilder countTag(ITag.INamedTag<Item> tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        items.forEach(item -> data.addEntry("comparator_shipped", "ID,Item", CSVUtils.join(id, Objects.requireNonNull(item.getRegistryName()).toString())));
        tags.forEach(tag -> data.addEntry("comparator_shipped", "ID,Item", CSVUtils.join(id, ("tag:" + tag.getName()))));
    }
}