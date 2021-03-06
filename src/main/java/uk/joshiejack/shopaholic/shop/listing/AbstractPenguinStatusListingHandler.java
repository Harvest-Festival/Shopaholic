package uk.joshiejack.shopaholic.shop.listing;

import joptsimple.internal.Strings;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;
import uk.joshiejack.shopaholic.api.shop.ShopLoadingData;

@SuppressWarnings("rawtypes")
public abstract class AbstractPenguinStatusListingHandler implements ListingHandler<Pair<String, Comparator>> {
    private static final ITextComponent EMPTY = new StringTextComponent(Strings.EMPTY);

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(Pair<String, Comparator> object) {
        return CommandListingHandler.ICON.get();
    }

    protected abstract String getTableName();

    //Unused
    @Override
    public Pair<String, Comparator> getObjectFromDatabase(ShopLoadingData data, DatabaseLoadedEvent event, String id) {
        Row row = event.table(getTableName()).fetch_where("id=" + id);
        String field = row.get("field");
        Comparator comparator = data.comparators.get(row.get("comparator id").toString());
        return Pair.of(field, comparator);
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof Pair && ((Pair)object).getLeft() != null && ((Pair)object).getRight() != null;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(Pair<String, Comparator> object) {
        return EMPTY;
    }
}
