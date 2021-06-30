package uk.joshiejack.shopaholic.plugins.kubejs.event;

import dev.latvian.kubejs.event.EventJS;
import dev.latvian.kubejs.item.ItemStackJS;
import uk.joshiejack.shopaholic.event.ItemGetValueEvent;

public class ItemGetValueEventJS extends EventJS {
    private final ItemGetValueEvent event;

    public ItemGetValueEventJS(ItemGetValueEvent event) {
        this.event = event;
    }

    public long getValue() {
        return event.getValue();
    }

    public ItemStackJS getItem() {
        return ItemStackJS.of(event.getStack());
    }

    public void setNewValue(long value) {
        event.setNewValue(value);
    }

    public long getNewValue() {
        return event.getNewValue();
    }
}