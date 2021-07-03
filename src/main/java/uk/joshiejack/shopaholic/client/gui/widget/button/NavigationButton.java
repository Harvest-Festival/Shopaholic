package uk.joshiejack.shopaholic.client.gui.widget.button;

//public abstract class ButtonArrow extends GuiButton {
//    protected final ShopScreen shop;
//    private final int xCoord;
//    private final int amount;
//
//    public ButtonArrow(ShopScreen shop, int amount, int xCoord, int buttonId, int x, int y) {
//        super(buttonId, x, y, "");
//        this.shop = shop;
//        this.xCoord = xCoord;
//        this.width = 14;
//        this.height = 12;
//        this.amount = amount;
//    }
//
//    @Override
//    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//        updateVisiblity();
//        if (visible) {
//            mc.getTextureManager().bindTexture(ShopScreen.EXTRA);
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
//            int state = getHoverState(hovered);
//            GlStateManager.enableBlend();
//            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
//            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
//            drawTexturedModalRect(x, y, xCoord, state * 12, width, height);
//            mouseDragged(mc, mouseX, mouseY);
//            GlStateManager.color(1.0F, 1.0F, 1.0F);
//        }
//    }
//
//    protected abstract void updateVisiblity();
//
//    @Override
//    public void mouseReleased(int mouseX, int mouseY) {
//        if (GuiScreen.isShiftKeyDown()) shop.scroll(amount * 10);
//        else shop.scroll(amount);
//    }
//}
