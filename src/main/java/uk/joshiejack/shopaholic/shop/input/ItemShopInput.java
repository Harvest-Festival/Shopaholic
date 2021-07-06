package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class ItemShopInput extends ShopInput<Item> {
    public ItemShopInput(Item item) {
        super(item);
    }

    public ItemShopInput(PacketBuffer buf) {
        super(buf.readRegistryIdSafe(Item.class));
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean hasTag(ShopTarget target, String key, String value) {
        return target.getStack().hasTag() && target.getStack().getTag().getString(key).equals(value);
    }
}
