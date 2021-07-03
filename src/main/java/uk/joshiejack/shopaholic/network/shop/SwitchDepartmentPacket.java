package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.inventory.DepartmentContainer;
import uk.joshiejack.shopaholic.shop.Department;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_SERVER)
public class SwitchDepartmentPacket extends PenguinPacket {
    private Department department;

    public SwitchDepartmentPacket() { }
    public SwitchDepartmentPacket(Department department) {
        this.department = department;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeUtf(department.id());
    }

    @Override
    public void decode(PacketBuffer from) {
        department = Department.REGISTRY.get(from.readUtf());
    }

    @Override
    public void handle(PlayerEntity player) {
        if (player.containerMenu instanceof DepartmentContainer)
            department.open(((DepartmentContainer)player.containerMenu).target, false);
    }
}
