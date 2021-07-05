package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.Widget;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.shop.Department;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class OutOfStockLabel extends Widget {
    public OutOfStockLabel(int x, int y, Department department) {
        super(x, y, 200, 18, department.getOutOfStockText());
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getInstance();
        mc.getTextureManager().bind(DepartmentScreen.EXTRA);
        blit(matrix, x, y, 0, 0, width / 2, height);
        blit(matrix, x + width / 2, y, 200 - width / 2, 0, width / 2, height);
        drawString(matrix, mc.font, getMessage(), x + 10, y + (height - 8) / 2, 14737632);
    }
}