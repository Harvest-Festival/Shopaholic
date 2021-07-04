package uk.joshiejack.shopaholic.shop;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.CostFormula;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import java.util.Map;

public class ShopRegistries {
    public static final Map<String, Comparator> COMPARATORS = new Object2ObjectOpenHashMap<>();
    public static final Map<String, Condition> CONDITIONS = new Object2ObjectOpenHashMap<>();
    public static final Map<String, CostFormula> COST_FORMULAE = new Object2ObjectOpenHashMap<>();
    public static final Map<String, ListingHandler<?>> LISTING_HANDLERS = new Object2ObjectOpenHashMap<>();
}
