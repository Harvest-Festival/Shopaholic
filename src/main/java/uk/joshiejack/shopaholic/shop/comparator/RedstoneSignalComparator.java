package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.IImmutable;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class RedstoneSignalComparator implements Comparator, IImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.getWorld().getDirectSignalTo(target.getPos());
    }
}
