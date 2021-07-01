package uk.joshiejack.shopaholic.bank;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.generic.MathsHelper;
import uk.joshiejack.shopaholic.ShopaholicServerConfig;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.network.SyncGoldPacket;

import java.util.UUID;

public class Vault implements INBTSerializable<CompoundNBT> {
    private final Bank bank;
    private final UUID uuid;
    private long balance = 0;
    private long expenses = 0;
    private long income = 0;
    private boolean shared;

    public Vault(Bank bank, UUID uuid) {
        this.bank = bank;
        this.uuid = uuid;
    }

    public Vault personal() {
        this.shared = false;
        return this;
    }

    public Vault shared() {
        this.shared = true;
        return this;
    }

    public long getBalance() {
        return balance;
    }

    public void decreaseGold(World world, long amount) {
        if (amount < 0) amount = 0;
        expenses += amount;
        setBalance(world, balance - amount);
    }

    public void increaseGold(World world, long amount) {
        if (amount < 0) amount = 0;
        income += amount;

        setBalance(world, balance + amount);
    }

    public void setBalance(World world, long amount) {
        balance = MathsHelper.constrainToRangeLong(amount, ShopaholicServerConfig.minGold.get(), ShopaholicServerConfig.maxGold.get());
        bank.setDirty(); //Mark for saving
        if (world instanceof ServerWorld)
            synchronize((ServerWorld) world); //Sync the data to the players
    }

    public void synchronize(ServerWorld world) {
        if (shared) {
            PenguinNetwork.sendToTeam(new SyncGoldPacket(WalletType.SHARED, balance, income, expenses), world, uuid);
        } else PenguinNetwork.sendToClient(new SyncGoldPacket(WalletType.PERSONAL, balance, income, expenses), (ServerPlayerEntity) world.getPlayerByUUID(uuid));
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.putLong("Balance", balance);
        tag.putLong("Expenses", expenses);
        tag.putLong("Income", income);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        balance = nbt.getLong("Balance");
        expenses = nbt.getLong("Expenses");
        income = nbt.getLong("Income");
    }
}
