package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import uk.joshiejack.penguinlib.client.gui.widget.AbstractButton;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.client.ShopaholicClient;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;

import javax.annotation.Nonnull;

public class DropItemButton extends AbstractButton<DepartmentScreen> {
    public DropItemButton(DepartmentScreen screen) {
        super(screen, 0, 0, screen.getMinecraft().getWindow().getScreenWidth(), screen.getMinecraft().getWindow().getScreenHeight(), ShopaholicClient.EMPTY_STRING,
                (btn) -> {
                    screen.updatePurchased(ItemIcon.EMPTY, 0);
                },
                (btn, mtx, mX, mY) -> {
                });
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
    }
}
