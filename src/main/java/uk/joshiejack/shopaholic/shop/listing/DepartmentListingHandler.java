package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;

public class DepartmentListingHandler implements ListingHandler<String> {
    @Override
    public String getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return data;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(String object) {
        return Department.REGISTRY.get(object).getLocalizedName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(String object) {
        return Department.REGISTRY.get(object).getIcon();
    }

    @Override
    public void purchase(PlayerEntity player, String object) {
        if (!player.level.isClientSide) {
            Department shop = Department.REGISTRY.get(object);
            ShopTarget target = new ShopTarget(player.level, player.blockPosition(), player, player, player.getMainHandItem(), new EntityShopInput(player));
            if (shop != null) {
                shop.open(target, false);
            }
        }
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof String && !((String)object).isEmpty();
    }
}

