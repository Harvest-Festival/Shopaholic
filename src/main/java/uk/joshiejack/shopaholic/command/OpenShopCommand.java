package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.input.InputToShop;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class OpenShopCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("open")
                .then(Commands.argument("shop", StringArgumentType.string())
                        .suggests((ctx, sb) -> ISuggestionProvider.suggest(suggestions(ctx), sb))
                        .executes(ctx -> {
                            String arg = StringArgumentType.getString(ctx, "shop");
                            PlayerEntity player = ctx.getSource().getPlayerOrException();
                            return InputToShop.open(InputToShop.COMMAND_TO_SHOP.get(arg),
                                    new ShopTarget(player.level, player.blockPosition(), player, player, ItemStack.EMPTY, new EntityShopInput(player)),
                                    InputMethod.COMMAND) ? 1 : 0;
                        }));
    }

    private static List<String> suggestions(final CommandContext<CommandSource> context) {
        return InputToShop.COMMAND_TO_SHOP.entries().stream()
                .filter(e -> e.getValue().isValidFor(ShopTarget.fromSource(context.getSource()), Condition.CheckType.SHOP_EXISTS))
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }
}
