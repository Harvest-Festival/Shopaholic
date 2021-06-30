package uk.joshiejack.shopaholic.client;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.joshiejack.shopaholic.Shopaholic;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Shopaholic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ShopaholicClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {

    }
}
