package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.EntityIcon;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;

public class EntityListingHandler implements ListingHandler<EntityListingHandler.EntitySpawnData> {
    @Override
    public EntitySpawnData getObjectFromDatabase(ShopLoadingData shopLoadingData, DatabaseLoadedEvent database, String data) {
        Row row = database.table("entity_listings").fetch_where("id=" + data);
        EntityType<?> type = row.entity();
        return new EntitySpawnData(type);
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof EntitySpawnData && ((EntitySpawnData)object).type != null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(EntitySpawnData object) {
        return object.type.getDescription();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(EntitySpawnData object) {
        return new EntityIcon(object.type, 1);
    }

    @Override
    public void purchase(PlayerEntity player, EntitySpawnData object) {
        if (!player.level.isClientSide)
            object.type.spawn((ServerWorld) player.level, new CompoundNBT(), null, player, player.blockPosition(), SpawnReason.SPAWN_EGG, true, true);
    }

    public static class EntitySpawnData {
        public final EntityType<?> type;

        public EntitySpawnData(EntityType<?> type) {
            this.type = type;
        }
    }
}
