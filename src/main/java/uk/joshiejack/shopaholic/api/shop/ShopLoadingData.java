package uk.joshiejack.shopaholic.api.shop;

import com.google.common.collect.Maps;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.Map;

//Temporary Registries
public class ShopLoadingData {
    public final Map<String, Comparator> comparators = Maps.newHashMap();
    public final Map<String, Condition> conditions = Maps.newHashMap();
    public final Map<String, StockMechanic> stock_mechanics = Maps.newHashMap();
}