package uk.joshiejack.shopaholic.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.penguinlib.network.PenguinPacket;

public class SetPlayerSeedPacket extends PenguinPacket {
    private int seed;

    public SetPlayerSeedPacket() { }
    public SetPlayerSeedPacket(int value) {
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
