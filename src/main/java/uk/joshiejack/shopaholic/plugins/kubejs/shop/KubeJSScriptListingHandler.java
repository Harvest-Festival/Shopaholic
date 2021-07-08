package uk.joshiejack.shopaholic.plugins.kubejs.shop;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.player.PlayerEventJS;
import dev.latvian.kubejs.script.ScriptType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.loading.FMLEnvironment;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

public class KubeJSScriptListingHandler implements ListingHandler<String> {
    private static final Lazy<Icon> ICON = Lazy.of(() -> new ItemIcon(Items.COMMAND_BLOCK));

    @Override
    public String getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return data;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(String scriptID) {
        return new StringTextComponent(scriptID);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(String scriptID) {
        return ICON.get();
    }

    @Override
    public void purchase(PlayerEntity player, String command) {
        KubeJSScriptListingHandlerEventJS e = new KubeJSScriptListingHandlerEventJS(player);
        e.post(FMLEnvironment.dist == Dist.CLIENT ? ScriptType.CLIENT : ScriptType.SERVER, Shopaholic.MODID + ".listing." + command);
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof String && !((String)object).isEmpty();
    }

    public static class KubeJSScriptListingHandlerEventJS extends PlayerEventJS {
        private final PlayerEntity player;

        public KubeJSScriptListingHandlerEventJS(PlayerEntity player) {
            this.player = player;
        }

        @Override
        public EntityJS getEntity() {
            return entityOf(player);
        }
    }
}

