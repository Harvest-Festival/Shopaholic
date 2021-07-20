package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.entity.EntityType;
import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.data.shop.comparator.ComparatorBuilder;
import uk.joshiejack.simplyseasons.api.Season;

public abstract class ConditionBuilder {
    public final String id;

    @SuppressWarnings("unused")
    public ConditionBuilder(String id) {
        this.id = id;
    }

    public abstract void save(ShopaholicDatabase data);

    @SuppressWarnings("unused")
    public static AndConditionBuilder and(String id, ConditionBuilder... conditions) {
        return new AndConditionBuilder(id, conditions);
    }

    @SuppressWarnings("unused")
    public static CompareConditionBuilder compare(String id, ComparatorBuilder compare1, boolean lessThan, boolean equalTo, boolean greaterThan, ComparatorBuilder compare2) {
        return new CompareConditionBuilder(id, compare1, lessThan, equalTo, greaterThan, compare2);
    }

    @SuppressWarnings("unused")
    public static EntityNearbyConditionBuilder entityNearby(String id, EntityType<?> entityType, double range) {
        return new EntityNearbyConditionBuilder(id, entityType, range);
    }

    @SuppressWarnings("unused")
    public static BlockStateConditionBuilder blockState(String id, String stateName, String stateValue) {
        return new BlockStateConditionBuilder(id, stateName, stateValue);
    }

    @SuppressWarnings("unused")
    public static HasNBTTagConditionBuilder entityHasNBTTag(String id, String tagData) {
        return new HasNBTTagConditionBuilder("entity", id, tagData);
    }

    @SuppressWarnings("unused")
    public static HasPetConditionBuilder hasPet(String id, EntityType<?> entityType) {
        return new HasPetConditionBuilder(id, entityType);
    }

    @SuppressWarnings("unused")
    public static ShippedConditionBuilder hasShipped(String id, int requiredAmount) {
        return new ShippedConditionBuilder(id, requiredAmount);
    }

    @SuppressWarnings("unused")
    public static InDimensionConditionBuilder inDimension(String id, RegistryKey<World> world) {
        return new InDimensionConditionBuilder(id, world);
    }

    @SuppressWarnings("unused")
    public static SeasonConditionBuilder inSeason(String id, Season season) {
        return new SeasonConditionBuilder(id, season);
    }

    @SuppressWarnings("unused")
    public static HasNBTTagConditionBuilder itemHasNBTTag(String id, String tagData) {
        return new HasNBTTagConditionBuilder("item", id, tagData);
    }

    @SuppressWarnings("unused")
    public static KubeJSConditionBuilder kubejs(String id) {
        return new KubeJSConditionBuilder(id);
    }

    @SuppressWarnings("unused")
    public static NamedConditionBuilder named(String id, String name) {
        return new NamedConditionBuilder("named", id, name);
    }

    @SuppressWarnings("unused")
    public static NotConditionBuilder not(String id, ConditionBuilder... conditions) {
        return new NotConditionBuilder(id, conditions);
    }

    @SuppressWarnings("unused")
    public static OpenHoursConditionBuilder openingHours(String id) {
        return new OpenHoursConditionBuilder(id);
    }

    @SuppressWarnings("unused")
    public static OrConditionBuilder or(String id, ConditionBuilder... conditions) {
        return new OrConditionBuilder(id, conditions);
    }

    @SuppressWarnings("unused")
    public static PerPlayerConditionBuilder perPlayer(String id, int max) {
        return new PerPlayerConditionBuilder(id, max);
    }

    @SuppressWarnings("unused")
    public static HasNBTTagConditionBuilder playerHasNBTTag(String id, String tagData) {
        return new HasNBTTagConditionBuilder("player", id, tagData);
    }

    @SuppressWarnings("unused")
    public static NamedConditionBuilder playerNamed(String id, String name) {
        return new NamedConditionBuilder("player_named", id, name);
    }

    @SuppressWarnings("unused")
    public static HasNBTTagConditionBuilder tileEntityHasNBTTag(String id, String tagData) {
        return new HasNBTTagConditionBuilder("tile_entity", id, tagData);
    }

    @SuppressWarnings("unused")
    public static TimeConditionBuilder timeOpen(String id) {
        return new TimeConditionBuilder(id);
    }

    @SuppressWarnings("unused")
    public static SeasonPredicateConditionBuilder seasonPredicate(String id, String predicate) {
        return new SeasonPredicateConditionBuilder(id, predicate);
    }
}
