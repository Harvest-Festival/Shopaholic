package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.network.AbstractPurchaseItemPacket;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class ClientPurchaseItemPacket extends AbstractPurchaseItemPacket {
    public ClientPurchaseItemPacket() {}
    public ClientPurchaseItemPacket(Department department, Listing listing, int amount) {
        super(department, listing, amount);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleClientPacket() {
        ClientPlayerEntity player = Minecraft.getInstance().player;;
        for (int i = 0; i < amount; i++) {
            listing.purchase(player);
        }
    }
}
