package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.network.AbstractPacketSyncDepartment;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncStockLevelsPacket extends AbstractPacketSyncDepartment {
    private CompoundNBT data;

    public SyncStockLevelsPacket() {}
    public SyncStockLevelsPacket(Department department, Stock stock) {
        super(department);
        this.data = stock.serializeNBT();
    }

    @Override
    public void encode(PacketBuffer to) {
        super.encode(to);
        to.writeNbt(data);
    }

    @Override
    public void decode(PacketBuffer from) {
        super.decode(from);
        data = from.readNbt();
    }

    @Override
    public void handle(PlayerEntity player) {
        department.getStockLevels().deserializeNBT(data);
    }
}