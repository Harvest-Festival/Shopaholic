package uk.joshiejack.shopaholic.plugins.kubejs.event;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerEventJS;
import uk.joshiejack.shopaholic.event.ItemPurchasedEvent;
import uk.joshiejack.shopaholic.plugins.kubejs.wrapper.ListingJS;

public class ItemPurchasedEventJS extends PlayerEventJS {
    private final ItemPurchasedEvent event;

    public ItemPurchasedEventJS(ItemPurchasedEvent event) {
        this.event = event;
    }

    @Override
    public EntityJS getEntity() {
        return entityOf(event.getPlayer());
    }

    public ListingJS getListing() {
        return new ListingJS(event.getListing());
    }
}