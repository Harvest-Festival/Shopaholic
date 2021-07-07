package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shop.IImutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class RainLevelComparator implements IImutableComparator {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.getWorld().isThundering() && target.getWorld().isRaining() ? 2 : target.getWorld().isRaining() ? 1 : 0;
    }
}