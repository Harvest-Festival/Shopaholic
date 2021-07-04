package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import java.util.Objects;

public class PotionEffectListingHandler implements ListingHandler<EffectInstance> {
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

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean isValid(Object object) {
        return object instanceof EffectInstance && ((EffectInstance)object).getEffect() != null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(EffectInstance object) {
        return object.getEffect().getDisplayName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(EffectInstance object) {
        return new ItemIcon(new ItemStack(Items.POTION));
    }

    @Override
    public void purchase(PlayerEntity player, EffectInstance object) {
        if (player.hasEffect(object.getEffect()))
            ObfuscationReflectionHelper.setPrivateValue(EffectInstance.class,
                    Objects.requireNonNull(player.getEffect(object.getEffect())),
                    Objects.requireNonNull(player.getEffect(object.getEffect())).getDuration() + object.getDuration(), "field_76460_b");
        player.addEffect(new EffectInstance(object)); //COPY!
    }
}