package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.MutableComparator;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractItemComparator implements MutableComparator {
    protected final List<Item> items = new ArrayList<>();
    protected final List<ITag.INamedTag<Item>> tags = new ArrayList<>();

    public abstract AbstractItemComparator create();

    @Override
    public Comparator create(Row data, String id) {
        AbstractItemComparator comparator = create();
        addEntry(comparator, data);
        return comparator;
    }

    @Override
    public void merge(Row data) {
        addEntry(this, data);
    }

    private void addEntry(AbstractItemComparator comparator, Row data) {
        String item = data.get("item");
        if (item.startsWith("tag:"))
            comparator.tags.add(ItemTags.createOptional(new ResourceLocation(item.replace("tag:", ""))));
        else {
            Item theItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
            if (theItem != Items.AIR)
                comparator.items.add(theItem);
        }
    }
}
