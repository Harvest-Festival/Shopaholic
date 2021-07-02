package uk.joshiejack.shopaholic.client.gui.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import joptsimple.internal.Strings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.widget.AbstractButton;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.network.bank.SwitchWalletPacket;

import javax.annotation.Nonnull;
import java.util.List;

public class SwitchAccountButton extends AbstractButton {
    private static final List<ITextComponent> active = Lists.newArrayList(new TranslationTextComponent("gui.shopaholic.manager.wallet.currently",
            new TranslationTextComponent("gui.shopaholic.manager.wallet.active").withStyle(TextFormatting.GREEN)));
    private static final List<ITextComponent> inactive = Lists.newArrayList(new TranslationTextComponent("gui.shopaholic.manager.wallet.currently",
            new TranslationTextComponent("gui.shopaholic.manager.wallet.inactive").withStyle(TextFormatting.RED)),
            new TranslationTextComponent("gui.shopaholic.manager.wallet.switch"));
    private final WalletType type;

    public SwitchAccountButton(WalletType type, Book book, int x, int y) {
        super(book, x, y, 7, 8, new StringTextComponent(Strings.EMPTY), (btn) -> {
            boolean isWalletActive = Wallet.getActive() == Wallet.getWallet(type);
            if (!isWalletActive)
                PenguinNetwork.sendToServer(new SwitchWalletPacket(Wallet.getActive() == Wallet.getWallet(WalletType.PERSONAL)));
        }, (btn, mtx, mX, mY) -> {
            boolean isWalletActive = Wallet.getActive() == Wallet.getWallet(type);
            GuiUtils.drawHoveringText(mtx, isWalletActive ? active : inactive, mX, mY, book.width, book.height, 200, book.minecraft().font);
        });
        this.type = type;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        if (visible) {
            StringHelper.enableUnicode();
            book.bindLeftTexture();
            boolean isWalletActive = Wallet.getActive() == Wallet.getWallet(type);
            blit(matrix, x, y, 31 + (!isWalletActive ? 10 : 0) + (hovered ? 17 : 0), 248, 7 + (isWalletActive ? 3 :0), 8);
            StringHelper.disableUnicode();
        }
    }
}
