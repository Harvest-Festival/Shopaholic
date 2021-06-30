package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.events.DatabasePopulateEvent;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.IImmutable;
import uk.joshiejack.shopaholic.api.shops.ListingHandler;
import uk.joshiejack.shopaholic.shop.builder.ListingBuilder;
import uk.joshiejack.shopaholic.shop.condition.AbstractListCondition;
import uk.joshiejack.shopaholic.shop.condition.CompareCondition;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.input.InputToShop;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShopLoader {
    @SubscribeEvent
    public static void onDatabaseCollected(DatabasePopulateEvent event) {
        //Update the database with the simple departments
        //Very simple listings AND Generic one for any types
        ListingHandler.HANDLERS.forEach((type, handler) -> {
            event.table("very_simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("type"), type, sdl.get(type), "unlimited"));
            event.table("simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("id"), type, sdl.get(type), sdl.get("stock_mechanic")));
        });

        event.table("very_simple_department_listings").rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("data"), sdl.get("type"), sdl.get("data"), "unlimited"));
        event.table("simple_department_listings").rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("id"), sdl.get("type"), sdl.get("data"), sdl.get("stock_mechanic")));
    }

    private static void registerSimpleListing(DatabasePopulateEvent database, Row sdl, String id, String type, String data, String stock_mechanic) {
        String department_id = sdl.get("department_id");
        long gold = sdl.getAsLong("gold");
        database.createTable("department_listings", "department_id", "id", "stock_mechanic", "cost_formula").insert(department_id, id, stock_mechanic, "default");
        database.createTable("sublistings", "department_id", "listing_id", "id", "type", "data", "gold", "weight").insert(department_id, id, "default", type, data, gold, 1);
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) { //LOW, So we appear after recipes have been added
        //Temporary Registry, Create all the comparators
        Map<String, Comparator> comparators = Maps.newHashMap(); //Temporary Registry
        //for all the type of comparators
        ShopRegistries.COMPARATORS.forEach((type, comparator) -> {
            if (comparator instanceof IImmutable)
                comparators.put(type, comparator);
            else {
                event.table("comparator_" + type).rows().forEach(row -> {
                    String comparator_id = row.id();
                    if (comparators.containsKey(comparator_id)) {
                        comparators.get(comparator_id).merge(row);
                    } else {
                        Comparator theComparator = ShopRegistries.COMPARATORS.get(type);
                        if (theComparator != null) {
                            comparators.put(comparator_id, theComparator.create(row));
                        }
                    }
                });
            }
        });

        //Create all the conditions
        Map<String, Condition> conditions = Maps.newHashMap(); //Temporary Registry
        for (String type : Condition.types()) {
            event.table("condition_" + type).rows().forEach(condition -> {
                String condition_id = condition.id();
                if (conditions.containsKey(condition_id)) {
                    conditions.get(condition_id).merge(condition);
                } else {
                    Condition theCondition = Condition.get(type);
                    if (theCondition != null) {
                        if (theCondition instanceof CompareCondition)
                            conditions.put(condition_id, ((CompareCondition) theCondition).create(condition, comparators));
                        else conditions.put(condition_id, theCondition.create(condition, condition_id));
                    }
                }
            });
        }

        conditions.entrySet().stream().filter(e -> e.getValue() instanceof AbstractListCondition).forEach(entry ->
                ((AbstractListCondition) entry.getValue()).initList(event, entry.getKey(), conditions));

        //Create all the stock mechanics
        Map<String, StockMechanic> stock_mechanics = Maps.newHashMap();
        //WHERE DO WE NPE???
        event.table("stock_mechanics");
        event.table("stock_mechanics").rows();
        event.table("stock_mechanics").rows().forEach(r -> {
        });
        event.table("stock_mechanics").rows().forEach(r -> {
            stock_mechanics.put("OOOO", new StockMechanic(1, 2));
        });


        event.table("stock_mechanics").rows().forEach(stock_mechanic ->
                stock_mechanics.put(stock_mechanic.id(), new StockMechanic(stock_mechanic.get("max stock"), stock_mechanic.get("replenish rate"))));

        event.table("stock_mechanics").rows().forEach(stock_mechanic ->
                stock_mechanics.put(stock_mechanic.id(), new StockMechanic(stock_mechanic.get("max stock"), stock_mechanic.get("replenish rate"))));
        //ap<String, CostScript> cost_scripts = Maps.newHashMap();
        //event.table("cost_formulae").rows().forEach(cost_formula ->
        //cost_scripts.put(cost_formula.id(), Scripting.get(cost_formula.getScript())));

        event.table("shops").rows().forEach(shop -> {
            String shop_id = shop.id();
            String name = shop.name();
            String vendor_id = shop.get("vendor_id");
            InputMethod opening_method = InputMethod.valueOf(shop.get("opening_method").toString().toUpperCase(Locale.ENGLISH));
            Shop theShop = new Shop(shop_id, name);
            event.table("departments").where("shop_id=" + shop_id).forEach(department -> {
                String department_id = department.id();
                Department theDepartment = new Department(theShop, department_id, opening_method); //Add the department to the shop in creation
                if (!department.isEmpty("icon")) theDepartment.setIcon(department.icon());
                if (!department.isEmpty("name")) theDepartment.setName(department.name());
                Row vendor = event.table("vendors").fetch_where("id=" + vendor_id); //Register the vendor
                InputToShop.register(vendor.get("type"), vendor.get("data"), theDepartment); //to the input

                //Add the conditions for this shop
                event.table("shop_conditions").where("shop_id=" + shop_id)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition_id").toString())));
                event.table("department_conditions").where("shop_id=" + shop_id + "&department_id=" + department_id)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition_id").toString())));

                event.table("department_listings").where("department_id=" + department_id).forEach(listing -> {
                    String listing_id = listing.id();
                    String data_id = listing_id.contains("$") ? listing_id.split("\\$")[0] : listing_id;
                    List<Sublisting> sublistings = Lists.newArrayList();//handler.getObjectsFromDatabase(database.get(), department_id, data_id);
                    event.table("sublistings").where("department_id=" + department_id + "&listing_id=" + data_id).forEach(sublisting -> {
                        String original_sub_id = sublisting.id();
                        String original_sub_type = sublisting.get("type");
                        boolean builder = original_sub_type.endsWith("_builder");
                        String sub_type = original_sub_type.replace("_builder", ""); //Remove the builder
                        List<String> data_entries = builder ? ListingBuilder.BUILDERS.get(sublisting.get("data").toString()).items() : Lists.newArrayList(sublisting.get("data").toString());
                        for (int i = 0; i < data_entries.size(); i++) {
                            String sub_id = builder ? original_sub_id + "$" + i : original_sub_id;
                            ListingHandler<?> handler = ListingHandler.HANDLERS.get(sub_type);
                            if (handler == null)
                                Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the listing handler type: " + sub_type + " for " + department_id + ": " + listing_id + " :" + sub_id);
                            else {
                                Sublisting<?> theSublisting = new Sublisting(sub_id, handler, handler.getObjectFromDatabase(event, data_entries.get(i)));
                                theSublisting.setGold(sublisting.getAsLong("gold"));
                                theSublisting.setWeight(sublisting.getAsDouble("weight"));
                                String material_id = sub_id.contains("$") ? sub_id.split("\\$")[0] : sub_id;
                                event.table("sublisting_materials").where("department_id=" + department_id + "&listing_id=" + data_id + "&sub_id=" + material_id).forEach(material -> {
                                    theSublisting.addMaterial(new MaterialCost(material.get("item"), material.get("amount")));
                                });

                                //Load in display overrides
                                Row display = event.table("sublisting_display_data").fetch_where("department_id=" + department_id + "&listing_id=" + data_id + "&sub_id=" + material_id);
                                if (!display.isEmpty("icon")) {
                                    NonNullList<ItemStack> icons = Arrays.stream(display.get("icon").toString().split(","))
                                            .map(s -> new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(s))))
                                            .collect(Collectors.toCollection(NonNullList::create));
                                    theSublisting.setDisplayIcon(icons);
                                }

                                if (!display.isEmpty("name")) theSublisting.setDisplayName(display.get("name"));
                                if (!display.isEmpty("tooltip"))
                                    theSublisting.setTooltip(display.get("tooltip").toString().split("\n"));
                                sublistings.add(theSublisting);
                            }
                        }
                    });

                    if (sublistings.size() > 0 && sublistings.stream().allMatch(sublisting -> sublisting.getHandler().isValid(sublisting.getObject()))) {
                        StockMechanic stockMechanic = stock_mechanics.get(listing.get("stock_mechanic").toString());
                        CostFormula costScript = CostFormula.COST_FORMULAE.get(listing.get("cost_formula").toString());
                        if (stockMechanic == null)
                            Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the stock mechanic: " + listing.get("stock_mechanic") + " for " + department_id + ":" + listing_id);
                        else if (costScript == null)
                            Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the cost script: " + listing.get("cost_formula") + " for " + department_id + ":" + listing_id);
                        else {
                            Listing theListing = new Listing(theDepartment, listing_id, sublistings, costScript, stockMechanic);
                            event.table("listing_conditions").where("department_id=" + department_id + "&listing_id=" + data_id)
                                    .forEach(condition -> {
                                        Condition cd = conditions.get(condition.get("condition_id").toString());
                                        if (cd == null)
                                            Shopaholic.LOGGER.error("Incorrect condition added as a listing condition with the id: "
                                                    + condition.get("condition_id") + " with the listing_id " + listing_id + " in the department " + theDepartment.id());
                                        else
                                            theListing.addCondition(conditions.get(condition.get("condition_id").toString()));
                                    });
                            sublistings.forEach(s -> s.setListing(theListing));
                            Shopaholic.LOGGER.log(Level.INFO, "Successfully added the listing: " + listing_id + " for " + department_id);
                        }
                    } else if (sublistings.size() == 0) {
                        Shopaholic.LOGGER.log(Level.ERROR, "No sublistings were added for the listing: " + department_id + ":" + listing_id);
                    } else {
                        for (Sublisting sublisting : sublistings) {
                            if (!sublisting.getHandler().isValid(sublisting.getObject())) {
                                Shopaholic.LOGGER.log(Level.ERROR, "The sublisting: " + sublisting.id() + " created an invalid object for the listing: " + department_id + ":" + listing_id);
                            }
                        }
                    }
                });
            });
        });
    }
}
