package uk.joshiejack.shopaholic.api.shop;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;

import java.util.List;
import java.util.Map;

public abstract class ListingHandler<T> {
    public static final Map<String, ListingHandler<?>> HANDLERS = Maps.newHashMap();
    protected final List<Pair<T, Long>> items = Lists.newArrayList();
    public abstract Icon createIcon(T t);
    public int getCount(T t) { return 1; }
    public abstract void purchase(PlayerEntity player, T t);
    public abstract boolean isValid(T t);
    public abstract ITextComponent getDisplayName(T t);
    public abstract String getValidityError();

    @OnlyIn(Dist.CLIENT)
    public void addTooltip(List<ITextComponent> list, T t) {}
    public abstract T getObjectFromDatabase(DatabaseLoadedEvent database, String data);

    public static void register(String name, ListingHandler<?> handler) {
        HANDLERS.put(name, handler);
    }
}
