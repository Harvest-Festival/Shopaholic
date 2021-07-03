package uk.joshiejack.shopaholic.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.widget.AbstractButton;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.network.shop.ServerPurchaseItemPacket;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.Sublisting;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractListingButton extends AbstractButton<DepartmentScreen> {
    protected final Listing listing;
    protected final Sublisting<?> sublisting;
    protected final Wallet wallet = Wallet.getActive();
    private boolean pressed;
    private int hoverTimer = 0;
    protected int stackSize = 0;

    public AbstractListingButton(DepartmentScreen screen, int x, int y, Listing listing) {
        super(screen, x, y, 200, 18, listing.getSubListing(screen.stock).getDisplayName(),
                (btn) -> PenguinNetwork.sendToServer(new ServerPurchaseItemPacket(screen.getMenu().department, listing,
                        ((AbstractListingButton) btn).stackSize)), (btn, mtx, mX, mY) -> {
                    List<ITextComponent> tooltip = new ArrayList<>();
                    listing.getSubListing(screen.stock).addTooltip(tooltip);
                    GuiUtils.drawHoveringText(mtx, tooltip, mX, mY, screen.width, screen.height, 200, screen.getMinecraft().font);
                });

        this.sublisting = listing.getSubListing(screen.stock);
        this.listing = listing;
    }

    @Override
    public void onPress() {
        pressed = true;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        mc.getTextureManager().bind(DepartmentScreen.EXTRA);
        int state = hovered && !canPurchase(1) ? 1 : hovered ? 2 : 0;
        //Background
        blit(matrix, x, y, 0, state * 18, width / 2, height);
        blit(matrix, x + width / 2, y, 200 - width / 2, state * 18, width / 2, height);
        //Foreground
        int color = !active ? 10526880 : hovered && canPurchase(1) ? 16777120 : 14737632;
        drawForeground(matrix, hovered, color);
        if (pressed)
            whilePressed();
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        this.onPress.onPress(this);
        this.pressed = false;
        this.stackSize = 0;
        this.hoverTimer = 0;
    }

    private void whilePressed() {
        //If ctrl is down,
        if (hoverTimer == 0 || (hoverTimer % 20 == 0 && Screen.hasControlDown())) {
            Icon display = listing.getSubListing(screen.stock).getIcon();
            long goldCost = listing.getGoldCost(screen.getMenu().target.getPlayer(), screen.stock);
            int multiplier = Screen.hasShiftDown() ? 10: 1;
            if (multiplier == 10 && !canPurchase(stackSize + 10)) multiplier = 1;
            if (canPurchase(multiplier)) {
                stackSize += multiplier;
                wallet.setBalance(wallet.getBalance() - goldCost * multiplier);
                screen.updatePurchased(display, multiplier * sublisting.getCount());
            }

            hoverTimer++;
        }

        if (Screen.hasControlDown()) {
            hoverTimer++;
        }
    }

    protected abstract void drawForeground(@Nonnull MatrixStack matrix, boolean hovered, int color);

    private boolean canPurchase(int x) {
        return wallet.getBalance() - (listing.getGoldCost(screen.getMenu().target.getPlayer(), screen.stock) * x) >= 0
                && listing.canPurchase(screen.getMenu().target.getPlayer(), screen.stock, x);
    }
}

