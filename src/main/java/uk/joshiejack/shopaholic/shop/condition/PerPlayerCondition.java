package uk.joshiejack.shopaholic.shop.condition;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.ShopaholicClient;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

import javax.annotation.Nonnull;

import static uk.joshiejack.shopaholic.Shopaholic.MODID;

public class PerPlayerCondition implements Condition {
    private int max;

    public PerPlayerCondition() {}
    private PerPlayerCondition(int max) {
        this.max = max;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return type == CheckType.SHOP_LISTING;
    }

    @Override
    public Condition create(ShopLoadingData loadingData, Row data, String id) {
        return new PerPlayerCondition(data.get("max"));
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        if (type != CheckType.SHOP_LISTING) return false;
        else {
            CompoundNBT tag = target.getPlayer().getPersistentData().getCompound(MODID);
            String label = department.id() + ":" + listing.id();
            return tag.getInt(label) < max;
        }
    }

    @Override
    public void onPurchase(PlayerEntity player, @Nonnull Department department, @Nonnull Listing listing) {
        CompoundNBT tag = player.getPersistentData().getCompound(MODID);
        String label = department.id() + ":" + listing.id();
        tag.putInt(label, tag.getInt(label) + 1);
        player.getPersistentData().put(MODID, tag);
        if (player.level.isClientSide && tag.getInt(label) >= max) {
            ShopaholicClient.refreshShop();
        }
    }
}
