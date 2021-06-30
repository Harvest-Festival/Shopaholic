package uk.joshiejack.shopaholic.api;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import uk.joshiejack.shopaholic.api.gold.IVault;

public interface IEconomyAPI {
    IVault getVaultForPlayer(World world, PlayerEntity player);
    IVault getVaultForTeam(World world, PlayerEntity player);
}
