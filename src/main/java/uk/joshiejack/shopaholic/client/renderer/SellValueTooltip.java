package uk.joshiejack.shopaholic.client.renderer;

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.client.ShopaholicClientConfig;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Shopaholic.MODID, value = Dist.CLIENT)
public class SellValueTooltip {
    @SubscribeEvent
    public static void tooltip(ItemTooltipEvent event) {
        if (ShopaholicClientConfig.enableSellValueTooltip.get() && ShippingRegistry.getValue(event.getItemStack()) > 0 && event.getFlags().isAdvanced())
            event.getToolTip().add(ITextComponent.nullToEmpty("Sell Value: " + TextFormatting.GOLD + StringHelper.convertNumberToString(ShippingRegistry.getValue(event.getItemStack())) + " Gold"));
    }
}