package uk.joshiejack.shopaholic.shop.inventory;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.events.NewDayEvent;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shop.Department;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class Restocker {
    @SubscribeEvent
    public static void onNewDay(NewDayEvent event) {
        ServerWorld world = event.getWorld();
        if (world.dimension() == World.OVERWORLD) {
            Department.REGISTRY.values().forEach(dp -> dp.getStockLevels().newDay(world.random));
            Inventory.setChanged(world);
            Market.get(event.getWorld()).newDay(world);
        }
    }
}
