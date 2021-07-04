package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.block.BlockState;
import net.minecraft.state.Property;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class BlockStateCondition implements Condition {
    private String key;
    private String value;

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        if (type == CheckType.SHOP_LISTING) return false;
        else {
            BlockState state = target.getWorld().getBlockState(target.getPos());
            Property<?> property = state.getBlock().getStateDefinition().getProperty(key);
            return property != null && state.getValue(property).toString().equals(value);
        }
    }

    @Override
    public Condition create(Row data, String id) {
        BlockStateCondition validator = new BlockStateCondition();
        validator.key = data.get("state name");
        validator.value = data.get("state value");
        return validator;
    }
}
