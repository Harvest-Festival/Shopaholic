package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class DepartmentListingBuilder extends SublistingBuilder<DepartmentListingBuilder> {
    public DepartmentListingBuilder(String departmentID) {
        super("department", departmentID);
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
