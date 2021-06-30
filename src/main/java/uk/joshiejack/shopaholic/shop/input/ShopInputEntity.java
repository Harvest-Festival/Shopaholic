package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.shopaholic.api.shops.ShopInput;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

public class ShopInputEntity extends ShopInput<EntityType<?>> {
    public ShopInputEntity(Entity entity) {
        super(entity.getType());
    }

    public ShopInputEntity(EntityType<?> type) {
        super(type);
    }

    public ShopInputEntity(PacketBuffer buf) {
        super(buf.readRegistryIdSafe(EntityType.class));
    }

    @Override
    public String getName(ShopTarget target) {
        return target.entity.getName().getString();
    }

    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        return target.entity.saveWithoutId(new CompoundNBT()).getString(key).equals(value);
    }
}
