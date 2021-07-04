package uk.joshiejack.shopaholic.plugins.kubejs.wrapper;

import uk.joshiejack.shopaholic.shop.Listing;

public class ListingJS {
    private final Listing listing;

    public ListingJS(Listing listing) {
        this.listing = listing;
    }

    public SublistingJS<?> sublisting(String id) {
        return new SublistingJS<>(listing.getSublistingByID(id));
    }

    public String department() {
        return listing.getDepartment().id();
    }

    public String id() {
        return listing.id();
    }
}
