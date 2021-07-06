package uk.joshiejack.shopaholic.shop;

import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.CostFormula;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

public class RegistryImpl implements ShopaholicAPI.IRegistry {
    @Override
    public void registerCostFormula(String name, CostFormula formula) {
        ShopRegistries.COST_FORMULAE.put(name, formula);
    }

    @Override
    public void registerListingHandler(String name, ListingHandler<?> handler) {
        ShopRegistries.LISTING_HANDLERS.put(name, handler);
    }

    @Override
    public void registerComparator(String name, Comparator comparator) {
        ShopRegistries.COMPARATORS.put(name, comparator);
    }

    @Override
    public void registerCondition(String name, Condition handler) {
        ShopRegistries.CONDITIONS.put(name, handler);
    }
}
