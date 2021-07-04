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

import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("rawtypes")
public class BundleListingHandler extends ListingHandler<List<Pair<ListingHandler, Object>>> {
    @SuppressWarnings("unchecked")
    @Override
    public Icon createIcon(List<Pair<ListingHandler, Object>> pairs) {
        return new ListIcon(pairs.stream().map(pair -> pair.getKey().createIcon(pair.getValue())).collect(Collectors.toList()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void purchase(PlayerEntity player, List<Pair<ListingHandler, Object>> list) {
        list.forEach(pair -> pair.getKey().purchase(player, pair.getValue()));
    }

    @Override
    public List<Pair<ListingHandler, Object>> getObjectFromDatabase(DatabaseLoadedEvent event, String data) {
        List<Pair<ListingHandler, Object>> list = Lists.newArrayList();
        event.table("bundles").where("id="+data).forEach(row -> {
            ListingHandler<?> handler = ListingHandler.HANDLERS.get(row.get("type").toString());
            Object entry = handler.getObjectFromDatabase(event, row.get("data").toString());
            list.add(Pair.of(handler, entry));
        });

        return list;
    }

    @Override
    public boolean isValid(List<Pair<ListingHandler, Object>> pairs) {
        return !pairs.isEmpty();
    }

    @Override
    public String getValidityError() {
        return "No objects were valid";
    }

    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unchecked")
    @Override
    public void addTooltip(List<ITextComponent> list, List<Pair<ListingHandler, Object>> pairs) {
        pairs.forEach(p -> p.getKey().addTooltip(list, p.getValue()));
    }

    @SuppressWarnings("unchecked")
    @Override
    public ITextComponent getDisplayName(List<Pair<ListingHandler, Object>> pairs) {
        ListingHandler handle = pairs.get(0).getKey();
        return handle.getDisplayName(pairs.get(0).getValue());
    }
}
