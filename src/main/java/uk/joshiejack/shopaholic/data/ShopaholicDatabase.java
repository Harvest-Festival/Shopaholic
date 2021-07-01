package uk.joshiejack.shopaholic.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;
import uk.joshiejack.shopaholic.Shopaholic;

import java.util.Random;

public class ShopaholicDatabase extends AbstractDatabaseProvider {
    public ShopaholicDatabase(DataGenerator gen) {
        super(gen, Shopaholic.MODID);
    }

    @Override
    protected void addDatabaseEntries() {
        Random random = new Random();
        //Experimental
        for (Item item: ForgeRegistries.ITEMS.getValues()) {
            addSellValueForItem(item, random.nextInt(3) < 2 ? random.nextInt(1000) : 1 + random.nextInt(100000));
        }
    }

    protected void addSellValueForItem(Item item, int value) {
        addEntry("item_values", "Item,Sell Value", CSVUtils.join(item.getRegistryName().toString(), value));
    }
}
