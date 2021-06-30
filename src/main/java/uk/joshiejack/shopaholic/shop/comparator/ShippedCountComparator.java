package uk.joshiejack.shopaholic.shop.comparator;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;
import uk.joshiejack.shopaholic.client.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;

public class ShippedCountComparator extends Comparator {
    private List<ItemStack> stacks = Lists.newArrayList();

    @Override
    public Comparator create(Row data, String id) {
        ShippedCountComparator comparator = new ShippedCountComparator();
        comparator.stacks.add(new ItemStack(data.item()));
        return comparator;
    }

    @Override
    public void merge(Row data) {
        stacks.add(new ItemStack(data.item()));
    }

    private Set<Shipping.SoldItem> getHolderSet(World world, PlayerEntity player) {
        return world.isClientSide ? Shipped.getSold() : Market.get((ServerWorld) world).getShippingForPlayer(player).getSold();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        int total = 0;
        Set<Shipping.SoldItem> sold = getHolderSet(target.world, target.player);
        for (ItemStack stack : stacks) {
            for (Shipping.SoldItem holder: sold) {
                if (holder.matches(stack)) {
                    total += holder.getStack().getCount();
                }
            }
        }

        return total;
    }
}
