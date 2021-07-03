package uk.joshiejack.shopaholic.client;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

import java.util.Map;

@OnlyIn(Dist.CLIENT)
public class ClientStockLevels {
    private static final Map<Department, Stock> stock = new Object2ObjectOpenHashMap<>();

    public static Stock getStock(Department department) {
        return stock.computeIfAbsent(department, Stock::new);
    }
}