package uk.joshiejack.shopaholic.data.shop.condition;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.simplyseasons.api.Season;

import java.util.Locale;

//Conditions
public class SeasonConditionBuilder extends ConditionBuilder {
    private final Season season;

    public SeasonConditionBuilder(String id, Season season) {
        super(id);
        this.season = season;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_season", "ID,Season", CSVUtils.join(id, season.name().toLowerCase(Locale.ROOT)));
    }
}