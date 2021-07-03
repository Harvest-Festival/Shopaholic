package uk.joshiejack.shopaholic.client.gui;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.inventory.DepartmentContainer;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.MaterialCost;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("ConstantConditions")
public class DepartmentScreen extends AbstractContainerScreen<DepartmentContainer> {
    public static final ResourceLocation EXTRA =  new ResourceLocation(Shopaholic.MODID, "textures/gui/shop_extra.png");
    private static final ResourceLocation BACKGROUND =  new ResourceLocation(Shopaholic.MODID, "textures/gui/shop.png");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private final Collection<Listing> contents;
    public final Stock stock;
    private ItemStack purchased;
    private int purcasableCount;
    private int start;
    private int end;

    public DepartmentScreen(DepartmentContainer container, PlayerInventory inv) {
        super(container, inv, container.department.getLocalizedName(), BACKGROUND, 256, 256);
        this.purchased = ItemStack.EMPTY;
        this.stock = container.department.getStockLevels();
        this.contents = Lists.newArrayList();
        for (Listing listing : ImmutableList.copyOf(container.department.getListings())) {
            if (listing.canList(container.target, stock)) {
                contents.add(listing);
            }
        }
    }

    @Override
    public void init() {
        super.init();
        setStart(0); //Reload
    }

    private ITextComponent getShopName() {
        return menu.shop != null ? menu.shop.getLocalizedName() : menu.shop.getLocalizedName();
    }

    @Override
    public void renderBg(@Nonnull MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        minecraft.getTextureManager().bind(BACKGROUND);
        int heightToUse = Math.max(purcasableCount, 3);
        if (heightToUse < 12) {
            blit(matrix, leftPos, topPos - 12 + (20 * heightToUse), 0, 228, imageWidth, 28);
            blit(matrix, leftPos, topPos, 0, 0, imageWidth, (20 * heightToUse) - 2);
        } else blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int p_230451_2_, int p_230451_3_) {
        //FancyFontRenderer.render(this, x + 20, topPos + 17, getShopName(), false);
        drawCoinage(matrix, leftPos, topPos + 19, Wallet.getActive().getBalance());
        drawPlayerInventory(matrix);
    }

    private void drawCoinage(@Nonnull MatrixStack matrix, int x, int y, long gold) {
        String formatted = String.valueOf(formatter.format(gold));
        //FancyFontRenderer.render(this, x + 220, y, formatted, true);
        //GlStateManager.disableDepth();
        minecraft.getTextureManager().bind(EXTRA);
        blit(matrix, (x + 224), y - 1, 244, 244, 12, 12);
        //GlStateManager.enableDepth();
    }

    private void drawPlayerInventory(@Nonnull MatrixStack matrix) {
        //FancyFontRenderer.render(this, leftPos + 250, topPos + 27, "BUYING", false);
        minecraft.getTextureManager().bind(EXTRA);
        blit(matrix, leftPos + 240, topPos + 40, 0, 62, 100, 194);

        int x2 = 0, y2 = 0;
        boolean first = true;
        //FancyFontRenderer.render(this, leftPos + 240, topPos + 44, "Inventory", false);
        for (ItemStack stack: menu.target.getPlayer().inventory.items) {
            if (!stack.isEmpty()) {
                minecraft.getItemRenderer().renderGuiItem(stack, leftPos + 253 + y2, topPos + 61 + x2 * 18);
            }

            x2++;
            if (x2 >= 9) {
                x2 = 0;
                if (first) {
                    y2 += 22;
                    first = false;
                } else {
                    y2 += 18;
                }
            }
        }
    }

//    @Override
//    public void drawForeground(int x, int y) {
//        if (!purchased.isEmpty()) {
//            StackRenderHelper.drawStack(purchased, x - leftPos, y - topPos, 1F);
//        }
//    }

    public void refresh() {
        setStart(start);
    }

