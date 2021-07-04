package uk.joshiejack.shopaholic.shop.condition;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.List;

public class TimeCondition implements Condition {
    private final List<Pair<Integer, Integer>> times = Lists.newArrayList();

    public TimeCondition() {}

    @Override
    public Condition create(Row data, String id) {
        TimeCondition validator = new TimeCondition();
        int open = data.getTime("open");
        int close = data.getTime("close");
        if (open >= 0 && close >= 0) {
            validator.times.add(Pair.of(open, close));
        }

        return validator;
    }

    @Override
    public void merge(Row data) {
        int open = data.getTime("open");
        int close = data.getTime("close");
        if (open >= 0 && close >= 0) {
            times.add(Pair.of(open, close));
        }
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (type == CheckType.SHOP_EXISTS) return true; //Always true no matter the time
        for (Pair<Integer, Integer> time: times) {
            if (TimeHelper.isBetween(target.getWorld(), time.getLeft(), time.getRight())) return true;
        }

        return false;
    }
}
