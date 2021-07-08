package uk.joshiejack.shopaholic.plugins;

import dev.latvian.kubejs.script.ScriptType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.interfaces.IModPlugin;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.ShopaholicAPI;
import uk.joshiejack.shopaholic.event.ItemGetValueEvent;
import uk.joshiejack.shopaholic.event.ItemPurchasedEvent;
import uk.joshiejack.shopaholic.event.ItemShippedEvent;
import uk.joshiejack.shopaholic.plugins.kubejs.shop.KubeJSScriptComparator;
import uk.joshiejack.shopaholic.plugins.kubejs.shop.KubeJSScriptCondition;
import uk.joshiejack.shopaholic.plugins.kubejs.event.ItemGetValueEventJS;
import uk.joshiejack.shopaholic.plugins.kubejs.event.ItemPurchasedEventJS;
import uk.joshiejack.shopaholic.plugins.kubejs.event.ItemShippedEventJS;

@PenguinLoader("kubejs")
public class KubeJSPlugin implements IModPlugin {
    private static final String PURCHASED_ITEM_ID = Shopaholic.MODID + ".item.purchased";
    private static final String ITEM_SHIPPED_ID = Shopaholic.MODID + ".item.shipped";
    private static final String GET_ITEM_VALUE_ID = Shopaholic.MODID + ".item.get_value";

    @Override
    public void setup() {
        MinecraftForge.EVENT_BUS.register(KubeJSPlugin.class);
        ShopaholicAPI.registry.registerComparator("kubejs", new KubeJSScriptComparator());
        ShopaholicAPI.registry.registerCondition("kubejs", new KubeJSScriptCondition());
    }

    @SubscribeEvent
    public static void onItemPurchased(ItemPurchasedEvent event) {
        new ItemPurchasedEventJS(event).post(PURCHASED_ITEM_ID);
    }

    @SubscribeEvent
    public static void onItemShipped(ItemShippedEvent event) {
        new ItemShippedEventJS(event).post(ITEM_SHIPPED_ID);
    }

    @SubscribeEvent
    public static void getItemValueEvent(ItemGetValueEvent event) {
        ItemGetValueEventJS e = new ItemGetValueEventJS(event);
        e.post(FMLEnvironment.dist == Dist.CLIENT ? ScriptType.CLIENT : ScriptType.SERVER, GET_ITEM_VALUE_ID);
        event.setNewValue(e.getNewValue());
    }
}
