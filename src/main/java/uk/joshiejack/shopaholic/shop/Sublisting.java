package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Sublisting<P> {
    private final String sub_id;
    private final ListingHandler<P> handler;
    private final P object;
    private Listing listing = null;
    private int int_id;
    private List<MaterialCost> materials = Lists.newArrayList();
    private List<ITextComponent> tooltip = Lists.newArrayList();
    private Icon icon;
    private ITextComponent displayName = new StringTextComponent("");
    private long gold;
    private double weight = 1;

    public Sublisting(String sub_id, ListingHandler<P> handler, P object) {
        this.sub_id = sub_id;
        this.handler = handler;
        this.object = object;
    }

    public Sublisting<?> setIntID(int id) {
        this.int_id = id;
        return this;
    }

    public void setListing(Listing listing) {
        this.listing = listing;
    }

    public int int_id() {
        return int_id;
    }

    public void addMaterial(MaterialCost cost) {
        this.materials.add(cost);
    }

    public void setGold(long gold) {
        this.gold = gold;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String id() {
        return sub_id;
    }

    public double getWeight() {
        return weight;
    }

    public P getObject() {
        return object;
    }

    public long getGold() {
        return gold;
    }

    public List<MaterialCost> getMaterials() {
        return materials;
    }

    public void purchase(PlayerEntity player) {
        handler.purchase(player, object);
        materials.forEach(m -> m.fulfill(player));
    }

    public ListingHandler<P> getHandler() {
        return handler;
    }

    public ITextComponent getDisplayName() {
        return !displayName.getContents().isEmpty() ? displayName : handler.getDisplayName(object);
    }

    public boolean isGoldOnly() {
        return materials.isEmpty();
    }

    public Icon getIcon() {
        if (icon == null) {
            icon = handler.createIcon(object);
        }

        return icon;
    }

    public int getCount() {
        return handler.getCount(object);
    }

    @OnlyIn(Dist.CLIENT)
    public void addTooltip(List<ITextComponent> list) {
        if (tooltip.size() == 0) handler.addTooltip(list, object);
        else list.addAll(tooltip);
    }

    public void setTooltip(String... tooltip) {
        this.tooltip = Lists.newArrayList(tooltip).stream().map(StringTextComponent::new).collect(Collectors.toList());
    }

    public void setDisplayName(String name) {
        this.displayName = new StringTextComponent(name);
    }

    public void setIcon(Icon icon) {
        this.icon =  icon;
    }

    public boolean hasMaterialRequirement(PlayerEntity player, int amount) {
        return materials.stream().allMatch(m -> m.isMet(player, amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sublisting<?> that = (Sublisting<?>) o;
        return Objects.equals(sub_id, that.sub_id) && Objects.equals(listing, that.listing);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sub_id, listing);
    }
}
