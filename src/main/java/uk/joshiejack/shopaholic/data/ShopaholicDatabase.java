package uk.joshiejack.shopaholic.data;

import joptsimple.internal.Strings;
import net.minecraft.data.DataGenerator;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.data.shop.DepartmentBuilder;
import uk.joshiejack.shopaholic.data.shop.ShopBuilder;
import uk.joshiejack.shopaholic.data.shop.Vendor;
import uk.joshiejack.shopaholic.data.shop.condition.ConditionBuilder;
import uk.joshiejack.shopaholic.data.shop.listing.ListingBuilder;
import uk.joshiejack.shopaholic.data.shop.listing.SublistingBuilder;
import uk.joshiejack.shopaholic.shop.input.InputMethod;

import java.util.Random;

public class ShopaholicDatabase extends AbstractDatabaseProvider {
    public ShopaholicDatabase(DataGenerator gen) {
        super(gen, Shopaholic.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        Random random = new Random();
        //Experimental
        for (Item item : ForgeRegistries.ITEMS.getValues()) {
            if (item == Items.AIR) continue;
            addSellValueForItem(item, random.nextInt(3) < 2 ? random.nextInt(1000) : 1 + random.nextInt(100000));
        }

        //Stock mechanics
        addStockMechanic("unlimited", Integer.MAX_VALUE, Integer.MAX_VALUE);
        addStockMechanic("limited_1", 1, 1);
        addStockMechanic("only_1", 1, 0);



        ShopBuilder.of("test_shop", "Piggy Bank")
                .vendor(Vendor.entity("pig", EntityType.PIG))
                .condition(ConditionBuilder.named("pig_name", "George"))
                .openWith(InputMethod.RIGHT_CLICK)
                .department(DepartmentBuilder.of("test_department", new ItemIcon(Items.GOLD_INGOT), "Gold Store")
                        .listing(ListingBuilder.of("apple").addSublisting(SublistingBuilder.item(Items.APPLE).cost(200).icon(new ItemIcon(Items.ACACIA_PLANKS))))
                        .listing(ListingBuilder.of("bucket").addSublisting(SublistingBuilder.item(Items.BUCKET).cost(100)))
                        .listing(ListingBuilder.of("poison").addSublisting(SublistingBuilder.potion("poison_effect", new EffectInstance(Effects.POISON, 1000))))
                        .listing(ListingBuilder.of("food").addSublisting(SublistingBuilder.bundle("food_bundle")
                                .addToBundle(SublistingBuilder.item(Items.CARROT))
                                .addToBundle(SublistingBuilder.item(Items.POTATO))
                                .addToBundle(SublistingBuilder.item(Items.BEETROOT))
                                .cost(9000)
                                .name("Food Bundle")
                                .tooltip("A delicious bundle of food.\nContains\n*Apple\n*Carrot\n*Potato")))
                )
                .department(DepartmentBuilder.of("crafting_central", new ItemIcon(Items.CRAFTING_TABLE), "Crafting Central")
                        .listing(ListingBuilder.of("furnace")
                                .addSublisting(SublistingBuilder.item(Items.FURNACE)
                                        .material(Tags.Items.COBBLESTONE, 8)))
                        .listing(ListingBuilder.of("stairs")
                                .stockMechanic("only_1")
                                .addSublisting(SublistingBuilder.item(Items.PISTON)
                                        .material(Tags.Items.COBBLESTONE, 6)
                                        .material(Tags.Items.DUSTS_REDSTONE, 1)
                                        .material(ItemTags.PLANKS, 2)
                                        .material(Tags.Items.INGOTS_IRON, 1)))
                        .listing(ListingBuilder.of("piggy_bank").addSublisting(SublistingBuilder.department("test_department")))
                )
                .save(this);
    }

    protected void addStockMechanic(String id, int maxStock, int replenishRate) {
        addEntry("stock_mechanics", "ID,Max Stock,Replenish Rate", CSVUtils.join(id, maxStock, replenishRate));
    }

    protected void addSellValueForItem(Item item, int value) {
        addEntry("item_values", "Item,Value", CSVUtils.join(item.getRegistryName().toString(), value));
    }

    public String subfolder = Strings.EMPTY;

    @Override
    public void addEntry(String file, String titles, String csv) {
        if (subfolder.equals(Strings.EMPTY)) super.addEntry(file, titles, csv);
        else super.addEntry(subfolder + "/" + file, titles, csv);
    }


}
