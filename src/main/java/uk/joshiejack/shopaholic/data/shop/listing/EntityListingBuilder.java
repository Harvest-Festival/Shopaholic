package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.penguinlib.data.database.CSVUtils;
import uk.joshiejack.shopaholic.data.ShopaholicDatabase;
import uk.joshiejack.shopaholic.shop.listing.EntityListingHandler;

public class EntityListingBuilder extends SublistingBuilder<EntityListingBuilder> {
    private final EntityListingHandler.EntitySpawnData entity;

    public EntityListingBuilder(String id, EntityListingHandler.EntitySpawnData entity) {
        super("entity", id);
        this.entity = entity;
    }

    @Override
    public void save(ShopaholicDatabase data) {
        data.addEntry("entity_listings", "ID,Entity", CSVUtils.join(this.data, entity.type.getRegistryName().toString()));
    }
}
