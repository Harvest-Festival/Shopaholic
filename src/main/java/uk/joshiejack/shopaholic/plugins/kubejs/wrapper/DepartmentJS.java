package uk.joshiejack.shopaholic.plugins.kubejs.wrapper;

import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

public class DepartmentJS {
    private final Department department;

    public DepartmentJS(Department department) {
        this.department = department;
    }

    public ShopJS shop() {
        return new ShopJS(department.getShop());
    }

    public ListingJS listing(String id) {
        Listing listing = department.getListingByID(id);
        return listing == null ? null : new ListingJS(listing);
    }

    public String id() {
        return department.id();
    }
}
