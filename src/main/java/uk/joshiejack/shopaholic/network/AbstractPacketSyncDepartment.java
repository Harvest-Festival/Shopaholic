package uk.joshiejack.shopaholic.network;

import net.minecraft.network.PacketBuffer;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.shopaholic.shop.Department;

public abstract class AbstractPacketSyncDepartment extends PenguinPacket {
    protected Department department;

    public AbstractPacketSyncDepartment() {}
    public AbstractPacketSyncDepartment(Department department) {
        this.department = department;
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeUtf(department.id());
    }

    @Override
    public void decode(PacketBuffer buf) {
        department = Department.REGISTRY.get(buf.readUtf(32767));
    }
}
