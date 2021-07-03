package uk.joshiejack.shopaholic.client.gui.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.shop.Listing;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class GoldListingButton extends AbstractListingButton {

    public GoldListingButton(DepartmentScreen screen, int x, int y, Listing listing) {
        super(screen, x, y, listing);
    }

    @Override
    protected void drawForeground(@Nonnull MatrixStack matrix, boolean hovered, int color) {
        sublisting.getIcon().render(mc, matrix, x + 2, y + 1);
        drawString(matrix, mc.font, getMessage(), x + 20, y + (height - 8) / 2, color);
        //GlStateManager.color(1.0F, 1.0F, 1.0F);
        //Draw the cost
        long goldCost = listing.getGoldCost(screen.getMenu().target.getPlayer(), screen.stock);
        ITextComponent cost = screen.getCostAsTextComponent(goldCost);
        if (goldCost != 0) {
            int width = mc.font.width(cost);
            drawString(matrix, mc.font, cost, x + 180 - width, y + (height - 8) / 2, color);
            mc.getTextureManager().bind(DepartmentScreen.EXTRA);
            blit(matrix, x + 184, (y + (height - 8) / 2) - 2, 244, 244, 12, 12);
        } else {
            int width = mc.font.width(cost);
            drawString(matrix, mc.font, cost, x + 194 - width, y + (height - 8) / 2, color);
        }
    }
}