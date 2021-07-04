package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.EntityIcon;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

public class EntityListingHandler extends ListingHandler<EntityListingHandler.EntitySpawnData> {
    @Override
    public EntitySpawnData getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        Row row = database.table("entity_listings").fetch_where("id=" + data);
        EntityType<?> type = row.entity();
        return new EntitySpawnData(type);
    }

    @Override
    public boolean isValid(EntitySpawnData data) {
        return data.type != null;
    }

    @Override
    public String getValidityError() {
        return "Entity does not exist";
    }

    @Override
    public ITextComponent getDisplayName(EntitySpawnData data) {
        return data.type.getDescription();
    }

    @Override
    public Icon createIcon(EntitySpawnData data) {
        return new EntityIcon(data.type, 1);
    }

    @Override
    public void purchase(PlayerEntity player, EntitySpawnData data) {
        if (!player.level.isClientSide)
            data.type.spawn((ServerWorld) player.level, new CompoundNBT(), null, player, player.blockPosition(), SpawnReason.SPAWN_EGG, true, true);
    }

    public static class EntitySpawnData {
        public final EntityType<?> type;

        public EntitySpawnData(EntityType<?> type) {
            this.type = type;
        }
    }
}
