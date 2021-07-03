package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.network.AbstractPacketSyncDepartment;
import uk.joshiejack.shopaholic.shop.Listing;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncStockLevelPacket extends AbstractPacketSyncDepartment {
    private Listing listing;
    private int stock;

    public SyncStockLevelPacket() {}
    public SyncStockLevelPacket(Listing listing, int stock) {
        super(listing.getDepartment());
        this.listing = listing;
        this.stock = stock;
    }

    @Override
    public void encode(PacketBuffer to) {
        super.encode(to);
        to.writeUtf(listing.id());
        to.writeVarInt(stock);
    }

    @Override
    public void decode(PacketBuffer from) {
        super.decode(from);
        listing = department.getListingByID(from.readUtf());
        stock = from.readVarInt();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientPacket() {
        department.getStockLevels(Minecraft.getInstance().level).setStockLevel(listing, stock);
    }
}
