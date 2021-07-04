package uk.joshiejack.shopaholic.shop;

import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.CostFormula;

public class ShopaholicAPIImpl implements ShopaholicAPI.IShopaholicAPI {
    @Override
    public void registerCostFormula(String name, CostFormula formula) {
        ShopRegistries.COST_FORMULAE.put(name, formula);
    }

    @Override
    public void registerComparator(String name, Comparator comparator) {
        ShopRegistries.COMPARATORS.put(name, comparator);
    }
}
