package uk.joshiejack.shopaholic.data;

import joptsimple.internal.Strings;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;
import uk.joshiejack.shopaholic.Shopaholic;

public class ShopaholicDatabase extends AbstractDatabaseProvider {

    public ShopaholicDatabase(String modid, DataGenerator gen) { super(gen, modid); }
    public ShopaholicDatabase(DataGenerator gen) {
        super(gen, Shopaholic.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
//        Random random = new Random();
//        //Experimental
//        for (Item item : ForgeRegistries.ITEMS.getValues()) {
//            if (item == Items.AIR) continue;
//            addSellValueForItem(item, random.nextInt(3) < 2 ? random.nextInt(1000) : 1 + random.nextInt(100000));
//        }

        //Stock mechanics
        addStockMechanic("unlimited", 1000000000, 1000000000);
        addStockMechanic("limited_1", 1, 1);
        addStockMechanic("only_1", 1, 0);
////
//        ShopBuilder.of("friendly_chest", "Daryl the Chest")
//                .vendor(Vendor.block("daryl", Blocks.CHEST))
//                .condition(ConditionBuilder.hasNBTTag("is_daryl", "CustomName", "{\"text\":\"daryl\"}"))
//                .condition(ConditionBuilder.kubejs("test_script"))
//                .department(DepartmentBuilder.of("nbt_test", new EntityIcon(EntityType.HORSE, 4), "Horsey Land")
//                        .itemListing(HusbandryItems.TRUFFLE.get(), 9999)
//                        .entityListing(EntityType.HORSE, 0)
//                        .listing(ListingBuilder.of("heal").addSublisting(SublistingBuilder.heal(10).material(Items.NETHER_STAR, 99)))
//                )
//                .save(this);
//
//        ShopBuilder.of("friendly_chest", "Daryl the Chest")
//                .vendor(Vendor.block("daryl", Blocks.CHEST))
//                .condition(ConditionBuilder.tileEntityHasNBTTag("is_daryl", "CustomName:'{\"text\":\"daryl\"}'"))
//                .department(DepartmentBuilder.of("nbt_test", new EntityIcon(EntityType.HORSE, 4), "Horsey Land")
//                        .itemListing(HusbandryItems.TRUFFLE.get(), 9999)
//                        .entityListing(EntityType.HORSE, 0)
//                        .listing(ListingBuilder.of("heal").addSublisting(SublistingBuilder.heal(10).material(Items.NETHER_STAR, 99)))
//                )
//                .save(this);
//
//        DepartmentBuilder overworldStore = DepartmentBuilder.of("overworld_store", new ItemIcon(Items.SPRUCE_SAPLING), "Overworld Store");
//        for (Item item: ForgeRegistries.ITEMS) {
//            if (random.nextInt(10) == 0)
//                overworldStore.itemListing(item, 100 + random.nextInt(901));
//        }
//
//        ShopBuilder.of("diamond_worship", "The Great Diamond Heist")
//                .vendor(Vendor.command("diamond_store", "diamond"))
//                .openWith(InputMethod.COMMAND)
//                .department(DepartmentBuilder.of("nether_store", new ItemIcon(Items.QUARTZ), "Nether Store")
//                        .condition(ConditionBuilder.inDimension("in_nether", World.NETHER))
//                        .listing(ListingBuilder.of("gold")
//                                .addSublisting(SublistingBuilder.item(Items.GOLD_INGOT).cost(5000))
//                                .condition(ConditionBuilder.or("near_gold_lovers",
//                                        ConditionBuilder.entityNearby("near_piglin", EntityType.PIGLIN, 32D),
//                                        ConditionBuilder.entityNearby("near_pigman", EntityType.ZOMBIFIED_PIGLIN, 32D))))
//                        .listing(ListingBuilder.of("magma_cream").stockMechanic(StockMechanicBuilder.of("5_daily").max(5).replenishRate(5))
//                                .addSublisting(SublistingBuilder.potion("fire_resist", new EffectInstance(Effects.FIRE_RESISTANCE, 1000))))
//                        .listing(ListingBuilder.of("nether_ore")
//                                .addSublisting(SublistingBuilder.item(Items.NETHER_GOLD_ORE).cost(450))
//                                .addSublisting(SublistingBuilder.item(Items.NETHER_QUARTZ_ORE).cost(220)))
//                )
//                .department(overworldStore
//                        .condition(ConditionBuilder.inDimension("in_overworld", World.OVERWORLD))
//                        .listing(ListingBuilder.of("saplings")
//                                .addSublisting(SublistingBuilder.item(Items.SPRUCE_SAPLING)
//                                        .material(Items.SPRUCE_LEAVES, 5))
//                                .addSublisting(SublistingBuilder.item(Items.JUNGLE_SAPLING)
//                                        .material(Items.JUNGLE_LEAVES, 5))
//                                .addSublisting(SublistingBuilder.item(Items.BIRCH_SAPLING)
//                                        .material(Items.BIRCH_LEAVES, 5))
//                                .addSublisting(SublistingBuilder.item(Items.OAK_SAPLING)
//                                        .material(Items.OAK_LEAVES, 5)))
//                        .listing(ListingBuilder.of("multi_type")
//                                .addSublisting(SublistingBuilder.potion("potions", new EffectInstance(Effects.INVISIBILITY, 5000))
//                                        .cost(4200))
//                                .addSublisting(SublistingBuilder.bundle("cow_equipment")
//                                        .cost(500)
//                                        .addToBundle(SublistingBuilder.item(Items.SHEARS))
//                                        .addToBundle(SublistingBuilder.item(Items.SADDLE))
//                                        .addToBundle(SublistingBuilder.item(Items.MILK_BUCKET)))
//                                .addSublisting(SublistingBuilder.entity("cow", new EntityListingHandler.EntitySpawnData(EntityType.COW)).cost(9000))))
//                .save(this);
//
//        ShopBuilder.of("test_shop", "Piggy Bank")
//                .vendor(Vendor.entity("pig", EntityType.PIG))
//                .condition(ConditionBuilder.named("pig_name", "George"))
//                .openWith(InputMethod.RIGHT_CLICK)
//                .department(DepartmentBuilder.of("test_department", new ItemIcon(Items.GOLD_INGOT), "Gold Store")
//                        .listing(ListingBuilder.of("apple").addSublisting(SublistingBuilder.item(Items.APPLE).cost(200).icon(new ItemIcon(Items.ACACIA_PLANKS))))
//                        .listing(ListingBuilder.of("bucket").addSublisting(SublistingBuilder.item(Items.BUCKET).cost(100)))
//                        .listing(ListingBuilder.of("poison").addSublisting(SublistingBuilder.potion("poison_effect", new EffectInstance(Effects.POISON, 1000))))
//                        .listing(ListingBuilder.of("food").addSublisting(SublistingBuilder.bundle("food_bundle")
//                                .addToBundle(SublistingBuilder.item(Items.CARROT))
//                                .addToBundle(SublistingBuilder.item(Items.POTATO))
//                                .addToBundle(SublistingBuilder.item(Items.BEETROOT))
//                                .cost(9000)
//                                .name("Food Bundle")
//                                .tooltip("A delicious bundle of food.\nContains\n*Apple\n*Carrot\n*Potato")))
//                )
//                .department(DepartmentBuilder.of("crafting_central", new ItemIcon(Items.CRAFTING_TABLE), "Crafting Central")
//                        .listing(ListingBuilder.of("furnace")
//                                .addSublisting(SublistingBuilder.item(Items.FURNACE)
//                                        .material(Tags.Items.COBBLESTONE, 8)))
//                        .listing(ListingBuilder.of("stairs")
//                                .stockMechanic("only_1")
//                                .addSublisting(SublistingBuilder.item(Items.PISTON)
//                                        .material(Tags.Items.COBBLESTONE, 6)
//                                        .material(Tags.Items.DUSTS_REDSTONE, 1)
//                                        .material(ItemTags.PLANKS, 2)
//                                        .material(Tags.Items.INGOTS_IRON, 1)))
//                        .listing(ListingBuilder.of("piggy_bank").addSublisting(SublistingBuilder.department("test_department")))
//                )
//                .save(this);
    }

    public static class StockMechanicBuilder {
        public final String id;
        public int max = Integer.MAX_VALUE;
        public int replenish = Integer.MAX_VALUE;

        public StockMechanicBuilder(String id) {
            this.id = id;
        }

        public static StockMechanicBuilder of(String id) {
            return new StockMechanicBuilder(id);
        }

        public StockMechanicBuilder max(int max) {
            this.max = max;
            return this;
        }

        public StockMechanicBuilder replenishRate(int rate) {
            this.replenish = rate;
            return this;
        }
    }

    public void addStockMechanic(String id, int maxStock, int replenishRate) {
        addEntry("stock_mechanics", "ID,Max Stock,Replenish Rate", CSVUtils.join(id, maxStock, replenishRate));
    }

    public void addSellValueForItem(Item item, int value) {
        addEntry("item_values", "Item,Value", CSVUtils.join(item.getRegistryName().toString(), value));
    }

    public String subfolder = Strings.EMPTY;

    @Override
    public void addEntry(String file, String titles, String csv) {
        if (subfolder.equals(Strings.EMPTY)) super.addEntry(file, titles, csv);
        else super.addEntry(subfolder + "/" + file, titles, csv);
    }


}
