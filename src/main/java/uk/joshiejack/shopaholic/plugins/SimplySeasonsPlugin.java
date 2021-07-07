package uk.joshiejack.shopaholic.plugins;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.IModPlugin;
import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonCondition;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonDaysComparator;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonPredicateCondition;
import uk.joshiejack.simplyseasons.world.season.SeasonalWorlds;

@PenguinLoader("simplyseasons")
public class SimplySeasonsPlugin implements IModPlugin {
    public static boolean loaded;

    public static int getTemperature(ShopTarget target) {
        return (int) SeasonalWorlds.getTemperature(target.getWorld(), target.getWorld().getBiome(target.getPos()), target.getPos());
    }

    @Override
    public void setup() {
        loaded = true;
        ShopaholicAPI.registry.registerComparator("season_days", new SeasonDaysComparator());
        ShopaholicAPI.registry.registerCondition("season", new SeasonCondition());
        ShopaholicAPI.registry.registerCondition("season_predicate", new SeasonPredicateCondition());
    }
}
