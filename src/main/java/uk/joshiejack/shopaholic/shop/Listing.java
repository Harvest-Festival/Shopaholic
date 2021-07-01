package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.util.loot.LootRegistryWithID;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.event.ItemPurchasedEvent;
import uk.joshiejack.shopaholic.shop.inventory.Inventory;
import uk.joshiejack.shopaholic.shop.inventory.Stock;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Listing {
    protected final List<Condition> conditions = Lists.newArrayList();
    private final LootRegistryWithID<Sublisting> sublistings = new LootRegistryWithID<>();
    private final String listing_id;
    protected final Department department;
    private final StockMechanic stockMechanic;
    private final CostFormula costScript;
    private static final Random random = new Random();

    public Listing(Department department, String listing_id, List<Sublisting> sublistings, CostFormula costScript, StockMechanic stockMechanic) {
        this.department = department;
        this.listing_id = listing_id;
        this.costScript = costScript;
        this.stockMechanic = stockMechanic;
        this.department.addListing(this);
        for (int id = 0; id < sublistings.size(); id++) {
            this.sublistings.add(id, sublistings.get(id).setIntID(id), sublistings.get(id).getWeight());
        }
    }

    public LootRegistryWithID<Sublisting> getSublistings() { return sublistings; }

    public boolean isSingleEntry() {
        return sublistings.isSingleEntry();
    }

    public Sublisting getSublistingByID(int id) {
        return sublistings.byID(id);
    }

    public int getRandomID(Random random) {
        return sublistings.get(random).int_id();
    }

    public StockMechanic getStockMechanic() {
        return stockMechanic;
    }

    public String getID() {
        return listing_id;
    }

    public long getGoldCost(PlayerEntity player, Stock stock) {
        if (costScript == null) {
            Shopaholic.LOGGER.log(Level.ERROR, "Cost script was null for: " + department.id() + ":" + listing_id);
            return 0L;
        } else {
            random.setSeed(player.getPersistentData().getCompound("PenguinStatuses").getInt("ShopSeed") + listing_id.hashCode() * 3643257684289L); //Get the shop seed
            long result = costScript.getCost(999999999L, getSubListing(stock), stock.getStockLevel(this), stockMechanic, random);
            if (result == 999999999L) {
                Shopaholic.LOGGER.log(Level.ERROR, "Had an error while processing getCost for the item: " + department.id() + ":" + listing_id);
                return 999999999;
            } else return result;
        }
    }

    public Sublisting getSubListing(Stock stock) {
        return isSingleEntry() ? getSublistingByID(0) : getSublistingByID(stock.getStockedObject(this));
    }

    /**
     * If this can be listed for this player
     **/
    public boolean canList(@Nonnull ShopTarget target, Stock stock) {
        for (Condition condition : conditions) {
            if (!condition.valid(target, department, this, Condition.CheckType.SHOP_LISTING)) {
                return false;
            }
        }

        return stock.getStockLevel(this) > 0;
    }

    /**
     * If the player is able to purchase this
     * Gold has already been checked
     **/
    public boolean canPurchase(PlayerEntity player, Stock stock, int amount) {
        return getSubListing(stock).hasMaterialRequirement(player, amount) && stock.getStockLevel(this) >= amount;
    }

    public void purchase(PlayerEntity player) {
        Stock stock = department.getStockLevels();
        stock.decreaseStockLevel(this);
        getSubListing(stock).purchase(player);
        if (!player.level.isClientSide)
            Inventory.setChanged((ServerWorld) player.level);
        conditions.forEach(c -> c.onPurchase(player, department, this));
        MinecraftForge.EVENT_BUS.post(new ItemPurchasedEvent(player, department, this));
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Listing listing = (Listing) o;
        return Objects.equals(listing_id, listing.listing_id) && Objects.equals(department, listing.department);
    }

    @Override
    public int hashCode() {
        return Objects.hash(listing_id, department);
    }

    public Department getDepartment() {
        return department;
    }
}
