package uk.joshiejack.shopaholic.client.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.client.ShopaholicClientConfig;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.client.gui.button.DepartmentTabButton;
import uk.joshiejack.shopaholic.client.gui.button.GoldListingButton;
import uk.joshiejack.shopaholic.client.gui.button.ItemListingButton;
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
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ConstantConditions")
public class DepartmentScreen extends AbstractContainerScreen<DepartmentContainer> {
    public static final ResourceLocation EXTRA = new ResourceLocation(Shopaholic.MODID, "textures/gui/shop_extra.png");
    private static final ResourceLocation BACKGROUND = new ResourceLocation(Shopaholic.MODID, "textures/gui/shop.png");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private static final TranslationTextComponent FREE = new TranslationTextComponent("gui.shopaholic.shop.free");
    private static final TranslationTextComponent ERROR = new TranslationTextComponent("gui.shopaholic.shop.error");
    private static final Cache<Long, ITextComponent> COST_CACHE = CacheBuilder.newBuilder().build();
    private final Collection<Listing> contents;
    public final Stock stock;
    private Pair<Icon, Integer> purchased;
    private int listingCount;
    private int start;
    private int end;

    public DepartmentScreen(DepartmentContainer container, PlayerInventory inv) {
        super(container, inv, container.department.getLocalizedName(), BACKGROUND, 256, 256);
        this.purchased = Pair.of(ItemIcon.EMPTY, 0);
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
        super.renderBg(matrix, partialTicks, mouseX, mouseY);
        renderBackground(matrix);
        minecraft.getTextureManager().bind(BACKGROUND);
        int heightToUse = Math.max(listingCount, 3);
        if (heightToUse < 12) {
            blit(matrix, leftPos, topPos - 12 + (20 * heightToUse), 0, 228, imageWidth, 28);
            blit(matrix, leftPos, topPos, 0, 0, imageWidth, (20 * heightToUse) - 2);
        } else blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        //FancyFontRenderer.render(this, x + 20, topPos + 17, getShopName(), false);
        drawCoinage(matrix, leftPos, topPos + 19, Wallet.getActive().getBalance());
        drawPlayerInventory(matrix);
        if (purchased.getLeft() != ItemIcon.EMPTY) {
            purchased.getLeft().renderWithCount(minecraft, matrix, mouseX - leftPos - 8, mouseY - topPos - 8, purchased.getRight());
            //renderCountText(mouseX - leftPos - 8, mouseY - topPos - 8, purchased.getRight());
        }
    }

    private void renderCountText(int x, int y, int count) {
        if (count != 1) {
            MatrixStack matrixstack = new MatrixStack();
            String s = String.valueOf(count);
            matrixstack.translate(0.0D, 0.0D, getBlitOffset() + 200.0F);
            IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
            minecraft.font.drawInBatch(s, (float) (x + 19 - 2 - minecraft.font.width(s)), (float) (y + 6 + 3), 16777215, true, matrixstack.last().pose(), irendertypebuffer$impl, false, 0, 15728880);
            irendertypebuffer$impl.endBatch();
        }
    }

    private void drawCoinage(@Nonnull MatrixStack matrix, int x, int y, long gold) {
        String formatted = String.valueOf(formatter.format(gold));
        //FancyFontRenderer.render(this, x + 220, y, formatted, true);
        //GlStateManager.disableDepth();
        minecraft.getTextureManager().bind(EXTRA);
        blit(matrix, 224, 16, 244, 244, 12, 12);
        //GlStateManager.enableDepth();
    }

