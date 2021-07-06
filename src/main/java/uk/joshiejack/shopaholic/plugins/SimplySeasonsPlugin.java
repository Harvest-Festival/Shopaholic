package uk.joshiejack.shopaholic.plugins;

import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.IModPlugin;
import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonCondition;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonPredicateCondition;
import uk.joshiejack.shopaholic.plugins.simplyseasons.SeasonDaysComparator;

@PenguinLoader("simplyseasons")
public class SimplySeasonsPlugin implements IModPlugin {
    @Override
    public void setup() {
        ShopaholicAPI.registry.registerComparator("season_days", new SeasonDaysComparator());
        ShopaholicAPI.registry.registerCondition("season", new SeasonCondition());
        ShopaholicAPI.registry.registerCondition("season_predicate", new SeasonPredicateCondition());
    }
}
