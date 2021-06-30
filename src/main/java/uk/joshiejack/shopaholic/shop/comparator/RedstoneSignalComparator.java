package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class RedstoneSignalComparator extends AbstractImmutableComparator {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.world.getDirectSignalTo(target.pos);
    }
}
