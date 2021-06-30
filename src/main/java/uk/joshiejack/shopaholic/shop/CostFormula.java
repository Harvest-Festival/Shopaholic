package uk.joshiejack.shopaholic.shop;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.Map;
import java.util.Random;

public interface CostFormula {
    Map<String, CostFormula> COST_FORMULAE = new Object2ObjectOpenHashMap<>();

    long getCost(long maxValue, Sublisting<?> subListing, int stockLevel, StockMechanic stockMechanic, Random random);

    static void register(String name, CostFormula formula) {
        COST_FORMULAE.put(name, formula);
    }
}