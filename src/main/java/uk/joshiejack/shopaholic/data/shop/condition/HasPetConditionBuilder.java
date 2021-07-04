package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.entity.EntityType;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class HasPetConditionBuilder extends ConditionBuilder {
    private final EntityType<?> entityType;

    public HasPetConditionBuilder(String id, EntityType<?> entityType) {
        super(id);
        this.entityType = entityType;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_has_pet", "ID,Entity", CSVUtils.join(id, entityType.getRegistryName().toString()));
    }
}