package uk.joshiejack.shopaholic.api.shop;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Objects;

public abstract class ShopInput<T extends IForgeRegistryEntry<T>> {
    protected final T id;

    protected ShopInput(T id) {
        this.id = id;
    }

    public void encode(PacketBuffer buf) {
        buf.writeRegistryId(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShopInput<?> input = (ShopInput<?>) o;
        return Objects.equals(id.getRegistryName(), input.id.getRegistryName());
    }

    public T get() {
        return id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id.getRegistryName());
    }

    public String getName(ShopTarget target) {
        return Objects.requireNonNull(id.getRegistryName()).toString();
    }
}
