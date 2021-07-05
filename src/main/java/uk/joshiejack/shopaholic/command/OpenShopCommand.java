package uk.joshiejack.shopaholic.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Shop;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.input.InputToShop;

public class OpenShopCommand {
    public static ArgumentBuilder<CommandSource, ?> register() {
        return Commands.literal("open")
                .then(Commands.argument("shop", StringArgumentType.string())
                        .suggests((ctx, sb) -> ISuggestionProvider.suggest(InputToShop.COMMAND_TO_SHOP.keySet(), sb))
                        .executes(ctx -> {
                            String arg = StringArgumentType.getString(ctx, "shop");
                            Shop shop = InputToShop.COMMAND_TO_SHOP.get(arg);
                            PlayerEntity player = ctx.getSource().getPlayerOrException();
                            return shop != null && InputToShop.open(shop.getDepartments(),
                                    new ShopTarget(player.level, player.blockPosition(), player, player, ItemStack.EMPTY, new EntityShopInput(player)),
                                    InputMethod.COMMAND) ? 1 : 0;
                        }));
    }
}
