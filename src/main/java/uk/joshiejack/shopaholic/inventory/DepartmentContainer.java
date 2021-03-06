package uk.joshiejack.shopaholic.inventory;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Shop;
import uk.joshiejack.shopaholic.shop.input.BlockShopInput;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;
import uk.joshiejack.shopaholic.shop.input.ItemShopInput;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class DepartmentContainer extends Container {
    public Shop shop;
    public Department department;
    public ShopTarget target;

    public DepartmentContainer(int windowID, PlayerEntity player, @Nullable PacketBuffer buf) {
        super(Shopaholic.ShopaholicContainers.SHOP.get(), windowID);
        if (buf != null) {
            boolean reloadLastDepartment = buf.readBoolean();
            Department d = Department.REGISTRY.get(buf.readUtf());
            BlockPos pos = BlockPos.of(buf.readVarLong());
            int entityID = buf.readVarInt();
            ItemStack stack = buf.readItem();
            byte type = buf.readByte();
            ShopInput<?> input = (type == 0 ? new BlockShopInput(buf) : type == 1 ? new EntityShopInput(buf) : new ItemShopInput(buf));
            target = new ShopTarget(player.level, pos, player.level.getEntity(entityID), player, stack, input);
            shop = Shop.get(d);
            department = shop != null && reloadLastDepartment && shop.getLast().isValidFor(target, Condition.CheckType.SHOP_LISTING) ? shop.getLast() : d;
            shop = Shop.get(department);
        }
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        return true;
    }

    public Container withData(Department department, ShopTarget target, boolean reloadLastDepartment) {
        this.target = target;
        this.shop = Shop.get(department);
        this.department = shop != null && reloadLastDepartment && shop.getLast().isValidFor(target, Condition.CheckType.SHOP_LISTING) ? shop.getLast() : department;
        this.shop = Shop.get(this.department);
        return this;
    }
}
