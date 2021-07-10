package uk.joshiejack.shopaholic.plugins.kubejs.shop;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerEventJS;
import dev.latvian.kubejs.script.ScriptType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.plugins.kubejs.wrapper.ShopTargetWrapper;

import javax.annotation.Nonnull;

public class KubeJSScriptCondition implements Condition {
    private String id;

    @Override
    public Condition create(ShopLoadingData data, Row row, String id) {
        KubeJSScriptCondition script = new KubeJSScriptCondition();
        script.id = id;
        return script;
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        KubeJSConditionValidateEventJS e = new KubeJSConditionValidateEventJS(target, type);
        e.post(FMLEnvironment.dist == Dist.CLIENT ? ScriptType.CLIENT : ScriptType.SERVER, Shopaholic.MODID + ".condition." + id);
        return e.isValid();
    }

    public static class KubeJSConditionValidateEventJS extends PlayerEventJS {
        private final ShopTargetWrapper target;
        private final CheckType type;
        private boolean valid = false;

        public KubeJSConditionValidateEventJS(ShopTarget target, CheckType type) {
            this.target = new ShopTargetWrapper(target);
            this.type = type;
        }

        public ShopTargetWrapper getTarget() {
            return target;
        }

        public CheckType getType() {
            return type;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid() {
            valid = true;
        }

        @Override
        public EntityJS getEntity() {
            return target.getPlayer();
        }
    }
}