package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.inventory.DepartmentContainer;
import uk.joshiejack.shopaholic.network.AbstractPacketSyncDepartment;
import uk.joshiejack.shopaholic.shop.Department;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_SERVER)
public class SwitchDepartmentPacket extends AbstractPacketSyncDepartment {
    public SwitchDepartmentPacket() { }
    public SwitchDepartmentPacket(Department department) {
        super(department);
    }

    @Override
    public void handle(PlayerEntity player) {
        if (player.containerMenu instanceof DepartmentContainer)
            department.open(((DepartmentContainer)player.containerMenu).target, false);
    }
}
