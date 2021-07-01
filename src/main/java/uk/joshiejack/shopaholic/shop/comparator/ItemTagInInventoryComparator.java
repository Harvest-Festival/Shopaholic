package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class ItemTagInInventoryComparator implements Comparator {
    private ITag.INamedTag<Item> tag;

    @Override
    public Comparator create(Row data) {
        ItemTagInInventoryComparator comparator = new ItemTagInInventoryComparator();
        comparator.tag = data.itemTag();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return PlayerHelper.getInventoryStream(target.getPlayer())
                .mapToInt(stack -> tag.contains(stack.getItem()) ? stack.getCount() : 0)
                .sum();
    }
}