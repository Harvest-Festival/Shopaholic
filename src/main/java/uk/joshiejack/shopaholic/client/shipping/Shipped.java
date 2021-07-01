package uk.joshiejack.shopaholic.client.shipping;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@OnlyIn(Dist.CLIENT)
public class Shipped {
    private final Set<Shipping.SoldItem> sold = Sets.newHashSet();
    private final List<Shipping.SoldItem> lastSold = Lists.newLinkedList();
    private static final Shipped INSTANCE = new Shipped();
    private final Cache<ItemStack, Integer> countCache = CacheBuilder.newBuilder().build();

    public static List<Shipping.SoldItem> getShippingLog() {
        return INSTANCE.lastSold;
    }

    public static Set<Shipping.SoldItem> getSold() {
        return INSTANCE.sold;
    }

    public static void clearCountCache() {
        INSTANCE.countCache.invalidateAll();
    }

    public static int getCount(ItemStack stack) {
        try {
            return INSTANCE.countCache.get(stack, () -> {
                int total = 0;
                for (Shipping.SoldItem s: INSTANCE.sold) {
                    if (s.matches(stack)) return s.getStack().getCount();
                }

                return total;
            });
        } catch (ExecutionException e) {
            return 0;
        }
    }
}
