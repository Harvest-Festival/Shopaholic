package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.network.NetworkHooks;
import uk.joshiejack.shopaholic.inventory.EconomyManagerContainer;

public class ManagerCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("manager")
                .executes(ctx -> {
                    NetworkHooks.openGui(ctx.getSource().getPlayerOrException(),
                            new SimpleNamedContainerProvider((id, inv, p) -> new EconomyManagerContainer(id),
                                    new TranslationTextComponent("gui.shopaholic.manager")));
                    return 1;
                });
    }
}
