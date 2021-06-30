package uk.joshiejack.shopaholic.network;

import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.client.Shipped;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.Set;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncSoldPacket extends AbstractSyncSoldItemList {
    public SyncSoldPacket() {}
    public SyncSoldPacket(Set<Shipping.SoldItem> set) {
        super(set);
    }

    @Override
    public void handle(ListNBT listNBT) {
        Shipped.clearCountCache();
        Shipping.readHolderCollection(listNBT, Shipped.getSold());
    }
}
