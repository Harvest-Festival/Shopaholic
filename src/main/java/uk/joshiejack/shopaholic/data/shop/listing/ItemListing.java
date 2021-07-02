package uk.joshiejack.shopaholic.data.shop.listing;

import net.minecraft.item.Item;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class ItemListing extends SublistingBuilder {
    public ItemListing(Item item) {
        super("item", item.getRegistryName().toString());
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
