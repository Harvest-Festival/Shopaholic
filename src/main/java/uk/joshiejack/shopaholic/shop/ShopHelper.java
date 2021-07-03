package uk.joshiejack.shopaholic.shop;

import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.input.InputMethod;

import javax.annotation.Nullable;
import java.util.Collection;

public class ShopHelper {
    public static void resetGUI() {
        /*GuiScreen screen = Minecraft.getMinecraft().currentScreen;
        if (screen instanceof ShopScreen) {
            ((ShopScreen)screen).refresh();
        } */ //TODO
    }

    public static void open(Collection<Department> shops, ShopTarget target, InputMethod method) {
        Department shop = getFirstShop(shops, target, Condition.CheckType.SHOP_IS_OPEN, method);
        if (shop != null) {
            shop.open(target, true);
        }
    }

    @Nullable
    public static Department getFirstShop(Collection<Department> shops, ShopTarget target, Condition.CheckType type, InputMethod method) {
        for (Department shop: shops) {
            if (shop.isValidFor(target, type, method)) {
                return shop;
            }
        }

        return null;
    }
}
