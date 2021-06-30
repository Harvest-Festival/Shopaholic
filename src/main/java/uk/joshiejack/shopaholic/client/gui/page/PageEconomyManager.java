package uk.joshiejack.shopaholic.client.gui.page;

import joptsimple.internal.Strings;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.penguinlib.client.gui.book.page.AbstractMultiPage;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.TextureIcon;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.gold.WalletType;
import uk.joshiejack.shopaholic.client.Shipped;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;
import uk.joshiejack.shopaholic.client.gui.widget.EconomyStatsLabel;
import uk.joshiejack.shopaholic.client.gui.widget.TransferBalanceButton;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class PageEconomyManager extends AbstractMultiPage.Right<Shipping.SoldItem> {
    public static final Icon ICON = new TextureIcon(ShopScreen.EXTRA, 240, 224);
    private long[] divisions = new long[]{1, 5, 10, 100, 1000};

    public PageEconomyManager(ITextComponent name) {
        super(name, 36);
    }

//    @Override
//    public void drawScreen(int x, int y) {
//        if (empty) drawStringWithWrap(StringHelper.localize(unlocalized), gui.getGuiLeft() + 30, 24, 124);
//    }
//
//    @Override
//    public void initGui(List<GuiButton> buttonList, List<GuiLabel> labelList) {
//        super.initGui(buttonList, labelList);
//        labelList.add(new EconomyStatsLabel(gui, 25, 24));
//        labelList.add(new ShippingLogLabel(gui, 25, 24));
//        for (int i = 0; i < divisions.length; i++) {
//            buttonList.add(new TransferButton(Wallet.Type.PERSONAL, divisions[i], gui, buttonList.size(), 22 + 25 * i, 75));
//            buttonList.add(new TransferButton(Wallet.Type.SHARED, divisions[i], gui, buttonList.size(), 22 + 25 * i, 107));
//        }
//
//        //If our team is the same as our id, remove the shared wallet
//        if (Wallet.getActive() == Wallet.getWallet(Wallet.Type.PERSONAL)) {
//            buttonList.add(new SwitchAccountButton(Wallet.Type.SHARED, gui, buttonList.size(), 25, 24));
//        } else buttonList.add(new SwitchAccountButton(Wallet.Type.PERSONAL, gui, buttonList.size(), 25, 24));
//    }

    @Override
    public void initLeft(Book book, int left, int top) {
        int maxPage = (int) Math.ceil((double) this.entries.size() / (double) this.perPage) - 1;
        System.out.println(maxPage);
        book.addButton(new EconomyStatsLabel(left + 25, top + 5, new StringTextComponent(Strings.EMPTY)));
        if (!Minecraft.getInstance().player.getUUID().equals(PenguinTeams.getTeamForPlayer(Minecraft.getInstance().player).getID()))
            for (int i = 0; i < divisions.length; i++) {
                int y = 57;
                book.addButton(new TransferBalanceButton(WalletType.PERSONAL, divisions[i], book, left + 22 + 25 * i, top + y));
                book.addButton(new TransferBalanceButton(WalletType.SHARED, divisions[i], book, left + 22 + 25 * i, top + y + 32));
            }
    }

//    @Override
//    public BackButton createBackButton(List<GuiButton> buttonList) { return new BackButton(this, gui, buttonList.size(), 164, 138); }
//
//    @Override
//    public ForwardButton createForwardButton(List<GuiButton> buttonList) { return new ForwardButton(this, gui, buttonList.size(), 270, 138); }
//
//    @Override
//    protected BookLabel createLabel(BookScreen gui, Shipping.HolderSold entry, PageSide side, int position) {
//        return new ShippedItemLabel(gui, 163 + (position % 6) * 21, 28 + (side == PageSide.RIGHT ? 54 : 0) + (position / 6) * 18, new CyclingStack(NonNullList.from(ItemStack.EMPTY, entry.getStack())), entry.getValue());
//    }

    @Override
    protected void initEntry(Book book, int i, int i1, int i2, Shipping.SoldItem sold) {

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
