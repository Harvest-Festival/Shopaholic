package uk.joshiejack.shopaholic.shop.listing;

import com.google.common.collect.Lists;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import java.util.List;

public class ItemListingHandler extends ListingHandler<ItemStack> {
    protected final List<Pair<ItemStack, Long>> items = Lists.newArrayList();

    @Override
    public String getType() {
        return "item";
    }

    @Override
    public ItemStack getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        //TODO: ?Stacks of items
        return new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data)));
    }

    @Override
    public String getStringFromObject(ItemStack stack) {
        //TODO: ?Stacks of items
        return stack.getItem().getRegistryName().toString();
    }

    @Override
    public String getValidityError() {
        return "Item does not exist";
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addTooltip(List<ITextComponent> list, ItemStack stack) {
        list.addAll(stack.getTooltipLines(null, ITooltipFlag.TooltipFlags.NORMAL));
    }

    @Override
    public boolean isValid(ItemStack stack) {
        return !stack.isEmpty();
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        return stack.getHoverName();
    }

    @Override
    public Icon createIcon(ItemStack stack) {
        return new ItemIcon(stack);
    }

    @Override
    public int getCount(ItemStack stack) {
        return stack.getCount();
    }

    @Override
    public void purchase(PlayerEntity player, ItemStack stack) {
        if (!stack.isEmpty()) {
            ItemHandlerHelper.giveItemToPlayer(player, stack.copy());
        }
    }
}
