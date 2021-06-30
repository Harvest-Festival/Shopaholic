package uk.joshiejack.shopaholic.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.gold.WalletType;
import uk.joshiejack.shopaholic.client.Wallet;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class EconomyStatsLabel extends Widget {
    private static final int fontColor1 = 4210752;
    private static final int fontColor2 = 0x3F3F3F;
    private boolean isSharedWalletActive;
    private Wallet personal, shared;
    public EconomyStatsLabel(int x, int y, ITextComponent name) {
        super(x, y, 120, 240, name);
        personal = Wallet.getWallet(WalletType.PERSONAL);
        shared = Minecraft.getInstance().player.getUUID().equals(PenguinTeams.getTeamForPlayer(Minecraft.getInstance().player).getID()) ? null :
                Wallet.getWallet(WalletType.SHARED);
        isSharedWalletActive = Wallet.getActive() == shared;
    }

    private static String formatGold(long value) {
        return NumberFormat.getNumberInstance(Locale.ENGLISH).format(value);
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        //GlStateManager.disableDepth();
        StringHelper.enableUnicode();
        //Draw your personal balance
        mc.font.drawShadow(matrix, TextFormatting.BOLD + "Personal account", x, y, fontColor1);
        if (shared != null) {
            mc.font.drawShadow(matrix, TextFormatting.BOLD + "Shared account", x, y + 100, fontColor1);
            /**drawHorizontalLine(x - 6, x + 115, y + 46, 0xFFB0A483);
             drawHorizontalLine(x - 5, x + 116, y + 46 + 1, 0xFF9C8C63);
             drawHorizontalLine(x - 6, x + 115, y + 96, 0xFFB0A483);
             drawHorizontalLine(x - 5, x + 116, y + 96 + 1, 0xFF9C8C63);**/
            StringHelper.disableUnicode();
            mc.font.draw(matrix, TextFormatting.BOLD + "^", x + 55, y + 64, fontColor1);
            int length = mc.font.width("Transfer");
            mc.font.drawShadow(matrix, "Transfer", x + 60 - length / 2, y + 67, 0xFFFFFF);
            mc.font.draw(matrix, TextFormatting.BOLD + "v", x + 55, y + 74, fontColor1);
        } else StringHelper.disableUnicode();

        //GlStateManager.pushMatrix();
        matrix.pushPose();
        float scale = 0.6666666666F;
        matrix.scale(scale, scale, scale);
        //GlStateManager.scale(scale, scale, scale);
        ////Personal Account
        //Expenses
        mc.font.drawShadow(matrix, "Expenses", (x) / scale, (y + 10) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getExpenses()), (x + 45) / scale, (y + 10) / scale, 0xFFFFFFFF);
        //Income
        mc.font.drawShadow(matrix, "Income", (x) / scale, (y + 19) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getIncome()), (x + 45) / scale, (y + 19) / scale, 0xFFFFFFFF);
        //Profit
        mc.font.drawShadow(matrix, "Profit", (x) / scale, (y + 28) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getIncome() - personal.getExpenses()), (x + 45) / scale, (y + 28) / scale, 0xFFFFFFFF);
        //Balance
        mc.font.drawShadow(matrix, "Balance", (x) / scale, (y + 37) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getBalance()), (x + 45) / scale, (y + 37) / scale, 0xFFFFFFFF);

        if (shared != null) {
            ////Shared Account
            //Expenses
            mc.font.drawShadow(matrix, "Expenses", (x) / scale, (y + 110) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getExpenses()), (x + 45) / scale, (y + 110) / scale, 0xFFFFFFFF);
            //Income
            mc.font.drawShadow(matrix, "Income", (x) / scale, (y + 119) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getIncome()), (x + 45) / scale, (y + 119) / scale, 0xFFFFFFFF);
            //Profit
            mc.font.drawShadow(matrix, "Profit", (x) / scale, (y + 128) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getIncome() - shared.getExpenses()), (x + 45) / scale, (y + 128) / scale, 0xFFFFFFFF);
            //Balance
            mc.font.drawShadow(matrix, "Balance", (x) / scale, (y + 137) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getBalance()), (x + 45) / scale, (y + 137) / scale, 0xFFFFFFFF);
        }

        RenderSystem.color4f(1F, 1F, 1F, 1F);
        //GlStateManager.color(1F, 1F, 1F, 1F);
        mc.getTextureManager().bind(ShopScreen.EXTRA);
        blit(matrix, (int)((x + 35) / scale), (int) ((y + 9)/scale), 244, 244, 12, 12);
        blit(matrix, (int)((x + 35) / scale), (int)((y + 18)/scale), 244, 244, 12, 12);
        blit(matrix, (int)((x + 35) / scale), (int)((y + 27)/scale), 244, 244, 12, 12);
        blit(matrix, (int)((x + 35) / scale), (int)((y + 36)/scale), 244, 244, 12, 12);
        if (shared != null) {
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 109) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 118) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 127) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 136) / scale), 244, 244, 12, 12);
        }

        matrix.popPose();
        //Draw the shared balance

        //Ignroe below?
        //mc.fontRenderer.drawString("Shared account", x, y + 20, gui.fontColor1);
        //GlStateManager.color(1F, 1F, 1F, 1F);
        //mc.getTextureManager().bindTexture(GOLD);
        //drawTexturedModalRect(x, y + 30, 244, 244, 12, 12);
        //mc.fontRenderer.drawShadow(matrix, text2, x + 14, y + 32, 0xFFFFFFFF);

        //StringHelper.disableUnicode();
        //mc.fontRenderer.setUnicodeFlag(flag);

        //GlStateManager.enableDepth();
    }
}

