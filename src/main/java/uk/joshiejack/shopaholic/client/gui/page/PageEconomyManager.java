package uk.joshiejack.shopaholic.client.gui.page;

import joptsimple.internal.Strings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractMultiPage;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.TextureIcon;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.gui.widget.*;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PageEconomyManager extends AbstractMultiPage.Right<Shipping.SoldItem> {
    public static final Icon ICON = new TextureIcon(ShopScreen.EXTRA, 240, 224);
    public static final ITextComponent EMPTY_STRING = new StringTextComponent(Strings.EMPTY);
    private static final long[] divisions = new long[]{1, 5, 10, 100, 1000};

    public PageEconomyManager(ITextComponent name) {
        super(name, 30);
    }

    @Override
    public void initLeft(Book book, int left, int top) {
        book.addButton(new EconomyStatsLabel(book, left + 25, top + 8));
        PlayerEntity player = book.minecraft().player;
        assert player != null;
        if (!player.getUUID().equals(PenguinTeams.getTeamForPlayer(player).getID())) {
            for (int i = 0; i < divisions.length; i++) {
                int y = 57;
                book.addButton(new TransferBalanceButton(WalletType.PERSONAL, divisions[i], book, left + 22 + 25 * i, top + y + 3));
                book.addButton(new TransferBalanceButton(WalletType.SHARED, divisions[i], book, left + 22 + 25 * i, top + y + 35));
            }

            int pWidth = book.minecraft().font.width(WalletType.PERSONAL.getName(player));
            int tWidth = book.minecraft().font.width(WalletType.SHARED.getName(player));
            book.addButton(new SwitchAccountButton(WalletType.PERSONAL, book, left + 12 + pWidth, top + 9));
            book.addButton(new SwitchAccountButton(WalletType.SHARED, book, left + 12 + tWidth, top + 109));
        }
    }

    @Override
    public void initRight(Book book, int left, int top) {
        book.addButton(new ShippingLogLabel(book, left + 15, top + 8));
        super.initRight(book, left, top);
    }

    @Override
    protected void initEntry(Book book, int x, int y, int id, Shipping.SoldItem sold) {
        book.addButton(new ShippedItemLabel(book, x + 10 + ((id % 6) * 21), y + 10 + ((id / 6) * 21), sold.getStack(), sold.getValue()));
    }

    @Override
    protected List<Shipping.SoldItem> getEntries() {
        return Shipped.getShippingLog();
    }

    @Override
    protected Icon getIcon() {
        return ICON;
    }
}
