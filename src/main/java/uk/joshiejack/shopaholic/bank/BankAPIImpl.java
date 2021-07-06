package uk.joshiejack.shopaholic.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.shopaholic.api.bank.IBank;
import uk.joshiejack.shopaholic.client.bank.Wallet;

public class BankAPIImpl implements IBank {
    @Override
    public long getBalance(PlayerEntity player) {
        return player.level.isClientSide() ? Wallet.getActive().getBalance() : Bank.get((ServerWorld) player.level).getVaultForPlayer(player).getBalance();
    }

    @Override
    public void increaseBalance(PlayerEntity player, long amount) {
        if (!player.level.isClientSide)
            Bank.get((ServerWorld) player.level).getVaultForPlayer(player).increaseBalance(player.level, amount);
    }

    @Override
    public void decreaseBalance(PlayerEntity player, long amount) {
        if (!player.level.isClientSide)
            Bank.get((ServerWorld) player.level).getVaultForPlayer(player).decreaseBalance(player.level, amount);
    }
}
