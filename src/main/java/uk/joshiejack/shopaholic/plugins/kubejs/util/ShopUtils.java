package uk.joshiejack.shopaholic.plugins.kubejs.util;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerJS;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.plugins.kubejs.wrapper.DepartmentJS;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.ShopHelper;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;

import javax.annotation.Nonnull;
import java.util.Objects;

import static uk.joshiejack.shopaholic.shop.input.InputToShop.ENTITY_TO_SHOP;

public class ShopUtils {
    private static ShopTarget target(PlayerJS<?> playerW, EntityJS entityW) {
        Entity entity = entityW.minecraftEntity;
        PlayerEntity player = playerW.minecraftPlayer;
        return new ShopTarget(entity.level, entity.blockPosition(), entity, player, player.getMainHandItem(), new EntityShopInput(entity));
    }

    public static void open(PlayerJS<?> player, EntityJS entityJS) {
        ShopTarget target = target(player, entityJS);
        ShopHelper.open(ENTITY_TO_SHOP.get((EntityShopInput) target.getInput()), target, InputMethod.SCRIPT);
    }

    public static void open(PlayerJS<?> player, EntityJS entityJS, String id) {
        Department shop = Department.REGISTRY.get(id);
        if (shop != null) {
            shop.open(target(player, entityJS));
        }
    }

    public static boolean canOpen(PlayerJS<?> player, EntityJS entityJS, String id) {
        Department department = Department.REGISTRY.get(id);
        return department != null && department.isValidFor(target(player, entityJS), Condition.CheckType.SHOP_IS_OPEN);
    }

    public static boolean has(PlayerJS<?> player, EntityJS entityJS) {
        ShopTarget target = target(player, entityJS);
        return ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((EntityShopInput) target.getInput()), target, Condition.CheckType.SHOP_EXISTS, InputMethod.SCRIPT) != null;
    }

    public static boolean isOpen(PlayerJS<?> player, EntityJS entityJS) {
        ShopTarget target = target(player, entityJS);
        return ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((EntityShopInput) target.getInput()), target, Condition.CheckType.SHOP_IS_OPEN, InputMethod.SCRIPT) != null;
    }

    public static DepartmentJS get(PlayerJS<?> player, @Nonnull EntityJS entityJS) {
        ShopTarget target = target(player, entityJS);
        return new DepartmentJS(Objects.requireNonNull(ShopHelper.getFirstShop(ENTITY_TO_SHOP.get((EntityShopInput) target.getInput()), target, Condition.CheckType.SHOP_EXISTS, InputMethod.SCRIPT)));
    }

    public static DepartmentJS get(String name) {
        Department department = Department.REGISTRY.get(name);
        return department == null ? null : new DepartmentJS(department);
    }
}
