package uk.joshiejack.shopaholic.event;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

public class ItemPurchasedEvent extends PlayerEvent {
    private final Department shop;
    private final Listing purchasable;

    public ItemPurchasedEvent(PlayerEntity player, Department shop, Listing purchasable) {
        super(player);
        this.shop = shop;
        this.purchasable = purchasable;
    }

    public Department getShop() {
        return shop;
    }

    public Listing getListing() {
        return purchasable;
    }
}
