package uk.joshiejack.shopaholic.shop.condition;

import com.google.common.base.CharMatcher;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.shipping.Shipped;
import uk.joshiejack.shopaholic.shipping.Market;
import uk.joshiejack.shopaholic.shipping.Shipping;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ShippedCondition implements Condition {
    private final List<Item> items = new ArrayList<>();
    private final List<ITag.INamedTag<Item>> tags = new ArrayList<>();
    private int requiredAmount;

    @Override
    public Condition create(Row data, String id) {
        ShippedCondition condition = new ShippedCondition();
        condition.requiredAmount = Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(id)); //Grab the required count from the id
        addEntry(condition, data);
        return condition;
    }

    private void addEntry(ShippedCondition condition, Row data) {
        String item = data.get("item");
        if (item.startsWith("tag:"))
            condition.tags.add(ItemTags.createOptional(new ResourceLocation(item.replace("tag:", ""))));
        else {
            Item theItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(item));
            if (theItem != Items.AIR)
                condition.items.add(theItem);
        }
    }

    @Override
    public void merge(Row data) {
        addEntry(this, data);
    }

    private Set<Shipping.SoldItem> getHolderSet(World world, PlayerEntity player) {
        return world.isClientSide ? Shipped.getSold() : Market.get((ServerWorld) world).getShippingForPlayer(player).getSold();
    }

    @Override
    public boolean valid(@Nonnull ShopTarget target, @Nonnull CheckType type) {
        int total = 0;
        Set<Shipping.SoldItem> sold = getHolderSet(target.getWorld(), target.getPlayer());
        for (Shipping.SoldItem holder : sold) {
            if (items.stream().anyMatch(holder::matches) ||
                tags.stream().anyMatch(holder::matches)) {
                total += holder.getStack().getCount();
                //Return early if this is true, no need to keep counting............
                if (total >= requiredAmount) {
                    return true;
                }
            }
        }

        return false;
    }
}
