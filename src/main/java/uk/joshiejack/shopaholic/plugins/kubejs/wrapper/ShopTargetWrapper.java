package uk.joshiejack.shopaholic.plugins.kubejs.wrapper;

import dev.latvian.kubejs.entity.EntityJS;
import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.player.PlayerJS;
import dev.latvian.kubejs.util.UtilsJS;
import dev.latvian.kubejs.world.WorldJS;
import net.minecraft.util.math.BlockPos;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class ShopTargetWrapper {
    private final ShopTarget target;

    public ShopTargetWrapper(ShopTarget target) {
        this.target = target;
    }

    public ShopTargetWrapper asPlayer() {
        return new ShopTargetWrapper(target.asPlayer());
    }

    public WorldJS getWorld() {
        return UtilsJS.getWorld(target.getWorld());
    }

    public BlockPos getPos() {
        return target.getPos();
    }

    public PlayerJS<?> getPlayer() {
        return getWorld().getPlayer(target.getPlayer());
    }

    public EntityJS getEntity() {
        return getWorld().getEntity(target.getEntity());
    }

    public ItemStackJS getStack() {
        return ItemStackJS.of(target.getStack());
    }
}
