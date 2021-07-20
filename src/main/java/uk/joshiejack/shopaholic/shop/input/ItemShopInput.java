package uk.joshiejack.shopaholic.shop.input;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.Lazy;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class ItemShopInput extends ShopInput<Item> {
    private final Lazy<ItemStack> asStack = Lazy.of(() -> new ItemStack(id));

    public ItemShopInput(Item item) {
        super(item);
    }

    public ItemShopInput(PacketBuffer buf) {
        super(buf.readRegistryIdSafe(Item.class));
    }

    @Override
    public String getName(ShopTarget target) {
        return asStack.get().getHoverName().getString();
    }
}