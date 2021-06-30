package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class BlockTagOnTargetComparator extends Comparator {
    private ITag.INamedTag<Block> tag;

    @Override
    public Comparator create(Row data, String id) {
        BlockTagOnTargetComparator comparator = new BlockTagOnTargetComparator();
        comparator.tag = data.blockTag();
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return tag.contains(target.world.getBlockState(target.pos).getBlock()) ? 1 : 0;
    }
}