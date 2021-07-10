package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.TextureIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.client.ShopaholicClient;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;

//Doesn't do anything, just displays the gold as the listing type, for selling item purposes
public class GoldListingHandler implements ListingHandler<Long> {
    @Override
    public Long getObjectFromDatabase(ShopLoadingData shopLoadingData, DatabaseLoadedEvent database, String data) {
        return 0L;
    }

    @Override
    public boolean isValid(Object object) {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(Long object) {
        return ShopaholicClient.EMPTY_STRING;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(Long object) {
        return new TextureIcon(DepartmentScreen.EXTRA, 240, 224);
    }

    @Override
    public int getCount(Long stack) {
        return 1;
    }

    @Override
    public void purchase(PlayerEntity player, Long object) {}
}
