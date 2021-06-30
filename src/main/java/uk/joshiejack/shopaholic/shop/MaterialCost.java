package uk.joshiejack.shopaholic.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.penguinlib.util.helpers.PlayerHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MaterialCost {
    private static final Random rand = new Random();
    private final Ingredient ingredient;
    private int cost;

    public MaterialCost(String item, int cost) {
        this.ingredient = item.startsWith("tag:") ? Ingredient.of(ItemTags.createOptional(new ResourceLocation(item.replace("tag:", ""))))
                : Ingredient.of(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(item))));
        this.cost = cost;
    }

    public int getCost() {
        return cost;
    }

    public ItemStack getIcon() {
        List<ItemStack> list = Arrays.asList(ingredient.getItems());
        ItemStack icon = list.size() == 0 ? new ItemStack(Items.BAKED_POTATO) : list.get(rand.nextInt(list.size()));
        icon.setCount(cost);
        return icon;
    }

    public NonNullList<ItemStack> getStacks() {
        return Arrays.stream(ingredient.getItems())
                .collect(Collectors.toCollection(NonNullList::create));
    }

    public boolean isMet(PlayerEntity player, int amount) {
        return PlayerHelper.hasInInventory(player, ingredient, (cost * amount));
    }

    public void fulfill(PlayerEntity player) {
        PlayerHelper.takeFromInventory(player, ingredient, cost);
    }
}
