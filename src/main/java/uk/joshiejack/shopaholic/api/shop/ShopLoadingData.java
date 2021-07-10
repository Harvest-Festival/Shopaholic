package uk.joshiejack.shopaholic.api.shop;

import com.google.common.collect.Maps;
import uk.joshiejack.shopaholic.shop.inventory.StockMechanic;

import java.util.Map;

//Temporary Registries
public class ShopLoadingData {
    public Map<String, Comparator> comparators = Maps.newHashMap();
    public Map<String, Condition> conditions = Maps.newHashMap();
    public Map<String, StockMechanic> stock_mechanics = Maps.newHashMap();
}