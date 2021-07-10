package uk.joshiejack.shopaholic.shop.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;

import javax.annotation.Nullable;
import java.util.Collection;

@Mod.EventBusSubscriber(modid = Shopaholic.MODID)
public class InputToShop {
    public static final Multimap<BlockShopInput, Department> BLOCK_TO_SHOP = HashMultimap.create();
    public static final Multimap<EntityShopInput, Department> ENTITY_TO_SHOP = HashMultimap.create();
    public static final Multimap<ItemShopInput, Department> ITEM_TO_SHOP = HashMultimap.create();
    public static final Multimap<String, Department> COMMAND_TO_SHOP = HashMultimap.create();

    public static void register(String type, String data, Department department) {
        switch (type) {
            case "block":
                BLOCK_TO_SHOP.get(new BlockShopInput(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(data)))).add(department);
                break;
            case "entity":
                ENTITY_TO_SHOP.get(new EntityShopInput(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data)))).add(department);
                break;
            case "item":
                ITEM_TO_SHOP.get(new ItemShopInput(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data)))).add(department);
                break;
            case "command":
                COMMAND_TO_SHOP.get(data).add(department);
                break;
        }
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isClientSide) return;
        BlockShopInput input = new BlockShopInput(event.getWorld().getBlockState(event.getPos()).getBlock());
        if(open(BLOCK_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getWorld().isClientSide) return;
        EntityShopInput input = new EntityShopInput(event.getTarget());
        if(open(ENTITY_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getTarget(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isClientSide) return;
        ItemShopInput input = new ItemShopInput(event.getItemStack().getItem());
        if(open(ITEM_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK))
            event.setCanceled(true);
    }

    public static boolean open(Collection<Department> shops, ShopTarget target, InputMethod method) {
        Department shop = getFirstShop(shops, target, Condition.CheckType.SHOP_IS_OPEN, method);
        if (shop != null) {
            shop.open(target, true);
            return true;
        }

        return false;
    }

    @Nullable
    public static Department getFirstShop(Collection<Department> shops, ShopTarget target, Condition.CheckType type, InputMethod method) {
        for (Department shop: shops) {
            if (shop.isValidFor(target, type, method)) {
                return shop;
            }
        }

        return null;
    }
}
