package uk.joshiejack.shopaholic.network.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.client.bank.Wallet;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class SyncGoldPacket extends PenguinPacket {
    private WalletType type;
    private long balance, income, expenses;

    public SyncGoldPacket() {}
    public SyncGoldPacket(WalletType type, long balance, long income, long expenses) {
        this.type = type;
        this.balance = balance;
        this.income = income;
        this.expenses = expenses;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeBoolean(type == WalletType.SHARED);
        to.writeLong(balance);
        to.writeLong(income);
        to.writeLong(expenses);
    }

    @Override
    public void decode(PacketBuffer from) {
        type = from.readBoolean() ? WalletType.SHARED : WalletType.PERSONAL;
        balance = from.readLong();
        income = from.readLong();
        expenses = from.readLong();
    }

    @Override
    public void handle(PlayerEntity player) {
        Wallet.setGold(type, balance, income, expenses);
    }
}
