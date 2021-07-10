package uk.joshiejack.shopaholic.plugins.simplyseasons;

import net.minecraftforge.common.util.LazyOptional;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.simplyseasons.api.ISeasonProvider;
import uk.joshiejack.simplyseasons.api.SSeasonsAPI;
import uk.joshiejack.simplyseasons.api.Season;

import javax.annotation.Nonnull;

public class SeasonCondition implements Condition {
    private Season season;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        LazyOptional<ISeasonProvider> provider = target.getWorld().getCapability(SSeasonsAPI.SEASONS_CAPABILITY);
        return provider.isPresent() && provider.resolve().get().getSeason(target.getWorld()) == season;
    }

    @Override
    public Condition create(ShopLoadingData data, Row row, String id) {
        SeasonCondition condition = new SeasonCondition();
        condition.season = row.getAsEnum(Season.class);
        return condition;
    }
}