package uk.joshiejack.shopaholic.api.shop;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Set;

public abstract class Condition {
    private static final Map<String, Condition> REGISTRY = Maps.newHashMap();

    public static void register(String name, Condition condition) {
        REGISTRY.put(name, condition);
    }

    public static Condition get(String string) {
        return REGISTRY.get(string);
    }

    public void onPurchase(PlayerEntity player, @Nonnull Department department, @Nonnull Listing listing) {}

    public boolean valid(@Nonnull ShopTarget target, @Nonnull Department department, @Nonnull Listing listing, @Nonnull CheckType type) {
        return valid(target, type);
    }

    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        return true;
    }

    public abstract Condition create(Row database, String id);

    public void merge(Row data) {}

    public static Set<String> types() {
        return REGISTRY.keySet();
    }

    public enum CheckType {
        SHOP_EXISTS, SHOP_IS_OPEN, SHOP_LISTING
    }
}
