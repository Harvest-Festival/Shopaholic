package uk.joshiejack.shopaholic.shop.comparator;

import com.google.common.collect.Streams;
import net.minecraft.item.Item;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class ItemInInventoryComparator extends Comparator {
    private Item item;

    @Override
    public Comparator create(Row data, String id) {
        ItemInInventoryComparator comparator = new ItemInInventoryComparator();
        comparator.item = data.item();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return Streams.concat(target.player.inventory.items.stream(), target.player.inventory.armor.stream(), target.player.inventory.offhand.stream())
                .mapToInt(stack -> stack.getItem() == item ? stack.getCount() : 0).sum();
    }
}
