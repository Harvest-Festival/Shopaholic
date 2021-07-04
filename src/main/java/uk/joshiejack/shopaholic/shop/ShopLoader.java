package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.JsonParser;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.events.DatabasePopulateEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.IImutableComparator;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.shop.builder.ListingBuilder;
import uk.joshiejack.shopaholic.shop.condition.AbstractListCondition;
import uk.joshiejack.shopaholic.shop.condition.CompareCondition;
import uk.joshiejack.shopaholic.api.shop.CostFormula;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.input.InputToShop;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.List;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("rawtypes")
@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShopLoader {
    @SubscribeEvent
    public static void onDatabaseCollected(DatabasePopulateEvent event) {
        //Update the database with the simple departments
        //Very simple listings AND Generic one for any types
        ShopRegistries.LISTING_HANDLERS.forEach((type, handler) -> {
            event.table("very_simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("type"), type, sdl.get(type), "unlimited"));
            event.table("simple_department_listings_" + type).rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("id"), type, sdl.get(type), sdl.get("stock mechanic")));
        });

        event.table("very_simple_department_listings").rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("data"), sdl.get("type"), sdl.get("data"), "unlimited"));
        event.table("simple_department_listings").rows().forEach(sdl -> registerSimpleListing(event, sdl, sdl.get("id"), sdl.get("type"), sdl.get("data"), sdl.get("stock mechanic")));
    }

    private static void registerSimpleListing(DatabasePopulateEvent database, Row sdl, String id, String type, String data, String stock_mechanic) {
        String departmentID = sdl.get("department id");
        long gold = sdl.getAsLong("gold");
        database.createTable("department_listings", "Department ID", "ID", "Stock Mechanic", "Cost Formula").insert(departmentID, id, stock_mechanic, "default");
        database.createTable("sublistings", "Department ID", "Listing ID", "ID", "Type", "Data", "Gold", "Weight").insert(departmentID, id, "default", type, data, gold, 1);
    }

    @SuppressWarnings("unchecked")
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) { //LOW, So we appear after recipes have been added
        //Clear out all existing shop related data
        Shop.clear(); //Clear out the department -> shop mappings
        Department.REGISTRY.clear(); //Clear out all the registered departments
        InputToShop.BLOCK_TO_SHOP.clear(); //Clear the block - > shop mappings
        InputToShop.ENTITY_TO_SHOP.clear(); //Clear the entity - > shop mappings
        InputToShop.ITEM_TO_SHOP.clear(); //Clear the item - > shop mappings

        //Temporary Registry, Create all the comparators
        Map<String, Comparator> comparators = Maps.newHashMap(); //Temporary Registry
        //for all the type of comparators
        ShopRegistries.COMPARATORS.forEach((type, comparator) -> {
            if (comparator instanceof IImutableComparator)
                comparators.put(type, comparator);
            else {
                event.table("comparator_" + type).rows().forEach(row -> {
                    String comparatorID = row.id();
                    if (comparators.containsKey(comparatorID)) {
                        comparators.get(comparatorID).merge(row);
                    } else {
                        Comparator theComparator = ShopRegistries.COMPARATORS.get(type);
                        if (theComparator != null) {
                            comparators.put(comparatorID, theComparator.create(row));
                        }
                    }
                });
            }
        });

        //Create all the conditions
        Map<String, Condition> conditions = Maps.newHashMap(); //Temporary Registry
        for (String type : ShopRegistries.CONDITIONS.keySet()) {
            event.table("condition_" + type).rows().forEach(condition -> {
                String conditionID = condition.id();
                if (conditions.containsKey(conditionID)) {
                    conditions.get(conditionID).merge(condition);
                } else {
                    Condition theCondition = ShopRegistries.CONDITIONS.get(type);
                    if (theCondition != null) {
                        if (theCondition instanceof CompareCondition)
                            conditions.put(conditionID, ((CompareCondition) theCondition).create(condition, comparators));
                        else conditions.put(conditionID, theCondition.create(condition, conditionID));
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

        JsonParser parser = new JsonParser();
        event.table("shops").rows().forEach(shop -> {
            String shopID = shop.id();
            String name = shop.name();
            String vendorID = shop.get("vendor id");
            InputMethod opening_method = InputMethod.valueOf(shop.get("opening method").toString().toUpperCase(Locale.ENGLISH));
            Shop theShop = new Shop(shopID, name);
            event.table("departments").where("shop id=" + shopID).forEach(department -> {
                String departmentID = department.id();
                Department theDepartment = new Department(theShop, departmentID, opening_method); //Add the department to the shop in creation
                if (!department.isEmpty("icon")) {
                    String icon = department.get("icon").toString();
                    if (icon.contains("\"")) {
                        theDepartment.setIcon(Icon.fromJson(parser.parse(icon).getAsJsonObject()));
                    } else theDepartment.setIcon(new ItemIcon(department.icon()));
                }
                if (!department.isEmpty("name")) theDepartment.setName(department.name());
                Row vendor = event.table("vendors").fetch_where("id=" + vendorID); //Register the vendor
                InputToShop.register(vendor.get("type"), vendor.getRL("data"), theDepartment); //to the input

                //Add the conditions for this shop
                event.table("shop_conditions").where("shop id=" + shopID)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition id").toString())));
                event.table("department_conditions").where("shop id=" + shopID + "&department id=" + departmentID)
                        .forEach(condition -> theDepartment.addCondition(conditions.get(condition.get("condition id").toString())));

                event.table("department_listings").where("department id=" + departmentID).forEach(listing -> {
                    String listingID = listing.id();
                    String dataID = listingID.contains("$") ? listingID.split("\\$")[0] : listingID;
                    List<Sublisting<?>> sublistings = Lists.newArrayList();//handler.getObjectsFromDatabase(database.get(), department_id, data_id);
                    event.table("sublistings").where("department id=" + departmentID + "&listing id=" + dataID).forEach(sublisting -> {
                        String originalSubID = sublisting.id();
                        String originalSubType = sublisting.get("type");
                        boolean builder = originalSubType.endsWith("_builder");
                        String sub_type = originalSubType.replace("_builder", ""); //Remove the builder
                        List<String> data_entries = builder ? ListingBuilder.BUILDERS.get(sublisting.get("data").toString()).items() : Lists.newArrayList(sublisting.get("data").toString());
                        for (int i = 0; i < data_entries.size(); i++) {
                            String subID = builder ? originalSubID + "$" + i : originalSubID;
                            ListingHandler<?> handler = ShopRegistries.LISTING_HANDLERS.get(sub_type);
                            if (handler == null)
                                Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the listing handler type: " + sub_type + " for " + departmentID + ": " + listingID + " :" + subID);
                            else {
                                Sublisting<?> theSublisting = new Sublisting(subID, handler, handler.getObjectFromDatabase(event, data_entries.get(i)));
                                theSublisting.setGold(sublisting.getAsLong("gold"));
                                theSublisting.setWeight(sublisting.getAsDouble("weight"));
                                String materialID = subID.contains("$") ? subID.split("\\$")[0] : subID;
                                event.table("sublisting_materials").where("department id=" + departmentID + "&listing id=" + dataID + "&sub id=" + materialID).forEach(material -> {
                                    theSublisting.addMaterial(new MaterialCost(material.get("item"), material.get("amount")));
                                });

                                //Load in display overrides
                                Row display = event.table("sublisting_display_data").fetch_where("department id=" + departmentID + "&listing id=" + dataID + "&sub id=" + materialID);
                                if (!display.isEmpty("icon")) {
                                    String icon = display.get("icon").toString();
                                    if (icon.contains("\"")) {
                                        theSublisting.setIcon(Icon.fromJson(parser.parse(icon).getAsJsonObject()));
                                    } else theSublisting.setIcon(new ItemIcon(display.icon()));
                                }

                                if (!display.isEmpty("name")) theSublisting.setDisplayName(display.get("name"));
                                if (!display.isEmpty("tooltip"))
                                    theSublisting.setTooltip(StringEscapeUtils.unescapeJava(display.get("tooltip").toString()).split("\n"));
                                sublistings.add(theSublisting);
                            }
                        }
                    });

                    if (sublistings.size() > 0 && sublistings.stream().allMatch(sublisting -> sublisting.getHandler().isValid(sublisting.getObject()))) {
                        StockMechanic stockMechanic = stock_mechanics.get(listing.get("stock mechanic").toString());
                        CostFormula costScript = ShopRegistries.COST_FORMULAE.get(listing.get("cost formula").toString());
                        if (stockMechanic == null)
                            Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the stock mechanic: " + listing.get("stock mechanic") + " for " + departmentID + ":" + listingID);
                        else if (costScript == null)
                            Shopaholic.LOGGER.log(Level.ERROR, "Failed to find the cost script: " + listing.get("cost formula") + " for " + departmentID + ":" + listingID);
                        else {
                            Listing theListing = new Listing(theDepartment, listingID, sublistings, costScript, stockMechanic);
                            event.table("listing_conditions").where("department id=" + departmentID + "&listing id=" + dataID)
                                    .forEach(condition -> {
                                        Condition cd = conditions.get(condition.get("condition id").toString());
                                        if (cd == null)
                                            Shopaholic.LOGGER.error("Incorrect condition added as a listing condition with the id: "
                                                    + condition.get("condition id") + " with the listing id " + listingID + " in the department " + theDepartment.id());
                                        else
                                            theListing.addCondition(conditions.get(condition.get("condition id").toString()));
                                    });
                            sublistings.forEach(s -> s.setListing(theListing));
                            Shopaholic.LOGGER.log(Level.INFO, "Successfully added the listing: " + listingID + " for " + departmentID);
                        }
                    } else if (sublistings.size() == 0) {
                        Shopaholic.LOGGER.log(Level.ERROR, "No sublistings were added for the listing: " + departmentID + ":" + listingID);
                    } else {
                        for (Sublisting sublisting : sublistings) {
                            if (!sublisting.getHandler().isValid(sublisting.getObject())) {
                                Shopaholic.LOGGER.log(Level.ERROR, "The sublisting: " + sublisting.id() + " created an invalid object for the listing: " + departmentID + ":" + listingID);
                            }
                        }
                    }
                });
            });
        });
    }
}
