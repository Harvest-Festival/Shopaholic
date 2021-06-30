package uk.joshiejack.shopaholic.shop.comparator;

import com.google.common.collect.Streams;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class ItemTagInInventoryComparator extends Comparator {
    private ITag.INamedTag<Item> tag;

    @Override
    public Comparator create(Row data, String id) {
        ItemTagInInventoryComparator comparator = new ItemTagInInventoryComparator();
        comparator.tag = data.itemTag();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return Streams.concat(target.player.inventory.items.stream(), target.player.inventory.armor.stream(), target.player.inventory.offhand.stream())
                .mapToInt(stack -> tag.contains(stack.getItem()) ? stack.getCount() : 0).sum();
    }
}