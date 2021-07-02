package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class ItemShopInput extends ShopInput<Ingredient> {
    public ItemShopInput(ItemStack stack) {
        super(Ingredient.of(stack));
    }

    public ItemShopInput(PacketBuffer buf) {
        super(Ingredient.fromNetwork(buf));
    }

    @Override
    public void encode(PacketBuffer buf) {
        id.toNetwork(buf);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        return target.getStack().hasTag() && target.getStack().getTag().getString(key).equals(value);
    }
}
