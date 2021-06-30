package uk.joshiejack.shopaholic.client.gui.button;

//public class ButtonShopTab extends GuiButton {
//    private final ShopScreen gui;
//    private final ItemStack icon;
//    private final Department shop;
//
//    public ButtonShopTab(ShopScreen gui, Department shop, int buttonId, int x, int y) {
//        super(buttonId, x, y, "");
//        this.gui = gui;
//        this.shop = shop;
//        this.icon = shop.getIcon();
//        this.width = 21;
//        this.height = 22;
//    }
//
//    @Override
//    public void drawButton(@Nonnull Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//        if (visible) {
//            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
//            mc.getTextureManager().bindTexture(ShopScreen.EXTRA);
//            drawTexturedModalRect(x, y, 107, 59 + (hovered ? 22: 0), 21, 22);
//            StackRenderHelper.drawStack(icon, x + 4, y + 3, 1F);
//            if (hovered) {
//                gui.addTooltip(Lists.newArrayList(shop.getLocalizedName()));
//            }
//        }
//    }
//
//    @Override
//    public void mouseReleased(int mouseX, int mouseY) {
//        Shop.get(shop).setLast(shop); //Update the last value to the new tab
//        Minecraft.getMinecraft().displayGuiScreen(new ShopScreen(shop, gui.target));
//    }
//}
