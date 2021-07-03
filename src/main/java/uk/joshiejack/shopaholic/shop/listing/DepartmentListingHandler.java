package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;

@SuppressWarnings("ConstantConditions")
public class DepartmentListingHandler extends ListingHandler<String> {
    @Override
    public String getType() {
        return "department";
    }

    @Override
    public String getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return data;
    }

    @Override
    public ITextComponent getDisplayName(String shop) {
        return Department.REGISTRY.get(shop).getLocalizedName();
    }

    @Override
    public Icon createIcon(String shop) {
        return Department.REGISTRY.get(shop).getIcon();
    }

    @Override
    public void purchase(PlayerEntity player, String id) {
        if (!player.level.isClientSide) {
            Department shop = Department.REGISTRY.get(id);
            ShopTarget target = new ShopTarget(player.level, player.blockPosition(), player, player, player.getMainHandItem(), new EntityShopInput(player));
            if (shop != null) {
                shop.open(target, false);
            }
        }
    }

    @Override
    public String getStringFromObject(String shop) {
        return shop.toString();
    }

    @Override
    public boolean isValid(String shop) {
        return shop != null && !shop.isEmpty();
    }

    @Override
    public String getValidityError() {
        return "Shop does not exist";
    }
}

