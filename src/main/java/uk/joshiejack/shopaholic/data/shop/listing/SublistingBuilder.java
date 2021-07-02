package uk.joshiejack.shopaholic.data.shop.listing;

import joptsimple.internal.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ITag;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.shop.handler.EntityListingHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class SublistingBuilder {
    public final List<ItemStack> materials = new ArrayList<>();
    public final List<Pair<ITag.INamedTag<Item>, Integer>> tagMaterials = new ArrayList<>();
    public final String data;
    public final String type;
    public String id;
    public int gold = 0;
    public int weight = 1;
    public String name = Strings.EMPTY;
    public String icon = "default";
    public String tooltip = Strings.EMPTY;

    public SublistingBuilder(String type, String data) {
        this.type = type;
        this.data = data;
        this.id = "default";
    }

    public static ItemListing item(Item item) {
        return new ItemListing(item);
    }

    public static PotionListing potion(String id, EffectInstance effect) {
        return new PotionListing(id, effect);
    }

    public static EntityListing entity(String id, EntityListingHandler.EntitySpawnData spawnData) {
        return new EntityListing(id, spawnData);
    }

    public static DepartmentListing department(String departmentID) {
        return new DepartmentListing(departmentID);
    }

    public static BundleListing bundle(String bundleID) {
        return new BundleListing(bundleID);
    }

    public SublistingBuilder id(String id) {
        this.id = id;
        return this;
    }

    public SublistingBuilder cost(int cost) {
        gold = cost;
        return this;
    }

    public SublistingBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public SublistingBuilder material(ItemStack stack) {
        materials.add(stack);
        return this;
    }

    public SublistingBuilder material(ITag.INamedTag<Item> stack, int count) {
        tagMaterials.add(Pair.of(stack, count));
        return this;
    }

    public abstract void save(ShopaholicDatabase data);
}
