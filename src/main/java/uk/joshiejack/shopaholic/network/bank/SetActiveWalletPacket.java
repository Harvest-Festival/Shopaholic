package uk.joshiejack.shopaholic.network.bank;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.bank.Wallet;
import uk.joshiejack.shopaholic.network.AbstractSetPlayerNBTPacket;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SetActiveWalletPacket extends AbstractSetPlayerNBTPacket {
    private boolean shared;

    public SetActiveWalletPacket() { super("ShopaholicSettings");}
    public SetActiveWalletPacket(boolean shared) {
        super("ShopaholicSettings");
        this.shared = shared;
    }

    @Override
    public void encode(PacketBuffer pb) {
        pb.writeBoolean(shared);
    }

    public void decode(PacketBuffer pb) {
        shared = pb.readBoolean();
    }

    @Override
    public void setData(CompoundNBT tag) {
        tag.putBoolean("SharedWallet", shared);
        Wallet.setActive(shared ? WalletType.SHARED : WalletType.PERSONAL);
    }
}
