package uk.joshiejack.shopaholic.api.shops;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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