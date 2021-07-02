package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetPlayerShopSeed extends PenguinPacket {
    private int seed;

    public SetPlayerShopSeed() { }
    public SetPlayerShopSeed(int value) {
        this.seed = value;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeVarInt(seed);
    }

    @Override
    public void decode(PacketBuffer from) {
        seed = from.readVarInt();
    }

    @Override
    public void handle(PlayerEntity player) {
        player.getPersistentData().putInt("ShopaholicSeed", seed);
    }
}
