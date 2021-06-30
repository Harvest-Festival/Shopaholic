package uk.joshiejack.shopaholic.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.Set;

public abstract class AbstractSyncSoldItemList extends PenguinPacket {
    private ListNBT listNBT;

    public AbstractSyncSoldItemList() {}
    public AbstractSyncSoldItemList(Set<Shipping.SoldItem> set) {
        listNBT = Shipping.writeHolderCollection(set);
    }

    public void encode(PacketBuffer to) {
        CompoundNBT tag = new CompoundNBT();
        tag.put("list", listNBT);
        to.writeNbt(tag);
    }

    public void decode(PacketBuffer from) {
        CompoundNBT tag = from.readNbt();
        assert tag != null;
        listNBT = tag.getList("list", 10);
    }

    @Override
    public void handle(PlayerEntity player) {
        handle(listNBT);
    }

    protected abstract void handle(ListNBT list);
}