    private void drawPlayerInventory(@Nonnull MatrixStack matrix) {
        if (!ShopaholicClientConfig.enableInventoryView.get()) return;
        //FancyFontRenderer.render(this, leftPos + 250, topPos + 27, "BUYING", false);
        minecraft.getTextureManager().bind(EXTRA);
        blit(matrix, 240, 40, 0, 62, 100, 194);

        int x2 = 0, y2 = 0;
        boolean first = true;
        //FancyFontRenderer.render(this, leftPos + 240, topPos + 44, "Inventory", false);
        for (ItemStack stack : menu.target.getPlayer().inventory.items) {
            if (!stack.isEmpty()) {
                minecraft.getItemRenderer().blitOffset = 50;
                minecraft.getItemRenderer().renderGuiItem(stack, 253 + y2, 61 + x2 * 18);
                minecraft.getItemRenderer().renderGuiItemDecorations(font, stack, 253 + y2, 61 + x2 * 18);
                minecraft.getItemRenderer().blitOffset = 0;
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

    public void refresh() {
        setStart(start);
    }

    public void setStart(int i) {
        buttons.clear();

//        //Up Arrow
//        addButton(new ButtonArrow(this, -1, 225, 0, leftPos + 232, topPos + 60) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start != 0;
//            }
//        });

//        //Down Arrow
//        addButton(new ButtonArrow(this, +1, 242, 1, leftPos + 232, topPos + 210) {
//            @Override
//            protected void updateVisiblity() {
//                visible = start < end;
//            }
//        });

        end = contents.size() - 10;
        start = Math.max(0, Math.min(end, i));
        listingCount = 2;

        //Arrows are added, now add the items being sold
        int id = start;
        int position = 0;
        int pPosition = 0;
        Iterator<Listing> it = contents.iterator();
        while (it.hasNext() && position <= 180) {
            Listing listing = it.next();
            if (pPosition >= start && listing.canList(menu.target, stock)) {
                if (listing.getGoldCost(menu.target.getPlayer(), stock) < 0) {
                    addButton(new GoldListingButton(this, leftPos + 28, 38 + topPos + position, listing));
                    listingCount++;
                    position += 20;
                } else {
                    int add = addButton(listing, leftPos + 28, 38 + topPos + position, position);
                    listingCount = add > 0 ? listingCount + 1 : listingCount;
                    position += add;
                }

                id++;
            }

            pPosition++;
        }

        //if (buttons.size() == 2) //addButton(new ButtonOutOfStock(this, 3, leftPos + 28, 38 + topPos + position));
        //Tabs
        if (menu.shop != null && menu.shop.getDepartments().size() > 1) {
            int j = 0;
            for (Department department : menu.shop.getDepartments()) {
                if (department.getListings().stream().anyMatch(l -> l.canList(menu.target, stock))) {
                    addButton(new DepartmentTabButton(this, leftPos + 5, topPos + 38 + (j * 23), department));
                    j++;
                }
            }

            if (listingCount < (3 + menu.shop.getDepartments().size())) {
                listingCount = 3 + menu.shop.getDepartments().size();
            }
        }
    }

    @SuppressWarnings("unchecked")
    private int addButton(Listing listing, int left, int top, int space) {
        if (listing.getSubListing(stock).isGoldOnly()) {
            if (space + 20 <= 200) {
                addButton(new GoldListingButton(this, left, top, listing));
                return 20;
            }
        } else {
            if (listing.getSubListing(stock).getMaterials().size() == 1 && listing.getGoldCost(menu.target.getPlayer(), stock) == 0) {
                if (space + 20 <= 200) {
                    MaterialCost requirement = ((List<MaterialCost>) listing.getSubListing(stock).getMaterials()).get(0);
                    addButton(new ItemListingButton(this, left, top, listing, requirement));
                    return 20;
                }
            } else if (space + 20 <= 200) {
                //addButton(new ButtonListingBuilding(this, listing, id, left, top));
                return 20;
            }
        }

        return 0;
    }

    public ITextComponent getCostAsTextComponent(long cost) {
        try {
            return cost == 0 ? FREE :
                    COST_CACHE.get(cost, () -> new StringTextComponent(StringHelper.convertNumberToString(cost)));
        } catch (ExecutionException ex) {
            return ERROR;
        }
    }

    public void updatePurchased(@Nonnull Icon icon, int amount) {
        if (icon == ItemIcon.EMPTY) purchased = Pair.of(ItemIcon.EMPTY, 0);
        else if (purchased.getLeft() == ItemIcon.EMPTY || purchased.getLeft() != icon)
            purchased = Pair.of(icon, amount);
        else purchased = Pair.of(purchased.getLeft(), purchased.getRight() + amount);
    }

    @Override
    public boolean mouseClicked(double x, double y, int mouseButton) {
        boolean ret = super.mouseClicked(x, y, mouseButton);
        if (!ret) {
            updatePurchased(ItemIcon.EMPTY, 0);
        }
        //TODO?if (selectedButton == null) {
        //updatePurchased(ItemStack.EMPTY, 0);
        //}

        return ret;
    }

    public void scroll(int amount) {
        setStart(start + amount);
    }
}
