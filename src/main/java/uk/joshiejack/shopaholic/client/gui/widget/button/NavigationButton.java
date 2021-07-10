package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.widget.AbstractButton;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public abstract class NavigationButton extends AbstractButton<DepartmentScreen> {
    private final int textureX;

    public NavigationButton(DepartmentScreen screen, int x, int y, int scrollAmount, int textureX, ITextComponent name) {
        super(screen, x, y, 14, 12, name,
                (btn) -> screen.scroll(Screen.hasShiftDown() ? scrollAmount * 10 : scrollAmount),
                (btn, mtx, mX, mY) -> {
                    GuiUtils.drawHoveringText(mtx, Lists.newArrayList(btn.getMessage()), mX, mY, screen.width, screen.height, 200, screen.getMinecraft().font);
                });
        this.textureX = textureX;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        updateVisibility();
        if (visible) {
            mc.getTextureManager().bind(screen.getMenu().shop.getExtra());
            blit(matrix, x, y, textureX, (hovered ? 12 : 0), width, height);
        }
    }

    protected abstract void updateVisibility();
}