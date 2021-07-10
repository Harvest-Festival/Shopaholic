package uk.joshiejack.shopaholic.shop.condition;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;
import java.util.regex.Pattern;

public class PlayerNamed implements Condition {
    private Pattern pattern;

    @Override
    public Condition create(ShopLoadingData loadingData, Row data, String id) {
        PlayerNamed validator = new PlayerNamed();
        validator.pattern = Pattern.compile(data.name());
        return validator;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return pattern.matcher(target.getPlayer().getName().getString()).matches();
    }
}
