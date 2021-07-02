package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.network.AbstractPacketSyncDepartment;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetStockedItemPacket extends AbstractPacketSyncDepartment {
    private Listing listing;
    private int stockID;

    public SetStockedItemPacket() { }
    public SetStockedItemPacket(Department department, Listing listing, int stockID) {
        super(department);
        this.listing = listing;
        this.stockID = stockID;
    }

    @Override
    public void encode(PacketBuffer buf) {
        super.encode(buf);
        buf.writeUtf(listing.getID());
        buf.writeInt(stockID);
    }

    @Override
    public void decode(PacketBuffer buf) {
        super.decode(buf);
        listing = department.getListingByID(buf.readUtf());
        stockID = buf.readInt();
    }

    @Override
    public void handle(PlayerEntity player) {
        department.getStockLevels().setStockedItem(listing, stockID);
    }
}