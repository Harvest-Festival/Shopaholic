package uk.joshiejack.shopaholic.shop.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.MapHelper;
import uk.joshiejack.penguinlib.util.helpers.MathsHelper;
import uk.joshiejack.shopaholic.client.ShopaholicClient;
import uk.joshiejack.shopaholic.network.shop.SetStockedItemPacket;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

import java.util.Random;

public class Stock implements INBTSerializable<CompoundNBT> {
    private static final Random initialRandom = new Random();
    private final Object2IntMap<Listing> stockLevels = new Object2IntOpenHashMap<>();
    private final Object2IntMap<Listing> stockItems = new Object2IntOpenHashMap<>();
    private final Department department;

    public Stock(Department department) {
        this.department = department;
        for (Listing listing : department.getListings())
            if (!listing.isSingleEntry() && !stockItems.containsKey(listing))
                stockItems.put(listing, listing.getRandomID(initialRandom));
    }

    public void decreaseStockLevel(Listing listing) {
        MapHelper.adjustOrPut(stockLevels, listing, -1, listing.getStockMechanic().getMaximum() - 1);
    }

    public void setStockedItem(Listing listing, int stockID) {
        stockItems.put(listing, stockID);
    }

    @OnlyIn(Dist.CLIENT)
    public void setStockLevel(Listing listing, int stock) {
        stockLevels.put(listing, stock);
        if (stock <= 0)
            ShopaholicClient.refreshShop(); //Stock levels have changed
    }

    public int getStockedObject(Listing listing) {
        if (!stockItems.containsKey(listing)) {
            stockItems.put(listing, listing.getRandomID(initialRandom));
            return 0;
        } else return stockItems.getInt(listing);
    }

    public int getStockLevel(Listing listing) {
        return MapHelper.adjustOrPut(stockLevels, listing, 0, listing.getStockMechanic().getMaximum());
    }

    public void newDay(Random random) {
        for (Listing listing : stockLevels.keySet()) {
            StockMechanic mechanic = listing.getStockMechanic();
            stockLevels.put(listing, MathsHelper.constrainToRangeInt(getStockLevel(listing) + mechanic.getIncrease(), 0, mechanic.getMaximum()));
        }

        for (Listing listing : stockItems.keySet()) {
            int id = listing.getRandomID(random);
            stockItems.put(listing, id);
            PenguinNetwork.sendToEveryone(new SetStockedItemPacket(department, listing, id));
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT data = new CompoundNBT();
        ListNBT list = new ListNBT();
        stockLevels.forEach((key, value) -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Key", key.id());
            tag.putInt("Value", value);
            list.add(tag);
        });

        data.put("Levels", list);

        ListNBT stockList = new ListNBT();
        stockItems.forEach((key, value) -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Key", key.id());
            tag.putInt("Value", value);
            stockList.add(tag);
        });

        data.put("Stock", stockList);

        return data;
    }

    @Override
    public void deserializeNBT(CompoundNBT data) {
        stockLevels.clear(); // using the same instance
        stockItems.clear(); // so make sure to clear this each time
        ListNBT nbt = data.getList("Levels", 10);
        for (int i = 0; i < nbt.size(); i++) {
            CompoundNBT tag = nbt.getCompound(i);
            Listing listing = department.getListingByID(tag.getString("Key"));
            if (listing != null)
                stockLevels.put(listing, tag.getInt("Value"));
        }

        ListNBT stock = data.getList("Stock", 10);
        for (int i = 0; i < stock.size(); i++) {
            CompoundNBT tag = stock.getCompound(i);
            Listing listing = department.getListingByID(tag.getString("Key"));
            if (listing != null)
                stockItems.put(listing, tag.getInt("Value"));
        }
    }
}
