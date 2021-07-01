package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.apache.commons.lang3.mutable.MutableInt;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.bank.WalletType;
import uk.joshiejack.shopaholic.bank.Bank;
import uk.joshiejack.shopaholic.bank.Vault;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

import javax.annotation.Nullable;

public class ShipCommand {
    @Nullable
    private static Vault getVault(CommandContext<CommandSource> ctx) throws CommandSyntaxException {
        WalletType type = ctx.getArgument("type", WalletType.class);
        Bank bank = Bank.get(ctx.getSource().getLevel());
        PlayerEntity player = EntityArgument.getPlayer(ctx, "player");
        return type == WalletType.PERSONAL
                ? bank.getVaultForPlayer(player) : bank.getVaultForTeam(PenguinTeams.getTeamForPlayer(player).getID());
    }

    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("ship")
                .then(Commands.literal("hand")
                        .executes(ctx -> {
                            ItemStack held = ctx.getSource().getPlayerOrException().getMainHandItem();
                            if (!held.isEmpty()) {
                                long value = ShippingRegistry.getValue(held);
                                if (value > 0) {
                                    Market.get(ctx.getSource().getLevel()).getShippingForPlayer(ctx.getSource().getPlayerOrException()).add(held.copy());
                                    ITextComponent name = held.getHoverName();
                                    held.shrink(held.getCount()); //Kill the item
                                    ctx.getSource().sendSuccess(new TranslationTextComponent("You successfully shipped %s", name), false);
                                    return 1;
                                }
                            }

                            ctx.getSource().sendFailure(new StringTextComponent("The item you tried to ship had no value"));
                            return 0;
                        }))
                .then(Commands.literal("inventory")
                        .executes(ctx -> {
                            MutableInt successcount = new MutableInt();
                            MutableInt failcount = new MutableInt();
                            Shipping shipping = Market.get(ctx.getSource().getLevel()).getShippingForPlayer(ctx.getSource().getPlayerOrException());
                            PlayerHelper.getInventoryStream(ctx.getSource().getPlayerOrException()).forEach(stack -> {
                                if (!stack.isEmpty()) {
                                    long value = ShippingRegistry.getValue(stack);
                                    if (value > 0) {
                                        shipping.add(stack.copy());
                                        successcount.add(stack.getCount());
                                        stack.shrink(stack.getCount());
                                    } else failcount.add(stack.getCount());
                                }
                            });

                            ctx.getSource().sendSuccess(new TranslationTextComponent("You successfully shipped %s items and failed to ship %s items",
                                    successcount.intValue(), failcount.intValue()), false);
                            return successcount.intValue() > 0 ? 1 : 0;
                        }));
    }
}
