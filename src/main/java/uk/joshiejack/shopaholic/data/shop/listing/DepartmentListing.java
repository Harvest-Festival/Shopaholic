package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class DepartmentListing extends SublistingBuilder {
    public DepartmentListing(String departmentID) {
        super("department", departmentID);
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
