package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.item.ItemStack;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class ItemInInventoryComparator extends AbstractItemComparator {
    @Override
    public AbstractItemComparator create() {
        return new ItemInInventoryComparator();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return PlayerHelper.getInventoryStream(target.getPlayer())
                .filter(stack -> items.contains(stack.getItem()) || tags.stream().anyMatch(tag -> tag.contains(stack.getItem())))
                .mapToInt(ItemStack::getCount)
                .sum();
    }
}