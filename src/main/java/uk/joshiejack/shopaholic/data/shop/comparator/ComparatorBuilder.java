package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.condition.ShippedConditionBuilder;

//TODO ALL THE COMPARATORS
public abstract class ComparatorBuilder {
    public final String id;

    protected ComparatorBuilder(String id) {
        this.id = id;
    }

    public static AddComparatorBuilder add(String id, ComparatorBuilder... comparators) {
        return new AddComparatorBuilder(id, comparators);
    }

    public static BlockTagOnTargetComparatorBuilder blockTag(String id, ITag.INamedTag<Block> tag) {
        return new BlockTagOnTargetComparatorBuilder(id, tag);
    }

    public static ItemInInventoryComparatorBuilder itemInInventory(String id, Item item) {
        return new ItemInInventoryComparatorBuilder(id, item);
    }

    public static ItemTagInInventoryComparatorBuilder itemTag(String id, ITag.INamedTag<Item> tag) {
        return new ItemTagInInventoryComparatorBuilder(id, tag);
    }

    public static ComparatorBuilder lightLevel() {
        return new Immutable("light_level");
    }

    public static NumberComparatorBuilder number(String id, int value) {
        return new NumberComparatorBuilder(id, value);
    }

    public static ComparatorBuilder redstoneSignal() {
        return new Immutable("redstone_signal");
    }

    public static ShippedComparatorBuilder shippedAmount(String id) {
        return new ShippedComparatorBuilder(id);
    }

    public abstract void save(ShopaholicDatabase data);

    public static class Immutable extends ComparatorBuilder {
        protected Immutable(String id) {
            super(id);
        }

        @Override
        public void save(ShopaholicDatabase data) {}
    }
}
