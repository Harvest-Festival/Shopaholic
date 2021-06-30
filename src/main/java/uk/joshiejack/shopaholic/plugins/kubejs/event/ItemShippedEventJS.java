package uk.joshiejack.shopaholic.plugins.kubejs.event;

import dev.latvian.kubejs.item.ItemStackJS;
import dev.latvian.kubejs.world.WorldEventJS;
import dev.latvian.kubejs.world.WorldJS;
import net.minecraft.world.World;
import uk.joshiejack.shopaholic.event.ItemShippedEvent;
import uk.joshiejack.shopaholic.plugins.kubejs.wrapper.PenguinTeamJS;

public class ItemShippedEventJS extends WorldEventJS {
    private final ItemShippedEvent event;

    public ItemShippedEventJS(ItemShippedEvent event) {
        this.event = event;
    }

    public long getValue() {
        return event.getValue();
    }

    public ItemStackJS getItem() {
        return ItemStackJS.of(event.getShipped());
    }

    public PenguinTeamJS getTeam() {
        return new PenguinTeamJS(event.getTeam());
    }

    @Override
    public WorldJS getWorld() {
        return worldOf((World) event.getWorld());
    }
}