package uk.joshiejack.shopaholic.data.shop;

import com.google.gson.JsonObject;
import joptsimple.internal.Strings;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.condition.ConditionBuilder;
import uk.joshiejack.shopaholic.data.shop.listing.ListingBuilder;
import uk.joshiejack.shopaholic.data.shop.listing.SublistingBuilder;
import uk.joshiejack.shopaholic.shop.listing.EntityListingHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DepartmentBuilder {
    public final String id;
    public final String name;
    public final String icon;
    public List<ConditionBuilder> conditions = new ArrayList<>();
    public List<ListingBuilder> listings = new ArrayList<>();

    public DepartmentBuilder(String id, String name, Icon icon) {
        this.id = id;
        this.name = name;
        this.icon = icon.toJson(new JsonObject()).toString();
    }

    public static DepartmentBuilder of(String id, Icon icon, String name) {
        return new DepartmentBuilder(id, name, icon);
    }

    public DepartmentBuilder listing(ListingBuilder listing) {
        this.listings.add(listing);
        return this;
    }

    public DepartmentBuilder condition(ConditionBuilder condition) {
        conditions.add(condition);
        return this;
    }

    //Shorthands
    public DepartmentBuilder entityListing(EntityType<?> entity, int gold) {
        listing(ListingBuilder.of(entity.getRegistryName().getPath())
                .addSublisting(SublistingBuilder.entity(entity.getRegistryName().getPath(),
                        new EntityListingHandler.EntitySpawnData(entity)).cost(gold)));
        return this;
    }

    public DepartmentBuilder itemListing(Item item, int gold) {
        listing(ListingBuilder.of(item.getRegistryName().getPath())
                .addSublisting(SublistingBuilder.item(item).cost(gold)));
        return this;
    }

    public DepartmentBuilder sellListing(Item item, int gold) {
        listing(ListingBuilder.of("sell_" + item.getRegistryName().getPath())
                .addSublisting(SublistingBuilder.sell().cost(-gold).material(item, 1)));
        return this;
    }

    public DepartmentBuilder sellListing(ITag.INamedTag<Item> item, int gold) {
        listing(ListingBuilder.of("sell_" + item.getName().getPath())
                .addSublisting(SublistingBuilder.sell().cost(-gold).material(item, 1)));
        return this;
    }


    public void save(ShopaholicDatabase data) {
        listings.forEach(listing -> {
            if (listing.stockMechanicBuilder != null)
                data.addStockMechanic(listing.stockMechanicBuilder.id, listing.stockMechanicBuilder.max, listing.stockMechanicBuilder.replenish);
            data.addEntry("department_listings", "Department ID,ID,Stock Mechanic,Cost Formula", CSVUtils.join(id, listing.id, listing.stockMechanic, listing.costFormula));
            listing.conditions.forEach(condition -> {
                data.addEntry("listing_conditions", "Department ID,Listing ID,Condition ID", CSVUtils.join(id, listing.id, condition.id));
                String previous = data.subfolder;
                data.subfolder = "shops/conditions";
                condition.save(data);
                data.subfolder = previous;
            });

            listing.sublistings.forEach(sublisting -> {
                data.addEntry("sublistings", "Department ID,Listing ID,ID,Type,Data,Gold,Weight", CSVUtils.join(id, listing.id, sublisting.id, sublisting.type, sublisting.data, sublisting.gold, sublisting.weight));
                sublisting.materials.forEach(material -> data.addEntry("sublisting_materials", "Department ID,Listing ID,Sub ID,Item,Amount",
                        CSVUtils.join(id, listing.id, sublisting.id, Objects.requireNonNull(material.getItem().getRegistryName()).toString(), material.getCount())));
                sublisting.tagMaterials.forEach(material -> data.addEntry("sublisting_materials", "Department ID,Listing ID,Sub ID,Item,Amount",
                        CSVUtils.join(id, listing.id, sublisting.id, "#" + material.getKey().getName(), material.getRight())));
                //Only if applicable
                if (!sublisting.icon.equals("default") || !sublisting.name.equals(Strings.EMPTY) || !sublisting.tooltip.equals(Strings.EMPTY)) {
                    data.addEntry("sublisting_display_data", "Department ID,Listing ID,Sub ID,Icon,Name,Tooltip",
                            CSVUtils.join(id, listing.id, sublisting.id, sublisting.icon, sublisting.name, sublisting.tooltip));
                }

                sublisting.save(data);
            });
        });
    }
}
