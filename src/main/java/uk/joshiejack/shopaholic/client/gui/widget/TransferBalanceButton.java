package uk.joshiejack.shopaholic.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.widget.AbstractButton;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.network.bank.TransferBalancePacket;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TransferBalanceButton extends AbstractButton {
    private static final ITextComponent FROM_SHARED = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager.from.shared").withStyle(TextFormatting.GOLD);
    private static final ITextComponent FROM_PERSONAL = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager.from.personal").withStyle(TextFormatting.GOLD);
    private static final ITextComponent X10 = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager.x.10", new StringTextComponent("SHIFT").withStyle(TextFormatting.AQUA));
    private static final ITextComponent X100 = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager.x.100", new StringTextComponent("CTRL").withStyle(TextFormatting.GREEN));
    private static final ITextComponent X1000 = new TranslationTextComponent("gui." + Shopaholic.MODID + ".manager.x.1000", new StringTextComponent("ALT").withStyle(TextFormatting.LIGHT_PURPLE));
    private final long value;

    public TransferBalanceButton(WalletType from, long value, Book book, int x, int y) {
        super(book, x, y, 23, 10, new StringTextComponent(String.valueOf(value)), (btn) -> {
            long gold = value;
            if (Screen.hasShiftDown()) gold *= 10;
            if (Screen.hasControlDown()) gold *= 100;
            if (Screen.hasAltDown()) gold *= 1000;
            PenguinNetwork.sendToServer(new TransferBalancePacket(from, gold));
        }, (btn, mtx, mX, mY) -> {
            List<ITextComponent> tooltip = new ArrayList<>();
            if (from == WalletType.SHARED)
                tooltip.add(FROM_SHARED);
            else
                tooltip.add(FROM_PERSONAL);
            tooltip.add(X10);
            tooltip.add(X100);
            tooltip.add(X1000);
            GuiUtils.drawHoveringText(mtx, tooltip, mX, mY, book.width, book.height, 200, book.minecraft().font);
        });

        this.value = value;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        book.bindLeftTexture();
        blit(matrix, x, y, hovered ? 23 : 0, 193, 23, 10);
        //Texture drawn, now to draw the text
        RenderSystem.pushMatrix();
        float scale = 0.5F;
        RenderSystem.scalef(scale, scale, scale);
        long gold = value;
        if (Screen.hasShiftDown()) gold *= 10;
        if (Screen.hasControlDown()) gold *= 100;
        if (Screen.hasAltDown()) gold *= 1000;
        book.minecraft().font.drawShadow(matrix, StringHelper.convertNumberToString(gold), (x + 9) / scale, (y + 3) / scale, 0xFFFFFF);
        RenderSystem.color3f(1.0F, 1.0F, 1.0F);
        book.minecraft().getTextureManager().bind(DepartmentScreen.EXTRA);
        blit(matrix, (int)((x + 2) / scale), (int)((y + 2) / scale), 244, 244, 12, 12);
        RenderSystem.popMatrix();
    }
}
