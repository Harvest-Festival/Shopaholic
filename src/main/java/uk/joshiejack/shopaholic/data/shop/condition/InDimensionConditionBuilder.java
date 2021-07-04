package uk.joshiejack.shopaholic.data.shop.condition;

import net.minecraft.util.RegistryKey;
import net.minecraft.world.World;
import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

//Conditions
public class InDimensionConditionBuilder extends ConditionBuilder {
    private final RegistryKey<World> world;

    public InDimensionConditionBuilder(String id, RegistryKey<World> world) {
        super(id);
        this.world = world;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("condition_in_dimension", "ID,Dimension", CSVUtils.join(id, world.location().toString()));
    }
}
