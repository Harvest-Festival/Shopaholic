package uk.joshiejack.shopaholic.shop.condition;

import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.time.DayOfWeek;
import java.util.EnumMap;

public class OpeningHoursCondition extends Condition {
    private final EnumMap<DayOfWeek, Pair<Integer, Integer>> hours = new EnumMap<>(DayOfWeek.class);

    @Override
    public Condition create(Row data, String id) {
        OpeningHoursCondition validator = new OpeningHoursCondition();
        for (DayOfWeek weekday: TimeHelper.DAYS) {
            int open = data.getTime(TimeHelper.shortName(weekday) + "_start");
            int close = data.getTime(TimeHelper.shortName(weekday) + "_end");
            if (open >= 0 && close >= 0) {
                validator.hours.put(weekday, Pair.of(open, close));
            }
        }

        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (type == CheckType.SHOP_EXISTS) return true; //Always add to the entity
        long time = target.getWorld().getDayTime();
        DayOfWeek weekday = TimeHelper.getWeekday(time);
        long timeOfDay = TimeHelper.getTimeOfDay(time);
        Pair<Integer, Integer> hours = this.hours.get(weekday);
        return hours != null && timeOfDay >= hours.getLeft() && timeOfDay <= hours.getRight();
    }
}
