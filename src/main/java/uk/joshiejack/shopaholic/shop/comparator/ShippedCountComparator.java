package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.util.Set;

public class ShippedCountComparator extends AbstractItemComparator {
    @Override
    public AbstractItemComparator create() {
        return new ShippedCountComparator();
    }

    private Set<Shipping.SoldItem> getHolderSet(World world, PlayerEntity player) {
        return world.isClientSide ? Shipped.getSold() : Market.get((ServerWorld) world).getShippingForPlayer(player).getSold();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        int total = 0;
        Set<Shipping.SoldItem> sold = getHolderSet(target.getWorld(), target.getPlayer());
        for (Shipping.SoldItem holder : sold) {
            if (items.stream().anyMatch(holder::matches) ||
                    tags.stream().anyMatch(holder::matches)) {
                total += holder.getStack().getCount();
            }
        }

        return total;
    }
}
