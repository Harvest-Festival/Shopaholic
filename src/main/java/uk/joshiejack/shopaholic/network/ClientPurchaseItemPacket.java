package uk.joshiejack.shopaholic.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;
import uk.joshiejack.shopaholic.shop.ShopHelper;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class ClientPurchaseItemPacket extends AbstractPurchaseItemPacket {
    public ClientPurchaseItemPacket() {}
    public ClientPurchaseItemPacket(Department department, Listing listing, int amount) {
        super(department, listing, amount);
    }

    @Override
    public void handle(PlayerEntity player) {
        for (int i = 0; i < amount; i++) {
            listing.purchase(player);
        }

        ShopHelper.resetGUI();
    }
}
