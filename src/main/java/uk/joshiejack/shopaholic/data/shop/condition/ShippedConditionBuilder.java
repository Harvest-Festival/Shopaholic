package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

//Conditions
public class ShippedConditionBuilder extends ConditionBuilder {
    private final List<Item> items = new ArrayList<>();
    private final List<ITag.INamedTag<Item>> tags = new ArrayList<>();

    public ShippedConditionBuilder(String id, int requiredAmount) {
        super(id + "_" + requiredAmount);
    }

    public ShippedConditionBuilder requireItem(Item item) {
        items.add(item);
        return this;
    }

    public ShippedConditionBuilder requireTag(ITag.INamedTag<Item> tag) {
        tags.add(tag);
        return this;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        items.forEach(item -> data.addEntry("condition_shipped", "ID,Item", CSVUtils.join(id, Objects.requireNonNull(item.getRegistryName()).toString())));
        tags.forEach(tag -> data.addEntry("condition_shipped", "ID,Item", CSVUtils.join(id, ("#" + tag.getName()))));
    }
}
