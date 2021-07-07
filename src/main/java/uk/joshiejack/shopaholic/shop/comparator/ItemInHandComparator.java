package uk.joshiejack.shopaholic.shop.comparator;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class ItemInHandComparator extends AbstractItemComparator {
    @Override
    public AbstractItemComparator create() {
        return new ItemInHandComparator();
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return Lists.newArrayList(target.getPlayer().getMainHandItem(), target.getPlayer().getOffhandItem()).stream()
                .filter(stack -> items.contains(stack.getItem()) || tags.stream().anyMatch(tag -> tag.contains(stack.getItem())))
                .mapToInt(ItemStack::getCount)
                .sum();
    }
}