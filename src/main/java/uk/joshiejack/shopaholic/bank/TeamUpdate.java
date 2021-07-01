package uk.joshiejack.shopaholic.bank;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.events.TeamChangedEvent;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.shipping.Market;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class TeamUpdate {
    @SubscribeEvent
    public static void onTeamChanged(TeamChangedEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getWorld().getPlayerByUUID(event.getPlayer());
        if (player != null) {
            Bank.get((ServerWorld) event.getWorld()).syncToPlayer(player);
            Market.get((ServerWorld) event.getWorld()).getShippingForPlayer(player).syncToPlayer(player);
        }
    }
}
