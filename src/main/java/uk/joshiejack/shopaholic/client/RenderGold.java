package uk.joshiejack.shopaholic.client;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.shopaholic.EconomyConfig;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;

import java.text.NumberFormat;
import java.util.Locale;

import static uk.joshiejack.shopaholic.Shopaholic.MODID;

@SuppressWarnings("unused")
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
        if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
            Minecraft mc = Minecraft.getInstance();
            MatrixStack matrix = event.getMatrixStack();
            matrix.pushPose();
            RenderSystem.enableBlend();
            //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int maxWidth = event.getWindow().getGuiScaledWidth();
            int maxHeight = event.getWindow().getGuiScaledHeight();
            if (EconomyConfig.enableHUD) {
                String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(Wallet.getActive().getBalance());
                float adjustedX = ((EconomyConfig.goldX / 100F) * maxWidth);
                float adjustedY = ((EconomyConfig.goldY / 100F) * maxHeight);

                if (EconomyConfig.enableGoldIcon) {
                    mc.getTextureManager().bind(ShopScreen.EXTRA);
                    int coinX = (int) (EconomyConfig.goldLeft ? maxWidth - mc.font.width(text) - 20 + adjustedX : maxWidth - adjustedX - 14);
                    mc.gui.blit(event.getMatrixStack(), coinX, (int) (2 + adjustedY), 244, 244, 12, 12);
                }

                int textX = (int)(EconomyConfig.goldLeft ? maxWidth - mc.font.width(text) - 5 + (int) adjustedX : maxWidth - adjustedX - 18 - mc.font.width(text));
                mc.font.drawShadow(matrix, text, textX, 4 + adjustedY, 0xFFFFFFFF);
            }

            RenderSystem.disableBlend();
            matrix.popPose();
        }
    }
}
