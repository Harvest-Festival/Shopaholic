package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class GoldListingBuilder extends SublistingBuilder<GoldListingBuilder> {
    public GoldListingBuilder() {
        super("gold", String.valueOf(0));
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
