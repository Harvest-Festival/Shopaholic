package uk.joshiejack.shopaholic.client.gui.button;

//public class TransferButton extends BookButton {
//    private final Wallet.Type from;
//    private final long value;
//
//    public TransferButton(Wallet.Type wallet, long value, BookScreen gui, int buttonId, int x, int y) {
//        super(gui, buttonId, x, y, "");
//        this.width = 23;
//        this.height = 10;
//        this.from = wallet;
//        this.value = value;
//    }
//
//    @Override
//    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
//        if (visible) {
//            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
//            mc.getTextureManager().bindTexture(gui.left); //Elements
//            hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height;
//            if (hovered) {
//                if (from == Wallet.Type.SHARED) gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.GOLD + "Click to transfer from the shared wallet to your personal wallet.", 200));
//                else gui.addTooltip(mc.fontRenderer.listFormattedStringToWidth(TextFormatting.GOLD + "Click to transfer from your personal wallet to the shared wallet.", 200));
//                gui.addTooltip(String.format("Hold down %sSHIFT %sfor 10x", TextFormatting.AQUA, TextFormatting.RESET));
//                gui.addTooltip(String.format("Hold down %sCTRL %sfor 100x", TextFormatting.GREEN, TextFormatting.RESET));
//                gui.addTooltip(String.format("Hold down %sALT %sfor 1000x", TextFormatting.LIGHT_PURPLE, TextFormatting.RESET));
//            }
//
//            drawTexturedModalRect(x, y, hovered ? 23 : 0, 193, 23, 10);
//            GlStateManager.pushMatrix();
//            float scale = 0.5F;
//            GlStateManager.scale(scale, scale, scale);
//            long gold = value;
//            if (GuiScreen.isShiftKeyDown()) gold *= 10;
//            if (GuiScreen.isCtrlKeyDown()) gold *= 100;
//            if (GuiScreen.isAltKeyDown()) gold *= 1000;
//            mc.fontRenderer.drawStringWithShadow(StringHelper.convertNumberToString(gold), (x + 9) / scale, (y + 3) / scale, 0xFFFFFF);
//            GlStateManager.color(1.0F, 1.0F, 1.0F);
//            mc.getTextureManager().bindTexture(ShopScreen.EXTRA);
//            drawTexturedModalRect((x + 2) / scale, (y + 2)/scale, 244, 244, 12, 12);
//            GlStateManager.popMatrix();
//        }
//    }
//
//    @Override
//    public void mouseReleased(int mouseX, int mouseY) {
//        long gold = value;
//        if (GuiScreen.isShiftKeyDown()) gold *= 10;
//        if (GuiScreen.isCtrlKeyDown()) gold *= 100;
//        if (GuiScreen.isAltKeyDown()) gold *= 1000;
//        PenguinNetwork.sendToServer(new TransferBalancePacket(from, gold));
//    }
//}
