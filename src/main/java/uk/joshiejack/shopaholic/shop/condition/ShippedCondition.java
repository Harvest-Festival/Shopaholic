package uk.joshiejack.shopaholic.shop.condition;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ShippedCondition implements Condition {
    private List<ItemStack> stacks = Lists.newArrayList();
    private int count;

    @Override
    public Condition create(Row data, String id) {
        ShippedCondition condition = new ShippedCondition();
        condition.count = Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(id)); //Grab the required count from the id
        condition.stacks.add(new ItemStack(data.item()));
        return condition;
    }

    @Override
    public void merge(Row data) {
        stacks.add(new ItemStack(data.item()));
    }

    private Set<Shipping.SoldItem> getHolderSet(World world, PlayerEntity player) {
        return world.isClientSide ? Shipped.getSold() : Market.get((ServerWorld) world).getShippingForPlayer(player).getSold();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        int total = 0;
        Set<Shipping.SoldItem> sold = getHolderSet(target.getWorld(), target.getPlayer());
        for (ItemStack stack : stacks) {
            for (Shipping.SoldItem holder : sold) {
                if (holder.matches(stack)) {
                    total += holder.getStack().getCount();
                    //Return early if this is true, no need to keep counting............
                    if (total >= count) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}
