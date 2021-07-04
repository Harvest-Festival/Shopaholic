package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListingBuilder {
    public final String id;
    public String stockMechanic = "unlimited";
    public String costFormula = "default";
    public List<ConditionBuilder> conditions = new ArrayList<>();
    public List<SublistingBuilder<?>> sublistings = new ArrayList<>();
    public ShopaholicDatabase.StockMechanicBuilder stockMechanicBuilder;

    public ListingBuilder(String id) {
        this.id = id;
    }

    public static ListingBuilder of(String id) {
        return new ListingBuilder(id);
    }

    public ListingBuilder addSublisting(SublistingBuilder<?> sublisting) {
        if (sublistings.size() > 0) {
            if (sublistings.size() == 1)
                sublistings.get(0).id("item_1");
            sublisting.id("item_" + (sublistings.size() + 1));
        }

        sublistings.add(sublisting);
        return this;
    }

    public ListingBuilder stockMechanic(String stockMechanic) {
        this.stockMechanic = stockMechanic;
        return this;
    }

    public ListingBuilder costFormula(String costFormula) {
        this.costFormula = costFormula;
        return this;
    }

    public ListingBuilder condition(ConditionBuilder condition) {
        conditions.add(condition);
        return this;
    }

    public ListingBuilder stockMechanic(ShopaholicDatabase.StockMechanicBuilder stockMechanic) {
        this.stockMechanicBuilder = stockMechanic;
        this.stockMechanic = stockMechanic.id;
        return this;
    }
}
