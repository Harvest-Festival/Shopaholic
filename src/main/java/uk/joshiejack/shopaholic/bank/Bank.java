package uk.joshiejack.shopaholic.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.network.bank.SetActiveWalletPacket;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Bank extends WorldSavedData {
    private static final String DATA_NAME = "bank";
    private final Map<UUID, Vault> vaults = new HashMap<>();

    public Bank() { super(DATA_NAME); }

    public static Bank get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(Bank::new, DATA_NAME);
    }

    public Vault getVaultForTeam(UUID uuid) {
        if (!vaults.containsKey(uuid)) {
            Vault vault = new Vault(this, uuid);
            vaults.put(uuid, vault);
            setDirty();
            return vault;
        } else return vaults.get(uuid);
    }

    public Vault getVaultForPlayer(PlayerEntity player) {
        //Change this to be based on a person player toggle instead
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getPersistentData().contains("ShopaholicSettings") &&
                player.getPersistentData().getCompound("ShopaholicSettings").getBoolean("SharedWallet"); //Player's Shared gold status
        return shared ? getVaultForTeam(team.getID()).shared() : getVaultForTeam(player.getUUID()).personal();
    }

    public void syncToPlayer(ServerPlayerEntity player) {
        //Sync both
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getPersistentData().contains("ShopaholicSettings") &&
                player.getPersistentData().getCompound("ShopaholicSettings").getBoolean("SharedWallet"); //Player's Shared gold status
        PenguinNetwork.sendToClient(new SetActiveWalletPacket(shared), player);
        getVaultForTeam(team.getID()).shared().synchronize((ServerWorld) player.level);
        getVaultForTeam(player.getUUID()).personal().synchronize((ServerWorld) player.level);
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        nbt.getList("Vaults", 10).stream().map(e -> (CompoundNBT)e).forEach(tag -> {
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            vaults.put(uuid, new Vault(this, uuid));
            vaults.get(uuid).deserializeNBT(tag.getCompound("Data"));
        });
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        vaults.forEach((uuid, vault) -> {
            CompoundNBT data = new CompoundNBT();
            data.putString("UUID", uuid.toString());
            data.put("Data", vault.serializeNBT());
            list.add(data);
        });

        nbt.put("Vaults", list);
        return nbt;
    }
}
