package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.IImmutable;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class LightLevelComparator implements Comparator, IImmutable {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.getWorld().getLightEmission(target.getPos());
    }
}