package uk.joshiejack.shopaholic.shop.condition;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

import java.util.function.Predicate;

public abstract class AbstractHasNBTTagCondition<T> extends AbstractPredicateCondition<T> {
    @Override
    protected AbstractPredicateCondition<T> create(String data) {
        AbstractHasNBTTagCondition<T> condition = create();
        try {
            CompoundNBT compoundnbt = (new JsonToNBT(new StringReader(data))).readStruct();
            condition.predicate = createPredicate(compoundnbt);
        } catch (CommandSyntaxException ignored) {}

        return condition;
    }

    protected abstract AbstractHasNBTTagCondition<T> create();
    protected abstract Predicate<T> createPredicate(CompoundNBT compoundnbt);
}