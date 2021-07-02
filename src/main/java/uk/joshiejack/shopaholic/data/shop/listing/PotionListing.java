package uk.joshiejack.shopaholic.data.shop.listing;

import net.minecraft.potion.EffectInstance;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class PotionListing extends SublistingBuilder {
    private final EffectInstance effect;

    public PotionListing(String id, EffectInstance effect) {
        super("potion", id);
        this.effect = effect;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("potion_listings", "ID,Duration,Amplifier,Is Ambient,Show Particles",
                CSVUtils.join(this.data, effect.getDuration(), effect.getAmplifier(), effect.isAmbient(), effect.showIcon()));
    }
}
