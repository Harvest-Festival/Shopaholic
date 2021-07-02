package uk.joshiejack.shopaholic.network.shipping;

import net.minecraft.nbt.ListNBT;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.network.AbstractSyncSoldItemList;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.Set;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncLastSoldPacket extends AbstractSyncSoldItemList {
    public SyncLastSoldPacket() {}
    public SyncLastSoldPacket(Set<Shipping.SoldItem> set) {
        super(set);
    }

    @Override
    public void handle(ListNBT listNBT) {
        Shipping.readHolderCollection(listNBT, Shipped.getShippingLog());
    }
}
