package uk.joshiejack.shopaholic.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import uk.joshiejack.shopaholic.Shopaholic;

@SuppressWarnings("ConstantConditions")
public class ShopaholicBlockStates extends BlockStateProvider {
    public ShopaholicBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Shopaholic.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }
}
