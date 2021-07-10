package uk.joshiejack.shopaholic.data.shop;

import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.condition.ConditionBuilder;
import uk.joshiejack.shopaholic.shop.input.InputMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ShopBuilder {
    protected final String id;
    protected final String name;
    protected Vendor vendor = Vendor.NONE;
    protected InputMethod input = InputMethod.RIGHT_CLICK;
    protected List<ConditionBuilder> conditions = new ArrayList<>();
    protected List<DepartmentBuilder> departments = new ArrayList<>();
    protected String bg = "default";
    protected String ex = "default";

    public ShopBuilder(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static ShopBuilder of(String id, String name) {
        return new ShopBuilder(id, name);
    }

    public ShopBuilder vendor(Vendor vendor) {
        this.vendor = vendor;
        return this;
    }

    public ShopBuilder openWith(InputMethod method) {
        this.input = method;
        return this;
    }

    public ShopBuilder department(DepartmentBuilder department) {
        this.departments.add(department);
        return this;
    }

    public ShopBuilder condition(ConditionBuilder condition) {
        this.conditions.add(condition);
        return this;
    }

    public ShopBuilder background(ResourceLocation background) {
        this.bg = background.toString();
        return this;
    }

    public ShopBuilder extra(ResourceLocation extra) {
        this.ex = extra.toString();
        return this;
    }

    public void save(ShopaholicDatabase data) {
        String prev = data.subfolder;
        data.subfolder = "shops";
        data.addEntry("shops", "ID,Name,Background Texture,Extra Texture,Vendor ID,Opening Method", CSVUtils.join(id, name, bg, ex, vendor.id, input.toString().toLowerCase(Locale.ROOT)));
        data.addEntry("vendors", "ID,Type,Data", CSVUtils.join(vendor.id, vendor.type, vendor.data));
        conditions.forEach(condition -> {
            data.addEntry("shop_conditions", "Shop ID,Condition ID", CSVUtils.join(id, condition.id));
            String previous = data.subfolder;
            data.subfolder = "shops/conditions";
            condition.save(data);
            data.subfolder = previous;
        });

        departments.forEach(department -> {
            data.addEntry("departments", "Shop ID,ID,Icon,Name", CSVUtils.join(id, department.id, department.icon, department.name));
            department.conditions.forEach(condition -> {
                data.addEntry("department_conditions", "Shop ID,Department ID,Condition ID", CSVUtils.join(id, department.id, condition.id));
                String previous = data.subfolder;
                data.subfolder = "shops/conditions";
                condition.save(data);
                data.subfolder = previous;
            });

            String previous = data.subfolder;
            data.subfolder = "shops/" + id;
            department.save(data);
            data.subfolder = previous;
        });

        data.subfolder = prev;
    }
}
