package uk.joshiejack.shopaholic.client;

import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.shopaholic.ShopaholicConfig;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID, value = Dist.CLIENT)
public class SellValueTooltip {
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (ShopaholicConfig.enableDebug && ShippingRegistry.INSTANCE.getValue(event.getItemStack()) > 0 && event.getFlags().isAdvanced())
            event.getToolTip().add(ITextComponent.nullToEmpty("Sell Value: " + ShippingRegistry.INSTANCE.getValue(event.getItemStack())));
    }
}