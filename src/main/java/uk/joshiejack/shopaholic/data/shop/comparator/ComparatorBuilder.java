package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

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

    public static ComparatorBuilder canSeeSky() {
        return new Immutable("can_see_sky");
    }


    public static AbstractItemComparatorBuilder held(String id) {
        return new AbstractItemComparatorBuilder(id, "comparator_held");
    }

    public static AbstractItemComparatorBuilder itemInInventory(String id, Item item) {
        return new AbstractItemComparatorBuilder(id, "comparator_item_in_inventory");
    }

    public static KubeJSComparatorBuilder kubejs(String id) {
        return new KubeJSComparatorBuilder(id);
    }

    public static ComparatorBuilder lightLevel() {
        return new Immutable("light_level");
    }

    public static NumberComparatorBuilder number(String id, int value) {
        return new NumberComparatorBuilder(id, value);
    }

    public static ComparatorBuilder playerHealth() { return new Immutable("player_health"); }

    public static ComparatorBuilder rainLevel() {
        return new Immutable("rain_level");
    }


    public static ComparatorBuilder redstoneSignal() {
        return new Immutable("redstone_signal");
    }

    public static ComparatorBuilder temperature() { return new Immutable("temperature"); }

    public static AbstractItemComparatorBuilder shippedAmount(String id) {
        return new AbstractItemComparatorBuilder(id, "comparator_shipped");
    }

    public static ComparatorBuilder vendorHealth() { return new Immutable("vendor_health"); }


    public abstract void save(ShopaholicDatabase data);

    public static class Immutable extends ComparatorBuilder {
        protected Immutable(String id) {
            super(id);
        }

        @Override
        public void save(ShopaholicDatabase data) {}
    }
}
