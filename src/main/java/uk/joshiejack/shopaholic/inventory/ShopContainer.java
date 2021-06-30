package uk.joshiejack.shopaholic.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import uk.joshiejack.shopaholic.Shopaholic;

import javax.annotation.Nonnull;

public class ShopContainer extends Container {
      public ShopContainer(int windowID) {
        super(Shopaholic.ShopaholicContainers.SHOP.get(), windowID);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        return true;
    }
}
