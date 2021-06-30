package uk.joshiejack.shopaholic.data;

import net.minecraft.data.DataGenerator;
import uk.joshiejack.penguinlib.data.generators.AbstractDatabaseProvider;
import uk.joshiejack.shopaholic.Shopaholic;

public class ShopaholicDatabase extends AbstractDatabaseProvider {
    public ShopaholicDatabase(DataGenerator gen) {
        super(gen, Shopaholic.MODID);
    }

    @Override
    protected void addDatabaseEntries() {

    }
}
