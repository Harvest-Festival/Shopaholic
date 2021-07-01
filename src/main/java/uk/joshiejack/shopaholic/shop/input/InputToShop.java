package uk.joshiejack.shopaholic.shop.input;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.block.Blocks;
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
    public static final Multimap<ShopInputBlockState, Department> BLOCKSTATE_TO_SHOP = HashMultimap.create();
    public static final Multimap<ShopInputEntity, Department> ENTITY_TO_SHOP = HashMultimap.create();
    public static final Multimap<ShopInputItem, Department> ITEM_TO_SHOP = HashMultimap.create();

    public static void register(String type, String data, Department department) {
        switch (type) {
            case "block":
                BLOCKSTATE_TO_SHOP.get(new ShopInputBlockState(ShopInputBlockState.fromString(data))).add(department);
                break;
            case "entity":
                ENTITY_TO_SHOP.get(new ShopInputEntity(ForgeRegistries.ENTITIES.getValue(new ResourceLocation(data)))).add(department);
                break;
            case "item":
                ITEM_TO_SHOP.get(new ShopInputItem(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation(data))))).add(department); //TODO: STACKS
                break;
        }
    }

    private static void fromString(String data) {
        //TODO:
        Blocks.DIAMOND_BLOCK.getStateDefinition().toString();
    }

    @SubscribeEvent
    public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event) {
        if (event.getWorld().isClientSide) return;
        ShopInputBlockState input = new ShopInputBlockState(event.getWorld().getBlockState(event.getPos()));
        ShopHelper.open(BLOCKSTATE_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        if (event.getWorld().isClientSide) return;
        ShopInputEntity input = new ShopInputEntity(event.getTarget());
        ShopHelper.open(ENTITY_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getTarget(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }

    @SubscribeEvent
    public static void onItemInteract(PlayerInteractEvent.RightClickItem event) {
        if (event.getWorld().isClientSide) return;
        ShopInputItem input = new ShopInputItem(event.getItemStack());
        ShopHelper.open(ITEM_TO_SHOP.get(input),
                new ShopTarget(event.getWorld(), event.getPos(), event.getEntity(), event.getPlayer(), event.getItemStack(), input),
                event.getPlayer().isShiftKeyDown() ? InputMethod.SHIFT_RIGHT_CLICK : InputMethod.RIGHT_CLICK);
    }
}
