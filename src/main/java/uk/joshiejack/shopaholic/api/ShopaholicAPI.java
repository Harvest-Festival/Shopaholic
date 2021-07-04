package uk.joshiejack.shopaholic.api;

import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.CostFormula;

public class ShopaholicAPI {
    public static IShopaholicAPI instance = null;

    public interface IShopaholicAPI {
        /**
         * Register a new cost formula
         * @param name          the name to register is as, used in the database
         * @param formula       the formula object
         * **/
        void registerCostFormula(String name, CostFormula formula);

        /**
         * Register a new comparator type
         * @param name          the name to register is as, used in the database
         * @param comparator    a default instance of the comparator
         * **/
        void registerComparator(String name, Comparator comparator);
    }
}
