package uk.joshiejack.shopaholic.shop.inventory;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.shopaholic.shop.Department;

import javax.annotation.Nonnull;
import java.util.Map;

public class Inventory extends WorldSavedData {
    private static final String DATA_NAME = "stock_levels";
    private final Map<Department, Stock> stock = new Object2ObjectOpenHashMap<>();

    public Inventory() {
        super(DATA_NAME);
    }

    private static Inventory getInstance(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(Inventory::new, DATA_NAME);
    }

    public static Stock getStock(ServerWorld world, Department department) {
        return getInstance(world).getStock(department);
    }

    private Stock getStock(Department department) {
        return stock.computeIfAbsent(department, Stock::new);
    }

    public static void setChanged(ServerWorld world) {
        getInstance(world).setDirty();
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        ListNBT list = nbt.getList("Departments", 10);
        list.forEach(inbt -> {
            CompoundNBT tag = (CompoundNBT) inbt;
            Department department = Department.REGISTRY.get(tag.getString("Department"));
            if (department != null)
                getStock(department).deserializeNBT(tag.getCompound("Stock"));
        });
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        Department.REGISTRY.values().forEach(department -> {
            CompoundNBT tag = new CompoundNBT();
            tag.putString("Department", department.id());
            tag.put("Stock", getStock(department).serializeNBT());
            list.add(tag);
        });

        nbt.put("Departments", list);
        return nbt;
    }
}
