package uk.joshiejack.shopaholic.plugins.kubejs;

import dev.latvian.kubejs.script.BindingsEvent;
import dev.latvian.kubejs.script.ScriptType;
import dev.latvian.kubejs.util.ClassFilter;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.plugins.kubejs.util.ShippingUtils;
import uk.joshiejack.shopaholic.plugins.kubejs.util.ShopUtils;

public class KubeJSPlugin extends dev.latvian.kubejs.KubeJSPlugin {
    @Override
    public void addClasses(ScriptType type, ClassFilter filter) {
        filter.allow("uk.joshiejack.shopaholic.plugins.kubejs");
        filter.allow(Condition.CheckType.class);
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("shops", ShopUtils.class);
        event.add("shipping", ShippingUtils.class);
    }
}
