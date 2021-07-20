package uk.joshiejack.shopaholic.data.shop.comparator;

import net.minecraft.block.Block;
import net.minecraft.tags.ITag;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class BlockTagOnTargetComparatorBuilder extends ComparatorBuilder {
    private final ITag.INamedTag<Block> tag;

    @SuppressWarnings("unused")
    protected BlockTagOnTargetComparatorBuilder(String id, ITag.INamedTag<Block> tag) {
        super(id);
        this.tag = tag;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("comparator_block_tag", "ID,Tag", CSVUtils.join(id, tag.getName().toString()));
    }
}
