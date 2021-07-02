package uk.joshiejack.shopaholic.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.client.gui.page.PageEconomyManager;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class ShippingLogLabel extends Widget {
    private static final ITextComponent NAME = new TranslationTextComponent("gui.shopaholic.manager.shipping").withStyle(TextFormatting.UNDERLINE);
    private static final ITextComponent COMBINED = new TranslationTextComponent("gui.shopaholic.manager.combined").withStyle(TextFormatting.BOLD);
    private final Book book;
    private final long total;
    private final long overall;

    public ShippingLogLabel(Book gui, int x, int y) {
        super(x, y, 0, 0, PageEconomyManager.EMPTY_STRING);
        long total = 0L;
        for (Shipping.SoldItem holderSold : Shipped.getShippingLog()) {
            total += holderSold.getValue();
        }

        this.total = total;
        this.overall = Wallet.getWallet(WalletType.PERSONAL).getIncome() + Wallet.getWallet(WalletType.SHARED).getIncome();
        this.book = gui;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        //GlStateManager.disableDepth();
        //Draw the shipping log
        mc.font.drawShadow(matrix, NAME, x + 61 - (mc.font.width(NAME) / 2F), y - 10, 0xFFFFFF);
        StringHelper.enableUnicode();

        hLine(matrix, x - 6, x + 115, y, book.lineColor1);
        hLine(matrix, x - 5, x + 116, y + 1, book.lineColor2);

        //Draw the statistics
        mc.font.drawShadow(matrix, COMBINED, x + 61 - (mc.font.width(COMBINED) / 2F), y + 128, book.fontColor1);
        hLine(matrix, x - 6, x + 115, y + 138, book.lineColor1);
        hLine(matrix, x - 5, x + 116, y + 139, book.lineColor2);
        StringHelper.disableUnicode();

        //Draw the shipping log totals
        if (total > 0L) {
            //GlStateManager.color(1F, 1F, 1F, 1F);
            mc.getTextureManager().bind(DepartmentScreen.EXTRA);
            blit(matrix, x - 3 + 18, y + 110 + 4, 244, 244, 12, 12);
            String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(total);
            mc.font.drawShadow(matrix, text, x - 3 + 32, y + 110 + 6, 0xFFFFFFFF);
        }

        mc.getTextureManager().bind(DepartmentScreen.EXTRA);
        blit(matrix, x - 3 + 18, y + 138 + 4, 244, 244, 12, 12);
        String text = NumberFormat.getNumberInstance(Locale.ENGLISH).format(overall);
        mc.font.drawShadow(matrix, text, x - 3 + 32, y + 138 + 6, 0xFFFFFFFF);
    }
}

