package uk.joshiejack.shopaholic.plugins.kubejs.wrapper;

import uk.joshiejack.shopaholic.shop.Shop;

public class ShopJS {
    private final Shop shop;

    public ShopJS(Shop shop) {
        this.shop = shop;
    }

    public String id() {
        return shop.id();
    }
}
