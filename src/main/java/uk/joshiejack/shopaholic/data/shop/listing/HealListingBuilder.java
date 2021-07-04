package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class HealListingBuilder extends SublistingBuilder<HealListingBuilder> {
    public HealListingBuilder(float healAmount) {
        super("heal", String.valueOf(healAmount));
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
