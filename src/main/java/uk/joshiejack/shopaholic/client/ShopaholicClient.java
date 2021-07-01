package uk.joshiejack.shopaholic.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import uk.joshiejack.penguinlib.client.gui.HUDRenderer;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.tab.Tab;
import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.client.gui.page.PageEconomyManager;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = Shopaholic.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ShopaholicClient {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        ScreenManager.register(Shopaholic.ShopaholicContainers.BOOK.get(),
                ((AbstractBookContainer container, PlayerInventory inv, ITextComponent text) ->
                        Book.getInstance(Shopaholic.MODID, container, inv, text, (Book bs) -> {
                            ITextComponent manager = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager");
                            bs.withTab(new Tab(manager, PageEconomyManager.ICON)).withPage(new PageEconomyManager(manager));
                            //Setup colours
                            bs.fontColor1 = 4210752;
                            bs.fontColor2 = 0x3F3F3F;
                            bs.lineColor1 = 0xFF515557;
                            bs.lineColor2 = 0xFF7F8589;
                        })
                ));

        if (ShopaholicClientConfig.enableClockHUD.get()) {
            HUDRenderer.RENDERERS.put(World.OVERWORLD, new HUDRenderer.HUDRenderData() {
                @Override
                public ITextComponent getHeader(Minecraft mc) {
                    return  new TranslationTextComponent("Day %s", 1 + TimeHelper.getElapsedDays(mc.level.getDayTime()));
                }
            });
        }
    }
}
