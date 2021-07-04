package uk.joshiejack.shopaholic.api.shop;

import net.minecraft.entity.player.PlayerEntity;
import uk.joshiejack.shopaholic.shop.Sublisting;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.Random;

public interface CostFormula {
    /**
     * A cost formula will take the default cost value of an item and manipulate it in some way
     * this can make items cost more as there are less in stock, or they can get cheaper
     * or you all sorts of other mechanics
     *
     * @param errorValue        return this if there is an error with the script
     * @param player            the player entity that is calling this script
     * @param subListing        the item we are trying to purchase [sublisting.getCost() for the default cost]
     * @param stockLevel        the current stock level of this listing
     * @param stockMechanic     the stock mechanic used, so you can know the maximum
     * @param random            a seeded random for this shop, if you need to do a random thing always use this instance
     * @return                  the cost of the item
     */
    long getCost(long errorValue, PlayerEntity player, Sublisting<?> subListing, int stockLevel, StockMechanic stockMechanic, Random random);
}