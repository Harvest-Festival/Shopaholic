package uk.joshiejack.shopaholic.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.nbt.CompoundNBT;
import uk.joshiejack.shopaholic.shipping.ShippingRegistry;

import javax.annotation.Nonnull;

public class CapValue extends LootFunction {
    public static final LootFunctionType TYPE = new LootFunctionType(new Serializer());
    private final long cap;

    public CapValue(ILootCondition[] conditionsIn, long max) {
        super(conditionsIn);
        this.cap = max;
    }

    @Nonnull
    @Override
    public LootFunctionType getType() {
        return TYPE;
    }

    @Nonnull
    public ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        if (value > cap && !stack.hasTag()) {
            stack.setTag(new CompoundNBT());
            stack.getTag().putLong("SellValue", cap);
        }

        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<CapValue> {
        public void serialize(@Nonnull JsonObject object, @Nonnull CapValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("cap", functionClazz.cap);
        }

        @Nonnull
        public CapValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
            return new CapValue(conditionsIn, object.get("cap").getAsLong());
        }
    }
}
