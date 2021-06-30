package uk.joshiejack.shopaholic.shipping;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.Shopaholic;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Market extends WorldSavedData {
    private static final String DATA_NAME = "Market";
    private final Map<UUID, Shipping> shippingData = new HashMap<>();

    public Market() { super(DATA_NAME); }

    public static Market get(ServerWorld world) {
        return world.getDataStorage().computeIfAbsent(Market::new, DATA_NAME);
    }

    public void newDay(ServerWorld world) {
        shippingData.values().forEach(s -> s.onNewDay(world));
    }

    private Shipping getShippingForTeam(UUID uuid) {
        if (!shippingData.containsKey(uuid)) {
            Shipping shipping = new Shipping(this, uuid);
            shippingData.put(uuid, shipping);
            setDirty();
            return shipping;
        } else return shippingData.get(uuid);
    }

    public Shipping getShippingForPlayer(PlayerEntity player) {
        PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
        boolean shared = player.getPersistentData().contains(Shopaholic.MODID + "Settings") &&
                player.getPersistentData().getCompound(Shopaholic.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
        return shared ? getShippingForTeam(team.getID()).shared() : getShippingForTeam(player.getUUID());
    }

    @Override
    public void load(@Nonnull CompoundNBT nbt) {
        nbt.getList("Shipping", 10).stream().map(e -> (CompoundNBT)e).forEach(tag -> {
            UUID uuid = UUID.fromString(tag.getString("UUID"));
            shippingData.put(uuid, new Shipping(this, uuid));
            shippingData.get(uuid).deserializeNBT(tag.getCompound("Data"));
        });
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbt) {
        ListNBT list = new ListNBT();
        shippingData.forEach((uuid, vault) -> {
            CompoundNBT data = new CompoundNBT();
            data.putString("UUID", uuid.toString());
            data.put("Data", vault.serializeNBT());
            list.add(data);
        });

        nbt.put("Shipping", list);
        return nbt;
    }
}
