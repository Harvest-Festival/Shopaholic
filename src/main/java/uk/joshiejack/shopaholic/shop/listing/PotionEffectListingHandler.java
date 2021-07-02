package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

public class PotionEffectListingHandler extends ListingHandler<EffectInstance> {
    @Override
    public String getType() {
        return "potion";
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public EffectInstance getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        Row row = database.table("potion_listings").fetch_where("id=" + data);
        Effect effect = row.effect();
        int duration = row.get("duration");
        int amplifier = row.get("amplifier");
        boolean ambient = row.get("is ambient");
        boolean particles = row.get("show particles");
        return new EffectInstance(effect, duration, amplifier, ambient, particles);
    }

    @Override
    public String getStringFromObject(EffectInstance instance) {
        return instance.getEffect().getRegistryName().toString() + " " + instance.getDuration() + " "
                + instance.getAmplifier() + " " + instance.isAmbient() + " " + instance.showIcon();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isValid(EffectInstance created) {
        return created.getEffect() != null;
    }

    @Override
    public String getValidityError() {
        return "Potion does not exist";
    }

    @Override
    public ITextComponent getDisplayName(EffectInstance resource) {
        return resource.getEffect().getDisplayName();
    }


    @Override
    public Icon createIcon(EffectInstance resource) {
        return new ItemIcon(new ItemStack(Items.POTION));
    }

    @Override
    public void purchase(PlayerEntity player, EffectInstance effect) {
        if (!player.level.isClientSide) {
            player.addEffect(new EffectInstance(effect)); //COPY!
        }
    }
}
