package uk.joshiejack.shopaholic.client.gui.widget.button;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.GuiUtils;
import uk.joshiejack.penguinlib.client.gui.widget.AbstractButton;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.shopaholic.client.ShopaholicClient;
import uk.joshiejack.shopaholic.client.gui.DepartmentScreen;
import uk.joshiejack.shopaholic.network.shop.SwitchDepartmentPacket;
import uk.joshiejack.shopaholic.shop.Department;

import javax.annotation.Nonnull;

@OnlyIn(Dist.CLIENT)
public class DepartmentTabButton extends AbstractButton<DepartmentScreen> {
    private final Department department;

    public DepartmentTabButton(DepartmentScreen screen, int x, int y, Department department) {
        super(screen, x, y, 21, 21, ShopaholicClient.EMPTY_STRING,
                (btn) -> {
                    //Shop.get(department).setLast(department);
                    PenguinNetwork.sendToServer(new SwitchDepartmentPacket(department));
                },
                (btn, mtx, mX, mY) -> {
                    GuiUtils.drawHoveringText(mtx, Lists.newArrayList(department.getLocalizedName()), mX, mY, screen.width, screen.height, 200, screen.getMinecraft().font);
                });
        this.department = department;
    }

    @Override
    protected void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks, boolean hovered) {
        mc.getTextureManager().bind(screen.getMenu().shop.getExtra());
        blit(matrix, x, y, 107, 59 + (hovered ? 22 : 0), 21, 22);
        department.getIcon().render(mc, matrix, x + 4, y + 3);
    }
}