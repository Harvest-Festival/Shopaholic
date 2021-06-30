package uk.joshiejack.shopaholic.api.shops;

import net.minecraft.network.PacketBuffer;

import java.util.Objects;

public abstract class ShopInput<T> {
    protected T id;

    protected ShopInput(T id) {
        this.id = id;
    }

    public void encode(PacketBuffer buf) {
        buf.writeUtf(id.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopInput<?> input = (ShopInput<?>) o;
        return Objects.equals(id, input.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getName(ShopTarget target) {
        return id.toString();
    }

    public abstract boolean hasTag(ShopTarget target, String key, String value);
}
