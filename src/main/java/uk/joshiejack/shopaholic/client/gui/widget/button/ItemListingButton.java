package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.MaterialCost;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class ItemListingButton extends AbstractListingButton {
    private final MaterialCost requirement;
    private final Icon icon;

    public ItemListingButton(DepartmentScreen screen, int x, int y, Listing listing) {
        super(screen, x, y, listing);
        this.requirement = listing.getSubListing(screen.stock).getMaterials().get(0);
        this.icon = requirement.getIcon();
    }

    @Override
    protected void drawForeground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, boolean hovered, int color) {
        sublisting.getIcon().render(mc, matrix, x + 2, y + 1);
        drawString(matrix, mc.font, getMessage(), x + 20, y + (height - 8) / 2, color);
        icon.renderWithCount(mc, matrix, x + 180, y + 1, requirement.getCost());
        boolean subTooltipHovered = mouseX >= x + 178 && mouseY >= y && mouseX < x + 198 && mouseY < y + 16;
        if (subTooltipHovered)
            screen.addFuture(() -> GuiUtils.drawHoveringText(matrix, requirement.getIcon().getTooltipLines(mc.player), mouseX, mouseY, screen.width, screen.height, 200, screen.getMinecraft().font));
        if (subTooltipHovered)
            this.subTooltipHovered = true;
    }
}