package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//TODO ALL THE COMPARATORS
public abstract class ComparatorBuilder {
    public final String id;

    @SuppressWarnings("unused")
    protected ComparatorBuilder(String id) {
        this.id = id;
    }

    @SuppressWarnings("unused")
    public static AddComparatorBuilder add(String id, ComparatorBuilder... comparators) {
        return new AddComparatorBuilder(id, comparators);
    }

    @SuppressWarnings("unused")
    public static BlockTagOnTargetComparatorBuilder blockTag(String id, ITag.INamedTag<Block> tag) {
        return new BlockTagOnTargetComparatorBuilder(id, tag);
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder canSeeSky() {
        return new Immutable("can_see_sky");
    }


    @SuppressWarnings("unused")
    public static AbstractItemComparatorBuilder held(String id) {
        return new AbstractItemComparatorBuilder(id, "comparator_held");
    }

    @SuppressWarnings("unused")
    public static AbstractItemComparatorBuilder itemInInventory(String id, Item item) {
        return new AbstractItemComparatorBuilder(id, "comparator_item_in_inventory");
    }

    @SuppressWarnings("unused")
    public static KubeJSComparatorBuilder kubejs(String id) {
        return new KubeJSComparatorBuilder(id);
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder lightLevel() {
        return new Immutable("light_level");
    }

    @SuppressWarnings("unused")
    public static NumberComparatorBuilder number(String id, int value) {
        return new NumberComparatorBuilder(id, value);
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder playerHealth() { return new Immutable("player_health"); }

    @SuppressWarnings("unused")
    public static StatusComparatorBuilder playerStatus(String id, String status) {
        return new StatusComparatorBuilder("player", id, status);
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder playerX() {
        return new Immutable("player_x");
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder playerY() {
        return new Immutable("player_y");
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder playerZ() {
        return new Immutable("player_z");
    }


    @SuppressWarnings("unused")
    public static ComparatorBuilder rainLevel() {
        return new Immutable("rain_level");
    }


    @SuppressWarnings("unused")
    public static ComparatorBuilder redstoneSignal() {
        return new Immutable("redstone_signal");
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder temperature() { return new Immutable("temperature"); }

    @SuppressWarnings("unused")
    public static AbstractItemComparatorBuilder shippedAmount(String id) {
        return new AbstractItemComparatorBuilder(id, "comparator_shipped");
    }

    @SuppressWarnings("unused")
    public static StatusComparatorBuilder teamStatus(String id, String status) {
        return new StatusComparatorBuilder("team", id, status);
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder vendorHealth() { return new Immutable("vendor_health"); }

    @SuppressWarnings("unused")
    public static ComparatorBuilder vendorX() {
        return new Immutable("vendor_x");
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder vendorY() {
        return new Immutable("vendor_y");
    }

    @SuppressWarnings("unused")
    public static ComparatorBuilder vendorZ() {
        return new Immutable("vendor_z");
    }

    public abstract void save(ShopaholicDatabase data);

    public static class Immutable extends ComparatorBuilder {
        @SuppressWarnings("unused")
        protected Immutable(String id) {
            super(id);
        }

        @Override
        public void save(ShopaholicDatabase data) {}
    }
}
