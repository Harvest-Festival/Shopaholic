package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.MaterialCost;

import javax.annotation.Nonnull;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ComboListingButton extends GoldListingButton {
    private final List<MaterialCost> icons;
    private static final float SCALE = 0.75F;

    public ComboListingButton(DepartmentScreen screen, int x, int y, Listing listing) {
        super(screen, x, y, listing);
        this.icons = sublisting.getMaterials();
    }

    @Override
    protected void drawForeground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, boolean hovered, int color) {
        super.drawForeground(matrix, mouseX, mouseY, hovered, color);
        subTooltipHovered = false;
        for (int i = 0; i < icons.size(); i++) {
            MaterialCost material = icons.get(i);
            int x = this.x + (sublisting.getGold() == 0 ? 48 : 0) + 135 - (i * 16);
            int y = this.y + 3;
            //TODO:
            boolean subTooltipHovered = mouseX >= x && mouseY >= y && mouseX < x + 14 && mouseY < y + 14;
            if (subTooltipHovered)
                GuiUtils.drawHoveringText(matrix, material.getIcon().getTooltipLines(mc.player), mouseX, mouseY, screen.width, screen.height, 200, screen.getMinecraft().font);
            if (subTooltipHovered)
                this.subTooltipHovered = true;
            RenderSystem.pushMatrix();
            RenderSystem.scalef(SCALE, SCALE, SCALE);
            material.getIcon().renderWithCount(mc, matrix, (int) (x / SCALE), (int) (y / SCALE), material.getCost());
            RenderSystem.popMatrix();
        }
    }
}