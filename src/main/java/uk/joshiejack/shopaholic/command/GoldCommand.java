package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.server.command.EnumArgument;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.gold.WalletType;
import uk.joshiejack.shopaholic.gold.Bank;
import uk.joshiejack.shopaholic.gold.Vault;

import javax.annotation.Nullable;

public class GoldCommand {
    @Nullable
    private static Vault getVault(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        WalletType type = ctx.getArgument("type", WalletType.class);
        Bank bank = Bank.get(ctx.getSource().getLevel());
        PlayerEntity player = EntityArgument.getPlayer(ctx, "player");
        return type == WalletType.PERSONAL
                ? bank.getVaultForPlayer(player) : bank.getVaultForTeam(PenguinTeams.getTeamForPlayer(player).getID());
    }

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("gold")
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("type", EnumArgument.enumArgument(WalletType.class))
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(ctx -> {
                                                    Vault vault = getVault(ctx);
                                                    if (vault != null) {
                                                        vault.setBalance(ctx.getSource().getLevel(), vault.getBalance() + IntegerArgumentType.getInteger(ctx, "amount"));
                                                        return 1;
                                                    } else return 0;
                                                })))))

                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("type", EnumArgument.enumArgument(WalletType.class))
                                        .then(Commands.argument("amount", IntegerArgumentType.integer())
                                                .executes(ctx -> {
                                                    Vault vault = getVault(ctx);
                                                    if (vault != null) {
                                                        vault.setBalance(ctx.getSource().getLevel(), IntegerArgumentType.getInteger(ctx, "amount"));
                                                        return 1;
                                                    } else return 0;
                                                })))));
    }
}
