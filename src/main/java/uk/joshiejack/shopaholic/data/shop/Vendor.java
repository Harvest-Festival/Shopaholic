package uk.joshiejack.shopaholic.data.shop;

import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;

public class Vendor {
    protected static final Vendor NONE = new Vendor("none", "default", "none");
    protected final String id;
    protected final String type;
    protected final String data;

    public Vendor(String id, String type, String data) {
        this.id = id;
        this.type = type;
        this.data = data;
    }

    public static Vendor entity(String id, EntityType<?> vendor) {
        return new Vendor(id, "entity", vendor.getRegistryName().toString());
    }

    public static Vendor item(String id, Item item) {
        return new Vendor(id, "item", item.getRegistryName().toString());
    }
}
