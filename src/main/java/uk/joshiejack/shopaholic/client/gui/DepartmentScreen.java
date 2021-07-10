package uk.joshiejack.shopaholic.client.gui;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.client.PenguinClient;
import uk.joshiejack.penguinlib.client.gui.AbstractContainerScreen;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.client.ShopaholicClientConfig;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.client.gui.widget.button.*;
import uk.joshiejack.shopaholic.inventory.DepartmentContainer;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

import javax.annotation.Nonnull;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

@SuppressWarnings("ConstantConditions")
@OnlyIn(Dist.CLIENT)
public class DepartmentScreen extends AbstractContainerScreen<DepartmentContainer> {
    public static final ResourceLocation EXTRA = new ResourceLocation(Shopaholic.MODID, "textures/gui/shop_extra.png");
    private static final DecimalFormat formatter = new DecimalFormat("#,###");
    private static final TranslationTextComponent FREE = new TranslationTextComponent("gui.shopaholic.shop.free");
    private static final TranslationTextComponent ERROR = new TranslationTextComponent("gui.shopaholic.shop.error");
    private static final Cache<Long, ITextComponent> COST_CACHE = CacheBuilder.newBuilder().build();
    private static final Object2IntMap<Department> SCROLL_POS = new Object2IntOpenHashMap<>();
    private Collection<Listing> contents;
    public Stock stock;
    private Pair<Icon, Integer> purchased;
    private int listingCount;
    private int end;

    public DepartmentScreen(DepartmentContainer container, PlayerInventory inv) {
        super(container, inv, container.department.getLocalizedName(), container.department.getShop().getBackground(), 256, 256);
    }

    @Override
    public void init() {
        super.init();
        setStart(getStartPos()); //Reload
    }

    private ITextComponent getShopName() {
        return menu.shop != null ? menu.shop.getLocalizedName() : menu.shop.getLocalizedName();
    }

