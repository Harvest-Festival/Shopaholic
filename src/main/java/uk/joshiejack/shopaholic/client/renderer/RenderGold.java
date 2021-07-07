package uk.joshiejack.shopaholic.client.renderer;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.shopaholic.client.ShopaholicClientConfig;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;

import java.text.NumberFormat;
import java.util.Locale;

import static uk.joshiejack.shopaholic.Shopaholic.MODID;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RenderGold {
    @SubscribeEvent
    public static void offsetPotion(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.getMatrixStack().pushPose();
            event.getMatrixStack().translate(0F, 16F, 0F);
        }
    }

    @SubscribeEvent
    public static void offsetPotion(RenderGameOverlayEvent.Post event) {
        if (event.getType() == RenderGameOverlayEvent.ElementType.POTION_ICONS) {
            event.getMatrixStack().popPose();
        }
    }

    @SubscribeEvent
    public static void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (ShopaholicClientConfig.enableGoldHUD.get() && event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack matrix = event.getMatrixStack();
            matrix.pushPose();
            RenderSystem.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int maxWidth = event.getWindow().getGuiScaledWidth();
            int maxHeight = event.getWindow().getGuiScaledHeight();
            String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(Wallet.getActive().getBalance());
            float adjustedX = ((ShopaholicClientConfig.goldHUDX.get() / 100F) * maxWidth);
            float adjustedY = ((ShopaholicClientConfig.goldHUDY.get() / 100F) * maxHeight);

            if (ShopaholicClientConfig.enableGoldIconHUD.get()) {
                mc.getTextureManager().bind(DepartmentScreen.EXTRA);
                int coinX = (int) (ShopaholicClientConfig.goldRenderSide.get() == ShopaholicClientConfig.GoldRenderSide.LEFT ? maxWidth - mc.font.width(text) - 20 + adjustedX : maxWidth - adjustedX - 14);
                mc.gui.blit(event.getMatrixStack(), coinX, (int) (2 + adjustedY), 244, 244, 12, 12);
            }

            int textX = (int) (ShopaholicClientConfig.goldRenderSide.get() == ShopaholicClientConfig.GoldRenderSide.LEFT ? maxWidth - mc.font.width(text) - 5 + (int) adjustedX : maxWidth - adjustedX - 18 - mc.font.width(text));
            mc.font.drawShadow(matrix, text, textX, 4 + adjustedY, 0xFFFFFFFF);

            RenderSystem.disableBlend();
            matrix.popPose();
        }
    }
}