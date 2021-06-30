package uk.joshiejack.shopaholic.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;
import uk.joshiejack.shopaholic.inventory.ShopContainer;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.Shop;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

import java.text.DecimalFormat;
import java.util.Collection;

@SuppressWarnings("ConstantConditions")
public class ShopScreen extends AbstractContainerScreen<ShopContainer> {
    public static final ResourceLocation EXTRA =  new ResourceLocation(Shopaholic.MODID, "textures/gui/shop_extra.png");
    private static final ResourceLocation BACKGROUND =  new ResourceLocation(Shopaholic.MODID, "textures/gui/shop.png");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private final Collection<Listing> contents;
    public final ShopTarget target;
    public final Department shop;
    public final Stock stock;
    private final Shop supermarket;
    private ItemStack purchased;
    private int purcasableCount;
    private int start;
    private int end;

    public ShopScreen(Department shop, ShopTarget target, ShopContainer container, PlayerInventory inv) {
        super(container, inv, shop.getLocalizedName(), BACKGROUND, 256, 256);
        this.target = target;
        this.purchased = ItemStack.EMPTY;
        this.supermarket = Shop.get(shop);
        this.shop = this.supermarket != null ? supermarket.getLast() : shop;
        this.stock = shop.getStockLevels();
        this.contents = Lists.newArrayList();
        for (Listing listing : ImmutableList.copyOf(this.shop.getListings())) {
            if (listing.canList(target, stock)) {
                contents.add(listing);
            }
        }
    }
//
//    @Override
//    public void init() {
//        super.init();
//        setStart(0); //Reload
//    }
//
//    private ITextComponent getShopName() {
//        return supermarket != null ? supermarket.getLocalizedName() : shop.getLocalizedName();
//    }
//
//    @Override
//    public void renderBg(@Nonnull MatrixStack matrix, float partialTicks, int x, int y) {
//        GlStateManager.pushMatrix();
//        GlStateManager.enableBlend();
//        drawResizableBackground(x, y);
//        FancyFontRenderer.render(this, x + 20, guiTop + 17, getShopName(), false);
//        drawCoinage(x, guiTop + 19, Wallet.getActive().getBalance());
//        drawPlayerInventory();
//        GlStateManager.disableBlend();
//        GlStateManager.popMatrix();
//    }
//
//    private void drawResizableBackground(int x, int y) {
//        minecraft.getTextureManager().bind(BACKGROUND);
//        int heightToUse = Math.max(purcasableCount, 3);
//        if (heightToUse < 12) {
//            drawTexturedModalRect(x, y - 12 + (20 * heightToUse), 0, 228, xSize, 28);
//            drawTexturedModalRect(x, y, 0, 0, xSize, (20 * heightToUse) - 2);
//
//        } else drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
//    }
//
//    private void drawCoinage(int x, int y, long gold) {
//        String formatted = String.valueOf(formatter.format(gold));
//        FancyFontRenderer.render(this, x + 220, y, formatted, true);
//        GlStateManager.disableDepth();
//        minecraft.renderEngine.bindTexture(EXTRA);
//        minecraft.ingameGUI.drawTexturedModalRect((x + 224), y - 1, 244, 244, 12, 12);
//        GlStateManager.enableDepth();
//    }
//
//    private void drawPlayerInventory() {
//        //FancyFontRenderer.render(this, guiLeft + 250, guiTop + 27, "BUYING", false);
//        minecraft.renderEngine.bindTexture(EXTRA);
//        drawTexturedModalRect(guiLeft + 240, guiTop + 40, 0, 62, 100, 194);
//
//        int x2 = 0, y2 = 0;
//        boolean first = true;
//        FancyFontRenderer.render(this, guiLeft + 240, guiTop + 44, "Inventory", false);
//        for (ItemStack stack: target.player.inventory.items) {
//            if (!stack.isEmpty()) {
//                StackRenderHelper.drawStack(stack, guiLeft + 253 + y2, guiTop + 61 + x2 * 18, 1F);
//            }
//
//            x2++;
//            if (x2 >= 9) {
//                x2 = 0;
//                if (first) {
//                    y2 += 22;
//                    first = false;
//                } else {
//                    y2 += 18;
//                }
//            }
//        }
//    }
//
//    @Override
//    public void drawForeground(int x, int y) {
//        if (!purchased.isEmpty()) {
//            StackRenderHelper.drawStack(purchased, x - guiLeft, y - guiTop, 1F);
//        }
//    }
//
//    public void refresh() {
//        setStart(start);
//    }
//
//    public void setStart(int i) {
//        buttonList.clear();
//
//        //Up Arrow
//        buttonList.add(new ButtonArrow(this, -1, 225, 0, guiLeft + 232, guiTop + 60) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start != 0;
//            }
//        });
//
//        //Down Arrow
//        buttonList.add(new ButtonArrow(this, +1, 242, 1, guiLeft + 232, guiTop + 210) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start < end;
//            }
//        });
//
//        end = contents.size() - 10;
//        start = Math.max(0, Math.min(end, i));
//        purcasableCount = 2;
//
//        //Arrows are added, now add the items being sold
//        int id = start;
//        int position = 0;
//        int pPosition = 0;
//        Iterator<Listing> it = contents.iterator();
//        while (it.hasNext() && position <= 180) {
//            Listing purchasable = it.next();
//            if (pPosition >= start && purchasable.canList(target, stock)) {
//                if (purchasable.getGoldCost(target.player, stock) < 0) {
//                    buttonList.add(new ButtonListing(this, purchasable, id + 2, guiLeft + 28, 38 + guiTop + position));
//                    purcasableCount++;
//                    position += 20;
//                } else {
//                    int add = addButton(purchasable, id + 2, guiLeft + 28, 38 + guiTop + position, position);
//                    purcasableCount = add > 0 ? purcasableCount + 1 : purcasableCount;
//                    position += add;
//                }
//
//                id++;
//            }
//
//            pPosition++;
//        }
//
//        if (buttonList.size() == 2) buttonList.add(new ButtonOutOfStock(this, 3, guiLeft + 28, 38 + guiTop + position));
//
//        //Tabs
//        if (supermarket != null && supermarket.getDepartments().size() > 1) {
//            int j = 0;
//            for (Department shop : ImmutableList.copyOf(supermarket.getDepartments()).reverse()) {
//                if (shop.getListings().stream().anyMatch(l -> l.canList(target, stock))) {
//                    buttonList.add(new ButtonShopTab(this, shop, buttonList.size(), guiLeft + 5, guiTop + 38 + (j * 23)));
//                    j++;
//                }
//            }
//
//            if (purcasableCount < (3 + supermarket.getDepartments().size())) {
//                purcasableCount = 3 + supermarket.getDepartments().size();
//            }
//        }
//    }
//
//    @SuppressWarnings("unchecked")
//    private int addButton(Listing listing, int id, int left, int top, int space) {
//        if (listing.getSubListing(stock).isGoldOnly()) {
//            if (space + 20 <= 200) {
//                buttonList.add(new ButtonListing(this, listing, id, left, top));
//                return 20;
//            }
//        } else {
//            if (listing.getSubListing(stock).getMaterials().size() == 1 && listing.getGoldCost(target.player, stock) == 0) {
//                if (space + 20 <= 200) {
//                    MaterialCost requirement = ((List<MaterialCost>) listing.getSubListing(stock).getMaterials()).get(0);
//                    buttonList.add(new ButtonListingItem(requirement, this, listing, id, left, top));
//                    return 20;
//                }
//            } else if (space + 20 <= 200) {
//                buttonList.add(new ButtonListingBuilding(this, listing, id, left, top));
//                return 20;
//            }
//        }
//
//        return 0;
//    }
//
//    public String getCostAsString(long cost) {
//        //StringHelper.localize()
//        if (cost == 0) return StringHelper.localize(Shopaholic.MODID + ".shop.free");
//        else return StringHelper.convertNumberToString(cost);
//    }
//
//    @Override
//    protected void drawTooltip(List<String> list, int x, int y) {
//        if (!purchased.isEmpty()) super.drawTooltip(list, x, y);
//        else super.drawTooltip(list, x, y);
//    }
//
//    public void updatePurchased(@Nonnull ItemStack stack, int amount) {
//        if (stack.isEmpty()) purchased = ItemStack.EMPTY;
//        else if (purchased.isEmpty() || !ItemStack.areItemsEqual(purchased, stack) || !ItemStack.areItemStackTagsEqual(purchased, stack)) purchased = stack.copy();
//        else purchased.grow(amount);
//    }
//
//    @Override
//    protected void mouseClicked(int x, int y, int mouseButton) throws IOException {
//        super.mouseClicked(x, y, mouseButton);
//        if (selectedButton == null) {
//            updatePurchased(ItemStack.EMPTY, 0);
//        }
//    }
//
//    public void scroll(int amount) {
//        setStart(start + amount);
//    }
}
