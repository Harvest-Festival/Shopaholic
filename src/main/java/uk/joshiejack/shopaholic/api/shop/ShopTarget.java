package uk.joshiejack.shopaholic.api.shop;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;

public class ShopTarget {
    private World world; //The world that this target is in
    private BlockPos pos; //The position of this target
    private Entity entity; //The entity associated with this target, EITHER the actual entity OR the player if it's an item or blockstate
    private PlayerEntity player; //The player entity that was interacting with this target
    private ItemStack stack; //The stack that is interacting, whether it's relevant or not
    private ShopInput<?> input; //The input handler, for special situations

    public ShopTarget() {}
    public ShopTarget(World world, BlockPos pos, Entity entity, PlayerEntity player, ItemStack stack, ShopInput<?> input) {
        this.world = world;
        this.pos = pos;
        this.entity = entity;
        this.player = player;
        this.stack = stack;
        this.input = input;
    }

    public static ShopTarget fromPlayer(PlayerEntity player) {
        return new ShopTarget(player.level, player.blockPosition(), player, player, player.getMainHandItem(), new EntityShopInput(player));
    }

    public static ShopTarget fromSource(CommandSource source) {
        try {
            return fromPlayer(source.getPlayerOrException());
        } catch (CommandSyntaxException ex) { return null; }
    }

    public ShopTarget asPlayer() {
        return new ShopTarget(world, pos, player, player, stack, input);
    }

    public World getWorld() {
        return world;
    }

    public BlockPos getPos() {
        return pos;
    }

    public Entity getEntity() {
        return entity;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    public ItemStack getStack() {
        return stack;
    }

    public ShopInput<?> getInput() {
        return input;
    }
}