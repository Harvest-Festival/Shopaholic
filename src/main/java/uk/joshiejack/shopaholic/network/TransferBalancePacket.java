package uk.joshiejack.shopaholic.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.bank.Bank;
import uk.joshiejack.shopaholic.bank.Vault;

import java.util.UUID;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_SERVER)
public class TransferBalancePacket extends PenguinPacket {
    private WalletType from;
    private long gold;

    public TransferBalancePacket() {}
    public TransferBalancePacket(WalletType type, long gold) {
        this.from = type;
        this.gold = gold;
    }

    @Override
    public void encode(PacketBuffer to) {
        to.writeBoolean(from == WalletType.SHARED);
        to.writeLong(gold);
    }

    @Override
    public void decode(PacketBuffer from) {
        this.from = from.readBoolean() ? WalletType.SHARED : WalletType.PERSONAL;
        gold = from.readLong();
    }

    @Override
    public void handle(PlayerEntity player) {
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        UUID playerUUID = player.getUUID();
        Vault teamVault = Bank.get((ServerWorld) player.level).getVaultForTeam(team.getID());
        Vault playerVault = Bank.get((ServerWorld) player.level).getVaultForTeam(playerUUID);
        if (from == WalletType.PERSONAL) {
            long actual = Math.min(gold, playerVault.getBalance());
            playerVault.personal().setBalance(player.level, playerVault.getBalance() - actual);
            teamVault.shared().setBalance(player.level, teamVault.getBalance() + actual);
        } else {
            long actual = Math.min(gold, teamVault.getBalance());
            teamVault.shared().setBalance(player.level, teamVault.getBalance() - actual);
            playerVault.personal().setBalance(player.level, playerVault.getBalance() + actual);
        }
    }
}
