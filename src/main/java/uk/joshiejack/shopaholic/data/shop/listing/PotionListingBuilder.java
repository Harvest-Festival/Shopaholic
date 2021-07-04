package uk.joshiejack.shopaholic.data.shop.listing;

import net.minecraft.potion.EffectInstance;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class PotionListingBuilder extends SublistingBuilder<PotionListingBuilder> {
    private final EffectInstance effect;

    public PotionListingBuilder(String id, EffectInstance effect) {
        super("potion", id);
        this.effect = effect;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("potion_listings", "ID,Effect,Duration,Amplifier,Is Ambient,Show Particles",
                CSVUtils.join(this.data, effect.getEffect().getRegistryName().toString(), effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.showIcon()));
    }
}
