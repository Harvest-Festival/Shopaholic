package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.server.ServerWorld;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.util.icon.EntityIcon;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

//TODO: More powerful options for the entity
public class EntityListingHandler extends ListingHandler<EntityListingHandler.EntitySpawnData> {
    @Override
    public String getType() {
        return "entity";
    }

    @Override
    public EntitySpawnData getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        Row row = database.table("entity_listings").fetch_where("id=" + data);
        EntityType<?> type = row.entity();
        int scale = row.getAsInt("scale");
        return new EntitySpawnData(type, scale);
    }

    @Override
    public String getStringFromObject(EntitySpawnData data) {
        return StringHelper.join(' ', data.type.getRegistryName().toString() + data.scale);
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
        return new EntityIcon(data.type, data.scale);
//        ItemStack stack = new ItemStack(PenguinItems.ENTITY);
//        stack.setTag(new CompoundNBT());
//        stack.getTag().setString("Entity", resource.toString());
//        return new ItemStack[]{stack};
    }

    @Override
    public void purchase(PlayerEntity player, EntitySpawnData data) {
        if (!player.level.isClientSide)
            data.type.spawn((ServerWorld) player.level, new CompoundNBT(), null, player, player.blockPosition(), SpawnReason.SPAWN_EGG, true, true);
    }

    public static class EntitySpawnData {
        public final EntityType<?> type;
        public final int scale;

        public EntitySpawnData(EntityType<?> type, int scale) {
            this.type = type;
            this.scale = scale;
        }
    }
}
