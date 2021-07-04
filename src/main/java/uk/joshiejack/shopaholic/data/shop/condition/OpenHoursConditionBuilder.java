package uk.joshiejack.shopaholic.data.shop.condition;

import com.google.common.collect.Lists;
import net.minecraft.util.RangedInteger;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Conditions
public class OpenHoursConditionBuilder extends ConditionBuilder {
    protected final Map<DayOfWeek, RangedInteger> hours = new HashMap<>();

    public OpenHoursConditionBuilder(String id) {
        super(id);
    }

    @Override
    public void save(ShopaholicDatabase data) {
        List<Object> entries = Lists.newArrayList(id);
        for (DayOfWeek day: TimeHelper.DAYS) {
            RangedInteger pair = hours.computeIfAbsent(day, (d) -> RangedInteger.of(0, 0));
            entries.add(pair.getMinInclusive());
            entries.add(pair.getMaxInclusive());
        }

        data.addEntry("condition_opening_hours",
                "ID,Mon Open,Mon Close,Tue Open,Tue Close,Wed Open,Wed Close,Thu Open,Thu Close,Fri Open,Fri Close,Sat Open,Sat Close,Sun Open,Sun Close",
                CSVUtils.join(entries.toArray()));
    }

    public OpenHoursConditionBuilder withHours(DayOfWeek day, int opening, int closing) {
        hours.put(day, RangedInteger.of(opening, closing));
        return this;
    }
}
