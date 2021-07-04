package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.comparator.ComparatorBuilder;
import uk.joshiejack.simplyseasons.api.Season;

public abstract class ConditionBuilder {
    public final String id;

    public ConditionBuilder(String id) {
        this.id = id;
    }

    public abstract void save(ShopaholicDatabase data);

    public static AndConditionBuilder and(String id, ConditionBuilder... conditions) {
        return new AndConditionBuilder(id, conditions);
    }

    public static CompareConditionBuilder compare(String id, ComparatorBuilder compare1, boolean lessThan, boolean equalTo, boolean greaterThan, ComparatorBuilder compare2) {
        return new CompareConditionBuilder(id, compare1, lessThan, equalTo, greaterThan, compare2);
    }

    public static EntityNearbyConditionBuilder entityNearby(String id, EntityType<?> entityType, double range) {
        return new EntityNearbyConditionBuilder(id, entityType, range);
    }

    public static BlockStateConditionBuilder blockState(String id, String stateName, String stateValue) {
        return new BlockStateConditionBuilder(id, stateName, stateValue);
    }

    public static HasNBTTagConditionBuilder hasNBTTag(String id, String tagName, String tagData) {
        return new HasNBTTagConditionBuilder(id, tagName, tagData);
    }

    public static HasPetConditionBuilder hasPet(String id, EntityType<?> entityType) {
        return new HasPetConditionBuilder(id, entityType);
    }

    public static ShippedConditionBuilder hasShipped(String id, int requiredAmount) {
        return new ShippedConditionBuilder(id, requiredAmount);
    }

    public static InDimensionConditionBuilder inDimension(String id, RegistryKey<World> world) {
        return new InDimensionConditionBuilder(id, world);
    }

    public static SeasonConditionBuilder inSeason(String id, Season season) {
        return new SeasonConditionBuilder(id, season);
    }

    public static NamedConditionBuilder named(String id, String name) {
        return new NamedConditionBuilder(id, name);
    }

    public static OpenHoursConditionBuilder openingHours(String id) {
        return new OpenHoursConditionBuilder(id);
    }

    public static OrConditionBuilder or(String id, ConditionBuilder... conditions) {
        return new OrConditionBuilder(id, conditions);
    }

    public static PerPlayerConditionBuilder perPlayer(String id, int max) {
        return new PerPlayerConditionBuilder(id, max);
    }

    public static TimeConditionBuilder timeOpen(String id) {
        return new TimeConditionBuilder(id);
    }

    public static SeasonPredicateConditionBuilder seasonPredicate(String id, String predicate) {
        return new SeasonPredicateConditionBuilder(id, predicate);
    }
}
