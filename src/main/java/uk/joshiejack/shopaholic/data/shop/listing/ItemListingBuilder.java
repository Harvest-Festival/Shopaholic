package uk.joshiejack.shopaholic.data.shop.listing;

import net.minecraft.item.Item;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class ItemListingBuilder extends SublistingBuilder<ItemListingBuilder> {
    public ItemListingBuilder(Item item) {
        super("item", item.getRegistryName().toString());
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
