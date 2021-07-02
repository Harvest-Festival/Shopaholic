package uk.joshiejack.shopaholic.network.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.network.AbstractSetPlayerNBTPacket;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_SERVER)
public class SwitchWalletPacket extends AbstractSetPlayerNBTPacket {
    private boolean shared;

    public SwitchWalletPacket() { super("ShopaholicSettings");}
    public SwitchWalletPacket(boolean shared) {
        super("ShopaholicSettings");
        this.shared = shared;
    }

    @Override
    public void encode(PacketBuffer buf) {
        buf.writeBoolean(shared);
    }

    @Override
    public void decode(PacketBuffer buf) {
        shared = buf.readBoolean();
    }

    @Override
    public void handle(PlayerEntity player) {
        super.handle(player);
        PenguinNetwork.sendToClient(new SetActiveWalletPacket(shared), (ServerPlayerEntity) player);
    }

    @Override
    public void setData(CompoundNBT tag) {
        tag.putBoolean("SharedWallet", shared); //Set this data, YESSSSSSSSSSSSS
    }
}
