package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shop.IImutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class PlayerHealthLevelComparator implements IImutableComparator {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return (int) target.getPlayer().getHealth();
    }
}