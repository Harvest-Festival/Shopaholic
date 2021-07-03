package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemListIcon;

public class MaterialCost {
    private final Lazy<Ingredient> ingredient = Lazy.of(() -> getItem().startsWith("tag:") ? Ingredient.of(ItemTags.createOptional(new ResourceLocation(getItem().replace("tag:", ""))))
            : Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(getItem())))));
    private final Lazy<Icon> icon = Lazy.of(() -> new ItemListIcon(Lists.newArrayList(ingredient.get().getItems())));
    private final String item;
    private final int cost;

    public MaterialCost(String item, int cost) {
        this.item = item;
        this.cost = cost;
    }

    private String getItem() {
        return item;
    }

    public int getCost() {
        return cost;
    }

    public Icon getIcon() {
        return icon.get();
    }

    public boolean isMet(PlayerEntity player, int amount) {
        return PlayerHelper.hasInInventory(player, ingredient.get(), (cost * amount));
    }

    public void fulfill(PlayerEntity player) {
        PlayerHelper.takeFromInventory(player, ingredient.get(), cost);
    }
}
