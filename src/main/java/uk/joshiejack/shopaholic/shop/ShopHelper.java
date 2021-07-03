package uk.joshiejack.shopaholic.shop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.shop.input.InputMethod;

import javax.annotation.Nullable;
import java.util.Collection;

public class ShopHelper {
    @OnlyIn(Dist.CLIENT)
    public static void refreshShop() {
        Screen screen = Minecraft.getInstance().screen;
        if (screen instanceof DepartmentScreen) {
            ((DepartmentScreen)screen).refresh();
        }
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
