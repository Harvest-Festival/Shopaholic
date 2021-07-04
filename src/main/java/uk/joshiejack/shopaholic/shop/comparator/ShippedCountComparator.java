package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShippedCountComparator implements Comparator {
    private final List<Item> items = new ArrayList<>();
    private final List<ITag.INamedTag<Item>> tags = new ArrayList<>();

    @Override
    public Comparator create(Row data) {
        ShippedCountComparator comparator = new ShippedCountComparator();
        addEntry(comparator, data);
        return comparator;
    }

    @Override
    public void merge(Row data) {
        addEntry(this, data);
    }

    private void addEntry(ShippedCountComparator comparator, Row data) {
        String item = data.get("item");
        if (item.startsWith("tag:"))
            comparator.tags.add(ItemTags.createOptional(new ResourceLocation(item.replace("tag:", ""))));
        else {
            Item theItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
            if (theItem != Items.AIR)
                comparator.items.add(theItem);
        }
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
