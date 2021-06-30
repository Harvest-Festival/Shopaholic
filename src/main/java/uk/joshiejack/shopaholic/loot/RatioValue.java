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

public class RatioValue extends LootFunction {
    public static final LootFunctionType TYPE = new LootFunctionType(new RatioValue.Serializer());
    private final double ratio;

    public RatioValue(ILootCondition[] conditionsIn, double ratio) {
        super(conditionsIn);
        this.ratio = ratio;
    }

    @Nonnull
    @Override
    public LootFunctionType getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        stack.getTag().putLong("SellValue", (long) (((double) value) * ratio));
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<RatioValue> {
        public void serialize(@Nonnull JsonObject object, @Nonnull RatioValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            object.addProperty("ratio", functionClazz.ratio);
        }

        @Nonnull
        public RatioValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
            return new RatioValue(conditionsIn, object.get("ratio").getAsDouble());
        }
    }
}
