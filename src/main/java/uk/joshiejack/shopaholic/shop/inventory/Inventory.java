package uk.joshiejack.shopaholic.shop.inventory;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.shopaholic.shop.Department;

import javax.annotation.Nonnull;
import java.util.Objects;

public class Inventory extends WorldSavedData {
    private static final String DATA_NAME = "Shop-Stock";

    public Inventory() {
        super(DATA_NAME);
    }

    public static void setChanged(ServerWorld world) {
        world.getDataStorage().computeIfAbsent(Inventory::new, DATA_NAME).setDirty();
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        ListNBT list = nbt.getList("Shops", 10);
        for (int i = 0; i < list.size(); i++) {
            CompoundNBT tag = list.getCompound(i);
            Department shop = Department.REGISTRY.get(tag.getString("Shop"));
            if (shop != null)
                shop.getStockLevels().deserializeNBT(tag.getCompound("Stock"));
        }
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        for (Department entry: Department.REGISTRY.values()) {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Shop", Objects.requireNonNull(entry.id()));
            tag.put("Stock", entry.getStockLevels().serializeNBT());
            list.add(tag);
        }

        nbt.put("Shops", list);
        return nbt;
    }
}