//@SideOnly(Dist.CLIENT)
//public class ButtonListing extends GuiButton {
//    private static final int HOVER = 2;
//    protected final ShopScreen shop;
//    protected final Listing listing;
//    protected final Sublisting sublisting;
//    private final Wallet wallet; //The active wallet
//    protected int state;
//    private int hoverTimer;
//    private int stackSize = 1;
//    private boolean clicked;
//    //private final ShopData data;
//
//    @SuppressWarnings("unchecked")
//    public ButtonListing(ShopScreen shop, Listing listing, int buttonId, int x, int y) {
//        super(buttonId, x, y, listing.getSubListing(shop.stock).getDisplayName());
//        this.height = 18;
//        this.shop = shop;
//        this.listing = listing;
//        this.sublisting = listing.getSubListing(shop.stock);
//        this.wallet = Wallet.getActive();
//        //this.data = TownHelper.getClosestTownToEntity(MCClientHelper.getPlayer(), false).getShops();
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//        if (visible) {
//            FontRenderer fontrenderer = mc.fontRenderer;
//            mc.getTextureManager().bindTexture(ShopScreen.EXTRA);
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
//            int originalState = getHoverState(hovered);
//            state = originalState;
//            if (state == HOVER && !canPurchase1()) {
//                state = 1;
//            }
//
//            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            drawBackground();
//            mouseDragged(mc, mouseX, mouseY);
//            int j = 14737632;
//
//            if (packedFGColour != 0) {
//                j = packedFGColour;
//            } else if (!enabled) {
//                j = 10526880;
//            } else if (state == HOVER && hovered) {
//                j = 16777120;
//            }
//
//            drawForeground(mc, fontrenderer, j);
//            GlStateManager.color(1.0F, 1.0F, 1.0F);
//
//            if (originalState == HOVER && listing.getGoldCost(shop.target.player, shop.stock) >= 0) {
//                List<String> list = new ArrayList<>();
//                sublisting.addTooltip(list);
//                shop.addTooltip(list);
//            }
//        }
//    }
//
//    private void drawBackground() {
//        drawTexturedModalRect(x, y, 0, state * 18, width / 2, height);
//        drawTexturedModalRect(x + width / 2, y, 200 - width / 2, state * 18, width / 2, height);
//    }
//
//    protected void drawForeground(Minecraft mc, FontRenderer fontrenderer, int j) {
//        GlStateManager.enableDepth();
//        StackRenderHelper.drawStack(listing.getSubListing(shop.stock).getIcon(), x + 2, y + 1, 1F);
//        drawString(fontrenderer, displayString, x + 20, y + (height - 8) / 2, j);
//        GlStateManager.color(1.0F, 1.0F, 1.0F);
//
//        //Draw the cost
//        long goldCost = listing.getGoldCost(shop.target.player, shop.stock);
//        String cost = shop.getCostAsString(goldCost);
//        if (goldCost != 0) {
//            int width = fontrenderer.getStringWidth(cost);
//            drawString(fontrenderer, cost, x + 180 - width, y + (height - 8) / 2, j);
//            mc.renderEngine.bindTexture(ShopScreen.EXTRA);
//            drawTexturedModalRect(x + 184, (y + (height - 8) / 2) - 2, 244, 244, 12, 12);
//        } else {
//            int width = fontrenderer.getStringWidth(cost);
//            drawString(fontrenderer, cost, x + 194 - width, y + (height - 8) / 2, j);
//        }
//    }
//
//    @Override
//    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
//        clicked = super.mousePressed(mc, mouseX, mouseY);
//        return clicked;
//    }
//
//    @Override
//    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY) {
//        if (clicked && hovered) {
//            if (hoverTimer == 0 || (hoverTimer %20 == 0 && GuiScreen.isCtrlKeyDown())) {
//                ItemStack display = listing.getSubListing(shop.stock).getIcon();
//                long goldCost = listing.getGoldCost(shop.target.player, shop.stock);
//                if (GuiScreen.isShiftKeyDown() && canPurchaseX(stackSize + 10)) {
//                    stackSize += 10;
//                    wallet.setBalance(wallet.getBalance() - goldCost * 10);
//                    if (goldCost >= 0) shop.updatePurchased(StackHelper.toStack(display, 10 * display.getCount()), 10 * display.getCount());
//                    if (hoverTimer %40 == 1) playPressSound(mc.getSoundHandler());
//                } else if (canPurchaseX(stackSize + 1)) {
//                    stackSize++;
//                    if (goldCost >= 0) shop.updatePurchased(display, display.getCount());
//                    wallet.setBalance(wallet.getBalance() - goldCost);
//                    if (hoverTimer %40 == 1) playPressSound(mc.getSoundHandler());
//                }
//
//                hoverTimer++;
//            }
//
//            if (GuiScreen.isCtrlKeyDown()) {
//                hoverTimer++;
//            }
//        } else {
//            hoverTimer = 0;
//            stackSize = 0;
//        }
//    }
//
//    @Override
//    public void mouseReleased(int mouseX, int mouseY) {
//        if (stackSize > 0) {
//            PenguinNetwork.sendToServer(new ServerPurchaseItemPacket(shop.shop, listing, stackSize));
//            stackSize = 0; //Reset
//        }
//
//        //Reset the clicking timer
//        clicked = false;
//    }
//
//    @Override
//    public void playPressSound(SoundHandler soundHandlerIn) {
//        if (state == HOVER) super.playPressSound(soundHandlerIn);
//    }
//

//}
