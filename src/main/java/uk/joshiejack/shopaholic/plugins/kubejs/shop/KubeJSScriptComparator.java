package uk.joshiejack.shopaholic.plugins.kubejs.shop;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerEventJS;
import dev.latvian.kubejs.script.ScriptType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.plugins.kubejs.wrapper.ShopTargetWrapper;

import javax.annotation.Nonnull;

public class KubeJSScriptComparator implements Comparator {
    private String id;

    @Override
    public Comparator create(Row row, String id) {
        KubeJSScriptComparator script = new KubeJSScriptComparator();
        script.id = id;
        return script;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        KubeJSComparatorGetEvent e = new KubeJSComparatorGetEvent(target);
        e.post(FMLEnvironment.dist == Dist.CLIENT ? ScriptType.CLIENT : ScriptType.SERVER, Shopaholic.MODID + ".comparator." + id);
        return e.getValue();
    }

    public static class KubeJSComparatorGetEvent extends PlayerEventJS {
        private final ShopTargetWrapper target;
        private int value = 0;

        public KubeJSComparatorGetEvent(ShopTarget target) {
            this.target = new ShopTargetWrapper(target);
        }

        public ShopTargetWrapper getTarget() {
            return target;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        @Override
        public EntityJS getEntity() {
            return target.getPlayer();
        }
    }
}