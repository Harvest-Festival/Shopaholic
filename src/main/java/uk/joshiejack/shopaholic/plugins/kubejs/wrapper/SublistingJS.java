package uk.joshiejack.shopaholic.plugins.kubejs.wrapper;

import dev.latvian.kubejs.item.ItemStackJS;
import net.minecraft.item.ItemStack;
import uk.joshiejack.shopaholic.shop.Sublisting;

@SuppressWarnings("unused")
public class SublistingJS<T> {
    private final Sublisting<T> sublisting;

    public SublistingJS(Sublisting<T> sublisting) {
        this.sublisting = sublisting;
    }

    public ItemStackJS item() {
        if (sublisting.getObject() instanceof ItemStack) {
            return ItemStackJS.of(sublisting.getObject());
        } else return ItemStackJS.of(ItemStack.EMPTY);
    }

    public long gold() {
        return sublisting.getGold();
    }

    public String id() {
        return sublisting.id();
    }
}
