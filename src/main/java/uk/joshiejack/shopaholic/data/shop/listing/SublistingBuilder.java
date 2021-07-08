package uk.joshiejack.shopaholic.data.shop.listing;

import com.google.gson.JsonObject;
import joptsimple.internal.Strings;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.tags.ITag;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.shop.listing.EntityListingHandler;

import java.util.ArrayList;
import java.util.List;

public abstract class SublistingBuilder<T extends SublistingBuilder<T>> {
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

    public static ItemListingBuilder item(Item item) {
        return new ItemListingBuilder(item);
    }

    public static PotionListingBuilder potion(String id, EffectInstance effect) {
        return new PotionListingBuilder(id, effect);
    }

    public static EntityListingBuilder entity(String id, EntityListingHandler.EntitySpawnData spawnData) {
        return new EntityListingBuilder(id, spawnData);
    }

    public static DepartmentListingBuilder department(String departmentID) {
        return new DepartmentListingBuilder(departmentID);
    }

    public static BundleListingBuilder bundle(String bundleID) {
        return new BundleListingBuilder(bundleID);
    }

    public static HealListingBuilder heal(float healAmount) {
        return new HealListingBuilder(healAmount);
    }

    public static CommandListingBuilder command(String command) {
        return new CommandListingBuilder(command);
    }

    public static KubeJSScriptListingBuilder kubejs(String script) {
        return new KubeJSScriptListingBuilder(script);
    }

    public T id(String id) {
        this.id = id;
        return (T) this;
    }

    public T cost(int cost) {
        gold = cost;
        return (T) this;
    }

    public SublistingBuilder<T> weight(int weight) {
        this.weight = weight;
        return this;
    }

    public SublistingBuilder<T> material(Item item, int count) {
        materials.add(new ItemStack(item, count));
        return this;
    }

    public SublistingBuilder<T> material(ITag.INamedTag<Item> stack, int count) {
        tagMaterials.add(Pair.of(stack, count));
        return this;
    }

    public SublistingBuilder<T> name(String name) {
        this.name = name;
        return this;
    }

    public SublistingBuilder<T> icon(Icon icon) {
        this.icon = icon.toJson(new JsonObject()).toString();
        return this;
    }

    public SublistingBuilder tooltip(String tooltip) {
        this.tooltip = tooltip;
        return this;
    }

    public abstract void save(ShopaholicDatabase data);
}
