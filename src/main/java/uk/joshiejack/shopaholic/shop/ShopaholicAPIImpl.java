package uk.joshiejack.shopaholic.shop;

import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.api.shop.Comparator;

public class ShopaholicAPIImpl implements ShopaholicAPI.IShopaholicAPI {
    @Override
    public void registerComparator(String name, Comparator comparator) {
        ShopRegistries.COMPARATORS.put(name, comparator);
    }
}
