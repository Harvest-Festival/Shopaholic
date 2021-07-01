package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.item.Item;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class ItemInInventoryComparator implements Comparator {
    private Item item;

    @Override
    public Comparator create(Row data) {
        ItemInInventoryComparator comparator = new ItemInInventoryComparator();
        comparator.item = data.item();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return PlayerHelper.getInventoryStream(target.getPlayer())
                .mapToInt(stack -> stack.getItem() == item ? stack.getCount() : 0)
                .sum();
    }
}