package uk.joshiejack.shopaholic.shop.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.ShopHelper;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class InputToShop {
    public static final Multimap<BlockStateShopInput, Department> BLOCKSTATE_TO_SHOP = HashMultimap.create();
    public static final Multimap<EntityShopInput, Department> ENTITY_TO_SHOP = HashMultimap.create();
    public static final Multimap<ItemShopInput, Department> ITEM_TO_SHOP = HashMultimap.create();

    public static void register(String type, String data, Department department) {
        switch (type) {
            case "block":
                BLOCKSTATE_TO_SHOP.get(new BlockStateShopInput(BlockStateShopInput.fromString(data))).add(department);
                break;
            case "entity":
                ENTITY_TO_SHOP.get(new EntityShopInput(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data)))).add(department);
                break;
            case "item":
                ITEM_TO_SHOP.get(new ItemShopInput(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data))))).add(department); //TODO: STACKS
                break;
        }
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isClientSide) return;
        BlockStateShopInput input = new BlockStateShopInput(event.getWorld().getBlockState(event.getPos()));
        ShopHelper.open(BLOCKSTATE_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getWorld().isClientSide) return;
        EntityShopInput input = new EntityShopInput(event.getTarget());
        ShopHelper.open(ENTITY_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getTarget(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isClientSide) return;
        ItemShopInput input = new ItemShopInput(event.getItemStack());
        ShopHelper.open(ITEM_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }
}
