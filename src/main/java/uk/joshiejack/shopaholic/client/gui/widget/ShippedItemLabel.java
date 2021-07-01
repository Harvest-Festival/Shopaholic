package uk.joshiejack.shopaholic.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.book.Book;
import uk.joshiejack.shopaholic.client.gui.page.PageEconomyManager;

import javax.annotation.Nonnull;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class ShippedItemLabel extends Widget {
    private final Book book;
    private final ItemStack icon;
    private final long value;

    public ShippedItemLabel(Book book, int x, int y, ItemStack stack, long value) {
        super(x, y, 16, 16, PageEconomyManager.EMPTY_STRING);
        this.book = book;
        this.width = 16;
        this.height = 16;
        this.icon = stack;
        this.value = value;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getItemRenderer().renderGuiItem(icon, x, y);
        float scale = 0.75F;
        int xPos = (int) ((x + 3) / scale);
        int yPos = (int) ((y + 3) / scale);
        RenderSystem.pushMatrix();
        RenderSystem.scalef(scale, scale, scale);
        mc.getItemRenderer().renderGuiItemDecorations(mc.font, icon, xPos, yPos);
        RenderSystem.popMatrix();

        boolean hovered = clicked(mouseX, mouseY);
        if (hovered) {
            List<ITextComponent> tooltip = new ArrayList<>();
            tooltip.add(icon.getHoverName());
            tooltip.add(new TranslationTextComponent(NumberFormat.getNumberInstance(Locale.ENGLISH).format(value) + "G").withStyle(TextFormatting.GOLD));
            GuiUtils.drawHoveringText(matrix, tooltip, mouseX, mouseY, book.width, book.height, 200, book.minecraft().font);
        }
    }
}