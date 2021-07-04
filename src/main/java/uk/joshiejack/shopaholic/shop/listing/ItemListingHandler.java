package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import java.util.List;

public class ItemListingHandler implements ListingHandler<ItemStack> {
    @Override
    public ItemStack getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data)));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addTooltip(List<ITextComponent> list, ItemStack object) {
        list.addAll(object.getTooltipLines(null, ITooltipFlag.TooltipFlags.NORMAL));
    }

    @Override
    public boolean isValid(ItemStack object) {
        return !object.isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(ItemStack object) {
        return object.getHoverName();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(ItemStack object) {
        return new ItemIcon(object);
    }

    @Override
    public int getCount(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public void purchase(PlayerEntity player, ItemStack object) {
        if (!object.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, object.copy());
        }
    }
}
