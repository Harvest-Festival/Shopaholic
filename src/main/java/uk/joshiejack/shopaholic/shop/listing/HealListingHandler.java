package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.TextureIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;

public class HealListingHandler implements ListingHandler<Float> {
    private static final ITextComponent NAME = new TranslationTextComponent("gui.shopaholic.shop.heal");
    @Override
    public Float getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return Float.valueOf(data);
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof Float;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(Float object) {
        return NAME;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(Float object) {
        return new TextureIcon(DepartmentScreen.EXTRA, 192, 240);
    }

    @Override
    public void purchase(PlayerEntity player, Float amount) {
        if (!player.level.isClientSide)
            player.heal(amount);
    }
}