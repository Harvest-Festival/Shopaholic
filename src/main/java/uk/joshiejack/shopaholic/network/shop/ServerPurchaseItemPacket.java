package uk.joshiejack.shopaholic.network.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.bank.Bank;
import uk.joshiejack.shopaholic.bank.Vault;
import uk.joshiejack.shopaholic.network.AbstractPurchaseItemPacket;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.Listing;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_SERVER)
public class ServerPurchaseItemPacket extends AbstractPurchaseItemPacket {
    public ServerPurchaseItemPacket() {}
    public ServerPurchaseItemPacket(Department department, Listing listing, int amount) {
        super(department, listing, amount);
    }

    @Override
    public void handle(PlayerEntity player) {
        if (listing.canPurchase(player, department.getStockLevels(player.level), amount)) {
            if (purchase((ServerPlayerEntity)player)) {
                PenguinNetwork.sendToClient(new ClientPurchaseItemPacket(department, listing, amount), (ServerPlayerEntity) player); //Send the packet back
            }
        }
    }

    private boolean purchase(ServerPlayerEntity player) {
        Vault vault = Bank.get((ServerWorld) player.level).getVaultForPlayer(player);
        long cost = listing.getGoldCost(player, department.getStockLevels(player.level)); //TownHelper.getClosestTownToEntity(player, false).getShops().getSellValue(shop, purchasable); //TODO: Enable adjusted value
        long total = cost * amount;
        if (vault.getBalance() - total >= 0) {
            if (total >= 0) vault.decreaseBalance(player.level, total);
            else vault.increaseBalance(player.level, -total);
            for (int i = 0; i < amount; i++) {
                listing.purchase(player);
            }

            return true;
        }

        return false;
    }
}
