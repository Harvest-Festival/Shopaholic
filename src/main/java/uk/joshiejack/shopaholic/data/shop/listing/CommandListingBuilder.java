package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class CommandListingBuilder extends SublistingBuilder<CommandListingBuilder> {
    @SuppressWarnings("unused")
    public CommandListingBuilder(String command) {
        super("command", command);
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
