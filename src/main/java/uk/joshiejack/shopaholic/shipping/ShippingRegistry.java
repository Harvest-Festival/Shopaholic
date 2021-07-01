package uk.joshiejack.shopaholic.shipping;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.event.ItemGetValueEvent;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class ShippingRegistry {
    private static final Object2LongMap<Item> itemToValue = new Object2LongOpenHashMap<>();
    private static final Object2LongMap<ResourceLocation> tagToValue = new Object2LongOpenHashMap<>();

    @SuppressWarnings("ConstantConditions")
    public static long getValue(ItemStack stack) {
        long value = stack.hasTag() && stack.getTag().contains("SellValue") ? stack.getTag().getLong("SellValue") : itemToValue.getLong(stack.getItem());
        if (value == 0) {
            for (ResourceLocation tag: stack.getItem().getTags())
                if (tagToValue.containsKey(tag)) {
                    value = tagToValue.getLong(tag);
                    break;
                }
        }

        ItemGetValueEvent event = new ItemGetValueEvent(stack, value);
        MinecraftForge.EVENT_BUS.post(event);
        return event.getNewValue();
    }

    @SubscribeEvent
    public static void onDatabaseLoaded(DatabaseLoadedEvent event) {
        itemToValue.clear();
        tagToValue.clear();
        event.table("item_values").rows().stream()
                .filter(row -> row.item() != null && !row.isEmpty("value"))
                .forEach(row -> itemToValue.put(row.item(), row.getAsLong("value")));
        event.table("tag_values").rows().stream()
                .filter(row -> row.getRL("tag") != null && !row.isEmpty("value"))
                .forEach(row -> tagToValue.put(row.getRL("tag"), row.getAsLong("value")));
    }
}
