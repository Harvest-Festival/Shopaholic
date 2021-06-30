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

import javax.annotation.Nonnull;

public class SetValue extends LootFunction {
    public static final LootFunctionType TYPE = new LootFunctionType(new SetValue.Serializer());
    private final long minValue;
    private final long maxValue;

    public SetValue(ILootCondition[] conditionsIn, long min, long max) {
        super(conditionsIn);
        this.minValue = min;
        this.maxValue = max;
    }

    @Nonnull
    @Override
    public LootFunctionType getType() {
        return TYPE;
    }

    @Nonnull
    @Override
    public ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundNBT());
        }

        stack.getTag().putLong("SellValue", minValue != maxValue ? context.getRandom().nextInt((int) (maxValue-minValue)) + minValue : minValue);
        return stack;
    }

    public static class Serializer extends LootFunction.Serializer<SetValue> {
        public void serialize(@Nonnull JsonObject object, @Nonnull SetValue functionClazz, @Nonnull JsonSerializationContext serializationContext) {
            if (functionClazz.minValue == functionClazz.maxValue) object.addProperty("min", functionClazz.minValue);
            else {
                object.addProperty("min", functionClazz.minValue);
                object.addProperty("max", functionClazz.maxValue);
            }
        }

        @Nonnull
        public SetValue deserialize(@Nonnull JsonObject object, @Nonnull JsonDeserializationContext deserializationContext, @Nonnull ILootCondition[] conditionsIn) {
            if (object.has("min") && object.has("max")) return new SetValue(conditionsIn, object.get("min").getAsLong(), object.get("max").getAsLong());
            else return new SetValue(conditionsIn, object.get("value").getAsLong(), object.get("value").getAsLong());
        }
    }
}
