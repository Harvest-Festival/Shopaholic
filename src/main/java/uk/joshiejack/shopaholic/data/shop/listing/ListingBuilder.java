package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.shop.condition.ConditionBuilder;

import java.util.ArrayList;
import java.util.List;

public class ListingBuilder {
    public final String id;
    public String stockMechanic = "unlimited";
    public String costFormula = "default";
    public List<ConditionBuilder> conditions = new ArrayList<>();
    public List<SublistingBuilder> sublistings = new ArrayList<>();

    public ListingBuilder(String id) {
        this.id = id;
    }

    public static ListingBuilder of(String id) {
        return new ListingBuilder(id);
    }

    public ListingBuilder addSublisting(SublistingBuilder sublisting) {
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
}
