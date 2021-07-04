package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;
import java.util.Map;

public class Shop {
    private static final Map<Department, Shop> DEPARTMENT_TO_SHOP = Maps.newHashMap();
    private final List<Department> departments = Lists.newArrayList();
    private final String id;
    private final ITextComponent name;
    private Department last;

    public Shop(String shop_id, String name) {
        this.id = shop_id;
        this.name = new TranslationTextComponent(name);
    }

    public String id() {
        return id;
    }

    public ITextComponent getLocalizedName() {
        return name;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public static Shop get(Department shop) {
        Shop market = shop.getShop() != null ? shop.getShop() : DEPARTMENT_TO_SHOP.get(shop);
        if (market != null && market.last == null) {
            market.last = shop;
        }

        return market;
    }

    public Department getLast() {
        return last;
    }

    public void setLast(Department last) {
        this.last = last;
    }
}
