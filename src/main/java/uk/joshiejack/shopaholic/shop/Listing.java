package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Level;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.loot.LootRegistryWithID;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.event.ItemPurchasedEvent;
import uk.joshiejack.shopaholic.network.shop.SyncStockLevelPacket;
import uk.joshiejack.shopaholic.api.shop.CostFormula;
import uk.joshiejack.shopaholic.shop.inventory.Inventory;
import uk.joshiejack.shopaholic.shop.inventory.Stock;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class Listing {
    protected final List<Condition> conditions = Lists.newArrayList();
    private final LootRegistryWithID<Sublisting<?>> sublistings = new LootRegistryWithID<>();
    private final String listing_id;
    protected final Department department;
    private final StockMechanic stockMechanic;
    private final CostFormula costScript;
    private static final Random random = new Random();

    public Listing(Department department, String listing_id, List<Sublisting<?>> sublistings, CostFormula costScript, StockMechanic stockMechanic) {
        this.department = department;
        this.listing_id = listing_id;
        this.costScript = costScript;
        this.stockMechanic = stockMechanic;
        this.department.addListing(this);
        sublistings.forEach(sublisting -> this.sublistings.add(sublisting.id(), sublisting, sublisting.getWeight()));
    }

    public LootRegistryWithID<Sublisting<?>> getSublistings() { return sublistings; }

    public boolean isSingleEntry() {
        return sublistings.isSingleEntry();
    }

    public Sublisting<?> getSublistingByID(String id) {
        return sublistings.byID(id);
    }

    public String getRandomID(Random random) {
        return sublistings.get(random).id();
    }

    public StockMechanic getStockMechanic() {
        return stockMechanic;
    }

    public String id() {
        return listing_id;
    }

    public long getGoldCost(PlayerEntity player, Stock stock) {
        if (costScript == null) {
            Shopaholic.LOGGER.log(Level.ERROR, "Cost script was null for: " + department.id() + ":" + listing_id);
            return 0L;
        } else {
            random.setSeed(player.getPersistentData().getInt("ShopaholicSeed") + listing_id.hashCode() * 3643257684289L); //Get the shop seed
            long result = costScript.getCost(999999999L, player, getSubListing(stock), stock.getStockLevel(this), stockMechanic, random);
            if (result == 999999999L) {
                Shopaholic.LOGGER.log(Level.ERROR, "Had an error while processing getCost for the item: " + department.id() + ":" + listing_id);
                return 999999999;
            } else return result;
        }
    }

    public Sublisting<?> getSubListing(Stock stock) {
        return isSingleEntry() ? sublistings.get(random) : getSublistingByID(stock.getStockedObject(this));
    }

    /**
     * If this can be listed for this player
     **/
    public boolean canList(@Nonnull ShopTarget target, Stock stock) {
        return stock.getStockLevel(this) > 0
                && conditions.stream().allMatch(condition -> condition.valid(target, department, this, Condition.CheckType.SHOP_LISTING));
    }

    /**
     * If the player is able to purchase this
     * Gold has already been checked
     **/
    public boolean canPurchase(PlayerEntity player, Stock stock, int amount) {
        return getSubListing(stock).hasMaterialRequirement(player, amount) && stock.getStockLevel(this) >= amount;
    }

    public void purchase(PlayerEntity player) {
        Stock stock = department.getStockLevels(player.level);
        getSubListing(stock).purchase(player);
        if (!player.level.isClientSide) {
            stock.decreaseStockLevel(this);
            PenguinNetwork.sendToClient(new SyncStockLevelPacket(this, stock.getStockLevel(this)), (ServerPlayerEntity) player);
            Inventory.setChanged((ServerWorld) player.level);
        }

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
