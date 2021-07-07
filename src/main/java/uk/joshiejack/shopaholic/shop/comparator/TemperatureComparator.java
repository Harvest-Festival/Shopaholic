package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.shopaholic.api.shop.IImutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.plugins.SimplySeasonsPlugin;

import javax.annotation.Nonnull;

public class TemperatureComparator implements IImutableComparator {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return SimplySeasonsPlugin.loaded ? SimplySeasonsPlugin.getTemperature(target) : (int) target.getWorld().getBiome(target.getPos()).getTemperature(target.getPos());
    }
}