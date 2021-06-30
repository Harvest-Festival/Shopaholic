package uk.joshiejack.shopaholic.data;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.text.WordUtils;
import uk.joshiejack.shopaholic.Shopaholic;

public class ShopaholicLanguage extends LanguageProvider {
    public ShopaholicLanguage(DataGenerator gen) {
        super(gen, Shopaholic.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("gui.shopaholic.manager", "Economy Manager");
        add("gui.shopaholic.manager.from.shared", "Click to transfer from the shared wallet to your personal wallet.");
        add("gui.shopaholic.manager.from.personal", "Click to transfer from your personal wallet to the shared wallet.");
        add("gui.shopaholic.manager.x.10", "Hold down %sSHIFT %sfor 10x");
        add("gui.shopaholic.manager.x.100", "Hold down %sCTRL %sfor 100x");
        add("gui.shopaholic.manager.x.1000", "Hold down %sALT %sfor 1000x");

        ForgeRegistries.ITEMS.getValues()
                .stream().filter(i -> i.getRegistryName().getNamespace().equals(Shopaholic.MODID))
                .forEach(item -> add(item, WordUtils.capitalizeFully(item.getRegistryName().getPath().replace("_", " "))));
    }
}
