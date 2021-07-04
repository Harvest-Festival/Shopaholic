package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.entity.EntityType;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class EntityNearbyConditionBuilder extends ConditionBuilder {
    private final EntityType<?> entityType;
    private final double range;

    public EntityNearbyConditionBuilder(String id, EntityType<?> entityType, double range) {
        super(id);
        this.entityType = entityType;
        this.range = range;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_entity_nearby", "ID,Entity,Range", CSVUtils.join(id, entityType.getRegistryName().toString(), range));
    }
}