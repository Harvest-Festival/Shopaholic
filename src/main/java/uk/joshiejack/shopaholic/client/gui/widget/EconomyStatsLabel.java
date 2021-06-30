package uk.joshiejack.shopaholic.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.gold.WalletType;
import uk.joshiejack.shopaholic.client.Wallet;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class EconomyStatsLabel extends Widget {
    private static final ITextComponent TRANSFER = new TranslationTextComponent("gui.shopaholic.manager.transfer");
    private static final ITextComponent EXPENSES = new TranslationTextComponent("gui.shopaholic.manager.expenses");
    private static final ITextComponent INCOME = new TranslationTextComponent("gui.shopaholic.manager.income");
    private static final ITextComponent PROFIT = new TranslationTextComponent("gui.shopaholic.manager.profit");
    private static final ITextComponent BALANCE = new TranslationTextComponent("gui.shopaholic.manager.balance");

    private final Book book;
    private boolean isSharedWalletActive;
    private Wallet personal, shared;
    private String playerName;
    private String teamName;

    public EconomyStatsLabel(Book b, int x, int y, ITextComponent name) {
        super(x, y, 0, 0, name);
        PlayerEntity player = Minecraft.getInstance().player;
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        personal = Wallet.getWallet(WalletType.PERSONAL);
        shared = player.getUUID().equals(team.getID()) ? null : Wallet.getWallet(WalletType.SHARED);
        isSharedWalletActive = Wallet.getActive() == shared;
        playerName = WalletType.PERSONAL.getName(player);
        teamName = WalletType.SHARED.getName(player);
        book = b;
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
        mc.font.drawShadow(matrix, TextFormatting.BOLD + playerName, x, y, book.fontColor1);
        if (shared != null) {
            mc.font.drawShadow(matrix, TextFormatting.BOLD + teamName, x, y + 100, book.fontColor1);
            hLine(matrix, x - 6, x + 115, y + 46, book.lineColor1);
            hLine(matrix, x - 5, x + 116, y + 46 + 1, book.lineColor2);
            hLine(matrix, x - 6, x + 115, y + 96, book.lineColor1);
            hLine(matrix, x - 5, x + 116, y + 96 + 1, book.lineColor2);
            StringHelper.disableUnicode();
            mc.font.draw(matrix, TextFormatting.BOLD + "^", x + 55, y + 64, book.fontColor1);
            int length = mc.font.width(TRANSFER);
            mc.font.drawShadow(matrix, TRANSFER, x + 60 - length / 2F, y + 67, 0xFFFFFF);
            mc.font.draw(matrix, TextFormatting.BOLD + "v", x + 55, y + 74, book.fontColor1);
        } else StringHelper.disableUnicode();

        matrix.pushPose();
        float scale = 0.6666666666F;
        matrix.scale(scale, scale, scale);
        ////Personal Account
        //Expenses
        mc.font.drawShadow(matrix, EXPENSES, (x) / scale, (y + 10) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getExpenses()), (x + 45) / scale, (y + 10) / scale, 0xFFFFFFFF);
        //Income
        mc.font.drawShadow(matrix, INCOME, (x) / scale, (y + 19) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getIncome()), (x + 45) / scale, (y + 19) / scale, 0xFFFFFFFF);
        //Profit
        mc.font.drawShadow(matrix, PROFIT, (x) / scale, (y + 28) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getIncome() - personal.getExpenses()), (x + 45) / scale, (y + 28) / scale, 0xFFFFFFFF);
        //Balance
        mc.font.drawShadow(matrix, BALANCE, (x) / scale, (y + 37) / scale, 0xFFFFFFFF);
        mc.font.drawShadow(matrix, formatGold(personal.getBalance()), (x + 45) / scale, (y + 37) / scale, 0xFFFFFFFF);

        if (shared != null) {
            ////Shared Account
            //Expenses
            mc.font.drawShadow(matrix, EXPENSES, (x) / scale, (y + 110) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getExpenses()), (x + 45) / scale, (y + 110) / scale, 0xFFFFFFFF);
            //Income
            mc.font.drawShadow(matrix, INCOME, (x) / scale, (y + 119) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getIncome()), (x + 45) / scale, (y + 119) / scale, 0xFFFFFFFF);
            //Profit
            mc.font.drawShadow(matrix, PROFIT, (x) / scale, (y + 128) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getIncome() - shared.getExpenses()), (x + 45) / scale, (y + 128) / scale, 0xFFFFFFFF);
            //Balance
            mc.font.drawShadow(matrix, BALANCE, (x) / scale, (y + 137) / scale, 0xFFFFFFFF);
            mc.font.drawShadow(matrix, formatGold(shared.getBalance()), (x + 45) / scale, (y + 137) / scale, 0xFFFFFFFF);
        }

        RenderSystem.color4f(1F, 1F, 1F, 1F);
        mc.getTextureManager().bind(ShopScreen.EXTRA);
        blit(matrix, (int) ((x + 35) / scale), (int) ((y + 9) / scale), 244, 244, 12, 12);
        blit(matrix, (int) ((x + 35) / scale), (int) ((y + 18) / scale), 244, 244, 12, 12);
        blit(matrix, (int) ((x + 35) / scale), (int) ((y + 27) / scale), 244, 244, 12, 12);
        blit(matrix, (int) ((x + 35) / scale), (int) ((y + 36) / scale), 244, 244, 12, 12);
        if (shared != null) {
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 109) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 118) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 127) / scale), 244, 244, 12, 12);
            blit(matrix, (int) ((x + 35) / scale), (int) ((y + 136) / scale), 244, 244, 12, 12);
        }

        matrix.popPose();
    }
}

