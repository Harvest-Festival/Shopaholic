package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

public class HealListingHandler implements ListingHandler<Float> {
    private static final ITextComponent NAME = new TranslationTextComponent("gui.shopaholic.shop.heal");
    @Override
    public Float getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return Float.valueOf(data);
    }

    @Override
    public boolean isValid(Float object) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(Float object) {
        return NAME;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(Float object) {
        return new ItemIcon(new ItemStack(Items.POTION)); //TODO: TEXTURE ICON
    }

    @Override
    public void purchase(PlayerEntity player, Float amount) {
        if (!player.level.isClientSide)
            player.heal(amount);
    }
}