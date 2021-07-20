package uk.joshiejack.shopaholic.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.item.BlockItem;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fml.RegistryObject;
import uk.joshiejack.shopaholic.Shopaholic;

import java.util.Objects;

public class ShopaholicItemModels extends ItemModelProvider {
    public ShopaholicItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Shopaholic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        Shopaholic.ShopaholicItems.ITEMS.getEntries().stream()
                .map(RegistryObject::get)
                .forEach(item -> {
                    String path = Objects.requireNonNull(item.getRegistryName()).getPath();
                    if (item instanceof BlockItem)
                        getBuilder(path).parent(new ModelFile.UncheckedModelFile(modLoc("block/" + path)));
                    else
                        singleTexture(path, mcLoc("item/generated"), "layer0", modLoc("item/" + path));
                });
    }
}
