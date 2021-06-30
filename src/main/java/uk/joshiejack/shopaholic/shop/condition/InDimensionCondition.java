package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class InDimensionCondition extends Condition {
    private ResourceLocation dimension;

    @Override
    public Condition create(Row data, String id) {
        InDimensionCondition condition = new InDimensionCondition();
        condition.dimension = data.getRL("dimension");
        return condition;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return target.getWorld().dimension().location().equals(dimension);
    }
}