    @Override
    public void renderBg(@Nonnull MatrixStack matrix, float partialTicks, int mouseX, int mouseY) {
        super.renderBg(matrix, partialTicks, mouseX, mouseY);
        renderBackground(matrix);
        minecraft.getTextureManager().bind(background);
        int heightToUse = Math.max(listingCount, 3);
        if (heightToUse < 12) {
            blit(matrix, leftPos, topPos - 12 + (20 * heightToUse), 0, 228, imageWidth, 28);
            blit(matrix, leftPos, topPos, 0, 0, imageWidth, (20 * heightToUse) - 2);
        } else blit(matrix, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        matrix.pushPose();
        int width = PenguinClient.FANCY_FONT.get().width(getShopName());
        boolean larger = width <= 90;
        boolean smaller = width >= 140;
        float scale = larger ? 1.5F : smaller ? 0.75F : 1F;
        matrix.scale(scale, scale, scale);
        PenguinClient.FANCY_FONT.get().draw(matrix, getShopName(), 22 / scale, (17 / scale) + (!larger && !smaller ? 3 : smaller ? 6 : 0), 0xF1B81F);
        matrix.popPose();
        drawCoinage(matrix, Wallet.getActive().getBalance());
        drawPlayerInventory(matrix);
        if (purchased.getLeft() != ItemIcon.EMPTY) {
            float blit = minecraft.getItemRenderer().blitOffset;
            minecraft.getItemRenderer().blitOffset = 100F;
            int blit2 = minecraft.gui.getBlitOffset();
            minecraft.gui.setBlitOffset(110);
            purchased.getLeft().renderWithCount(minecraft, matrix, mouseX - leftPos - 8, mouseY - topPos - 8, purchased.getRight());
            minecraft.getItemRenderer().blitOffset = blit;
            minecraft.gui.setBlitOffset(blit2);
        }
    }

    private void drawCoinage(@Nonnull MatrixStack matrix, long gold) {
        String formatted = formatter.format(gold);
        int width = PenguinClient.FANCY_FONT.get().width(formatted);
        PenguinClient.FANCY_FONT.get().draw(matrix, formatted, 220 - width, 20, 0xFFF5CB5C);
        minecraft.getTextureManager().bind(EXTRA);
        blit(matrix, 224, 17, 244, 244, 12, 12);
    }

    private void drawPlayerInventory(@Nonnull MatrixStack matrix) {
        if (!ShopaholicClientConfig.enableInventoryView.get()) return;
        minecraft.getTextureManager().bind(menu.shop.getExtra());
        blit(matrix, 240, 40, 0, 62, 100, 194);

        int x2 = 0, y2 = 0;
        boolean first = true;
        PenguinClient.FANCY_FONT.get().draw(matrix, "Inventory", 260, 44, 0xFFF5CB5C);
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
        setStart(getStartPos());
    }

    private int getStartPos() {
        return SCROLL_POS.getOrDefault(menu.department, 0);
    }

    public void setStart(int i) {
        buttons.clear();
        children.clear();

        purchased = Pair.of(ItemIcon.EMPTY, 0);
        stock = menu.department.getStockLevels(minecraft.level);
        contents = Lists.newArrayList();
        for (Listing listing : menu.department.getListings()) {
            if (listing.canList(menu.target, stock)) {
                contents.add(listing);
            }
        }

        //Up Arrow
        addButton(new NavigationButton(this, leftPos + 232, topPos + 60, -1, 225, new TranslationTextComponent("button.penguinlib.previous")) {
            @Override
            protected void updateVisibility() {
                visible = getStartPos() != 0;
            }
        });

        //Down Arrow
        addButton(new NavigationButton(this, leftPos + 232, topPos + 210, +1, 242, new TranslationTextComponent("button.penguinlib.next")) {
            @Override
            protected void updateVisibility() {
                visible = getStartPos() < end;
            }
        });

        end = contents.size() - 10;
        SCROLL_POS.put(menu.department, Math.max(0, Math.min(end, i)));
        listingCount = 2;

        //Arrows are added, now add the items being sold
        int position = 0;
        int pPosition = 0;
        int start = getStartPos();
        Iterator<Listing> it = contents.iterator();
        while (it.hasNext() && position <= 180) {
            Listing listing = it.next();
            if (pPosition >= start && listing.canList(menu.target, stock)) {
                if (listing.getGoldCost(menu.target.getPlayer(), stock) < 0) {
                    addButton(new ItemListingButton(this, leftPos + 28, 38 + topPos + position, listing));
                    listingCount++;
                    position += 20;
                } else {
                    int add = addButton(listing, leftPos + 28, 38 + topPos + position, position);
                    listingCount = add > 0 ? listingCount + 1 : listingCount;
                    position += add;
                }
            }

            pPosition++;
        }

        if (listingCount == 2) addButton(new OutOfStockLabel(leftPos + 28, 38 + topPos + position, menu.department));
        //Tabs
        if (menu.shop != null && menu.shop.getDepartments().size() > 1) {
            int j = 0;
            for (Department department : menu.shop.getDepartments()) {
                if (department.isValidFor(menu.target, Condition.CheckType.SHOP_LISTING)) {
                    if (department.getListings().stream().anyMatch(l -> l.canList(menu.target, stock))) {
                        addButton(new DepartmentTabButton(this, leftPos + 5, topPos + 38 + (j * 23), department));
                        j++;
                    }
                }
            }

            if (listingCount < (3 + menu.shop.getDepartments().size())) {
                listingCount = 3 + menu.shop.getDepartments().size();
            }
        }

        //addButton(new DropItemButton(this));
    }

    private int addButton(Listing listing, int left, int top, int space) {
        if (listing.getSubListing(stock).isGoldOnly()) {
            if (space + 20 <= 200) {
                addButton(new GoldListingButton(this, left, top, listing));
                return 20;
            }
        } else {
            if (listing.getSubListing(stock).getMaterials().size() == 1 && listing.getGoldCost(menu.target.getPlayer(), stock) == 0) {
                if (space + 20 <= 200) {
                    addButton(new ItemListingButton(this, left, top, listing));
                    return 20;
                }
            } else if (space + 20 <= 200) {
                addButton(new ComboListingButton(this, left, top, listing));
                return 20;
            }
        }

        return 0;
    }

    public static ITextComponent getCostAsTextComponent(long cost) {
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
        //Skip the container below
        for (IGuiEventListener iguieventlistener : this.children()) {
            if (iguieventlistener.mouseClicked(x, y, mouseButton)) {
                setFocused(iguieventlistener);
                if (mouseButton == 0) {
                    this.setDragging(true);
                }

                return true;
            }
        }

        updatePurchased(ItemIcon.EMPTY, 0);
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dir) {
        scroll((int) -dir);
        return super.mouseScrolled(mouseX, mouseY, dir);
    }

    public void scroll(int amount) {
        setStart(getStartPos() + amount);
        init(); //reload?
    }

    @Override
    public int getXSize() {
        return 512;
    }
}