    public void setStart(int i) {
        buttons.clear();

//        //Up Arrow
//        buttons.add(new ButtonArrow(this, -1, 225, 0, leftPos + 232, topPos + 60) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start != 0;
//            }
//        });

//        //Down Arrow
//        buttons.add(new ButtonArrow(this, +1, 242, 1, leftPos + 232, topPos + 210) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start < end;
//            }
//        });

        end = contents.size() - 10;
        start = Math.max(0, Math.min(end, i));
        purcasableCount = 2;

        //Arrows are added, now add the items being sold
        int id = start;
        int position = 0;
        int pPosition = 0;
        Iterator<Listing> it = contents.iterator();
        while (it.hasNext() && position <= 180) {
            Listing purchasable = it.next();
            if (pPosition >= start && purchasable.canList(menu.target, stock)) {
                if (purchasable.getGoldCost(menu.target.getPlayer(), stock) < 0) {
                    ///buttons.add(new ButtonListing(this, purchasable, id + 2, leftPos + 28, 38 + topPos + position));
                    purcasableCount++;
                    position += 20;
                } else {
                    int add = addButton(purchasable, id + 2, leftPos + 28, 38 + topPos + position, position);
                    purcasableCount = add > 0 ? purcasableCount + 1 : purcasableCount;
                    position += add;
                }

                id++;
            }

            pPosition++;
        }

        if (buttons.size() == 2) //buttons.add(new ButtonOutOfStock(this, 3, leftPos + 28, 38 + topPos + position));

        //Tabs
        if (menu.shop != null && menu.shop.getDepartments().size() > 1) {
            int j = 0;
            for (Department shop : ImmutableList.copyOf(menu.shop.getDepartments()).reverse()) {
                if (shop.getListings().stream().anyMatch(l -> l.canList(menu.target, stock))) {
                    //buttons.add(new ButtonShopTab(this, shop, buttons.size(), leftPos + 5, topPos + 38 + (j * 23)));
                    j++;
                }
            }

            if (purcasableCount < (3 + menu.shop.getDepartments().size())) {
                purcasableCount = 3 + menu.shop.getDepartments().size();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int addButton(Listing listing, int id, int left, int top, int space) {
        if (listing.getSubListing(stock).isGoldOnly()) {
            if (space + 20 <= 200) {
                //buttons.add(new ButtonListing(this, listing, id, left, top));
                return 20;
            }
        } else {
            if (listing.getSubListing(stock).getMaterials().size() == 1 && listing.getGoldCost(menu.target.getPlayer(), stock) == 0) {
                if (space + 20 <= 200) {
                    MaterialCost requirement = ((List<MaterialCost>) listing.getSubListing(stock).getMaterials()).get(0);
                    //buttons.add(new ButtonListingItem(requirement, this, listing, id, left, top));
                    return 20;
                }
            } else if (space + 20 <= 200) {
                //buttons.add(new ButtonListingBuilding(this, listing, id, left, top));
                return 20;
            }
        }

        return 0;
    }

    public String getCostAsString(long cost) {
        if (cost == 0) return StringHelper.localize(Shopaholic.MODID + ".shop.free").getString();
        else return StringHelper.convertNumberToString(cost);
    }

//    @Override
//    protected void drawTooltip(List<String> list, int x, int y) {
//        if (!purchased.isEmpty()) super.drawTooltip(list, x, y);
//        else super.drawTooltip(list, x, y);
//    }

    public void updatePurchased(@Nonnull ItemStack stack, int amount) {
        if (stack.isEmpty()) purchased = ItemStack.EMPTY;
        else if (purchased.isEmpty() || !ItemStack.isSame(purchased, stack) || !ItemStack.tagMatches(purchased, stack)) purchased = stack.copy();
        else purchased.grow(amount);
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        boolean ret = super.mouseClicked(x, y, mouseButton);
        //TODO?if (selectedButton == null) {
            //updatePurchased(ItemStack.EMPTY, 0);
        //}

        return ret;
    }

    public void scroll(int amount) {
        setStart(start + amount);
    }
}
