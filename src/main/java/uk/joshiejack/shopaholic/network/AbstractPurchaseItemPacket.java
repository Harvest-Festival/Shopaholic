package uk.joshiejack.shopaholic.network;

import net.minecraft.network.PacketBuffer;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

public abstract class AbstractPurchaseItemPacket extends AbstractPacketSyncDepartment {
    protected Listing listing;
    protected int amount;

    public AbstractPurchaseItemPacket() {}
    public AbstractPurchaseItemPacket(Department department, Listing listing, int amount) {
        super(department);
        this.listing = listing;
        this.amount = amount;
    }

    @Override
    public void encode(PacketBuffer buf) {
        super.encode(buf);
        buf.writeUtf(listing.id());
        buf.writeInt(amount);
    }

    @Override
    public void decode(PacketBuffer buf) {
        super.decode(buf);
        listing = department.getListingByID(buf.readUtf());
        amount =  buf.readInt();
    }
}