package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.shopaholic.Shopaholic;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShopaholicCommands {
    @SubscribeEvent
    public static void registerCommands(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                LiteralArgumentBuilder.<CommandSource>literal(Shopaholic.MODID)
                        .requires(cs -> cs.hasPermission(2))
                        .then(GoldCommand.register())
                        //TODO.then(ShareCommand.register())
                        //TODO.then(TransferCommand.register())
        );
    }
}
