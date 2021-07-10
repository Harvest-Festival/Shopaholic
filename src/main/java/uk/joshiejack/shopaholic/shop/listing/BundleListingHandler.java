package uk.joshiejack.shopaholic.shop.listing;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ListIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;
import uk.joshiejack.shopaholic.shop.ShopRegistries;

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class BundleListingHandler implements ListingHandler<List<Pair<ListingHandler, Object>>> {
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public Icon createIcon(List<Pair<ListingHandler, Object>> object) {
        return new ListIcon(object.stream().map(pair -> pair.getKey().createIcon(pair.getValue())).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void purchase(PlayerEntity player, List<Pair<ListingHandler, Object>> object) {
        object.forEach(pair -> pair.getKey().purchase(player, pair.getValue()));
    }

    @Override
    public List<Pair<ListingHandler, Object>> getObjectFromDatabase(ShopLoadingData shopLoadingData, DatabaseLoadedEvent event, String data) {
        List<Pair<ListingHandler, Object>> list = Lists.newArrayList();
        event.table("bundles").where("id="+data).forEach(row -> {
            ListingHandler<?> handler = ShopRegistries.LISTING_HANDLERS.get(row.get("type").toString());
            Object entry = handler.getObjectFromDatabase(shopLoadingData, event, row.get("data").toString());
            list.add(Pair.of(handler, entry));
        });

        return list;
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof List && !((List)object).isEmpty();
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public void addTooltip(List<ITextComponent> list, List<Pair<ListingHandler, Object>> object) {
        object.forEach(p -> p.getKey().addTooltip(list, p.getValue()));
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public ITextComponent getDisplayName(List<Pair<ListingHandler, Object>> object) {
        ListingHandler handle = object.get(0).getKey();
        return handle.getDisplayName(object.get(0).getValue());
    }
}
