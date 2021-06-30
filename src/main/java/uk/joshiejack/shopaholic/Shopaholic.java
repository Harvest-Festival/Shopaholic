package uk.joshiejack.shopaholic;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;
import uk.joshiejack.penguinlib.item.base.BookItem;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.Condition;
import uk.joshiejack.shopaholic.api.shops.ListingHandler;
import uk.joshiejack.shopaholic.inventory.EconomyManagerContainer;
import uk.joshiejack.shopaholic.inventory.ShopContainer;
import uk.joshiejack.shopaholic.loot.CapValue;
import uk.joshiejack.shopaholic.loot.RatioValue;
import uk.joshiejack.shopaholic.loot.SetValue;
import uk.joshiejack.shopaholic.shop.CostFormula;
import uk.joshiejack.shopaholic.shop.comparator.*;
import uk.joshiejack.shopaholic.shop.condition.*;
import uk.joshiejack.shopaholic.shop.handler.*;

import javax.annotation.Nonnull;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
@Mod(Shopaholic.MODID)
public class Shopaholic {
    public static final String MODID = "shopaholic";
    public static final Logger LOGGER = LogManager.getLogger();

    public Shopaholic() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        eventBus.addListener(this::setupCommon);
        ShopaholicContainers.CONTAINERS.register(eventBus);
        ShopaholicItems.ITEMS.register(eventBus);
        ShopaholicSounds.SOUNDS.register(eventBus);
    }

    private void setupCommon(FMLCommonSetupEvent event) {
        //Register Comparators
        Comparator.register("item_in_inventory", new ItemInInventoryComparator());
        Comparator.register("light_level", new LightLevelComparator());
        Comparator.register("number", new NumberComparator());
        Comparator.register("player_status", new PlayerStatusComparator());
        Comparator.register("redstone_level", new RedstoneSignalComparator());
        Comparator.register("shipped", new ShippedCountComparator());
        Comparator.register("item_tag_in_inventory", new ItemTagInInventoryComparator());
        Comparator.register("penguin_team_status", new PenguinTeamStatusComparator());
        Comparator.register("block_tag_on_target", new BlockTagOnTargetComparator());
        //Register Conditions
        Condition.register("and", new AndCondition());
        Condition.register("compare", new CompareCondition());
        Condition.register("has_nbt_tag", new HasNBTTagCondition());
        Condition.register("dimension", new InDimensionCondition());
        Condition.register("entity_nearby", new EntityNearbyCondition());
        Condition.register("has_pet", new HasPetCondition());
        Condition.register("hours", new OpeningHoursCondition());
        Condition.register("named", new NamedCondition());
        Condition.register("or", new OrCondition());
        Condition.register("per_player", new PerPlayerCondition());
        Condition.register("shipped", new ShippedCondition());
        Condition.register("time", new TimeCondition());
        //Register Listing Handlers
        ListingHandler.register("bundle", new BundleListingHandler());
        ListingHandler.register("department", new DepartmentListingHandler());
        ListingHandler.register("entity", new EntityListingHandler());
        ListingHandler.register("item", new ItemListingHandler());
        ListingHandler.register("potion", new PotionEffectListingHandler());
        //Register Listing Builders
        //TODO? ListingBuilder.register("food", new FoodBuilder());
        //Cost Formulae
        CostFormula.register("default", (m, listing, level, mechanic, rand) -> listing.getGold());
    }

    @SubscribeEvent
    public static void registerLootData(RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(MODID, "cap_value"), CapValue.TYPE);
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(MODID, "ratio_value"), RatioValue.TYPE);
        Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(MODID, "set_value"), SetValue.TYPE);
    }

    public static class ShopaholicContainers {
        public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Shopaholic.MODID);
        public static final RegistryObject<ContainerType<AbstractBookContainer>> BOOK = CONTAINERS.register("economy_manager", () -> IForgeContainerType.create((id, inv, data) -> new EconomyManagerContainer(id)));
        public static final RegistryObject<ContainerType<ShopContainer>> SHOP = CONTAINERS.register("shop", () -> IForgeContainerType.create((id, inv, data) -> new ShopContainer(id)));
    }

    public static class ShopaholicItems {
        public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Shopaholic.MODID);
        public static final RegistryObject<Item> ECONOMY_MANAGER = ITEMS.register("economy_manager", () -> new BookItem(new Item.Properties().stacksTo(1).tab(ItemGroup.TAB_MISC), ShopaholicContainers.BOOK::get));
    }

    public static class ShopaholicSounds {
        public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);
        public static final RegistryObject<SoundEvent> KERCHING = createSoundEvent("kerching");

        private static RegistryObject<SoundEvent> createSoundEvent(@Nonnull String name) {
            return SOUNDS.register(name, () -> new SoundEvent(new ResourceLocation(MODID, name)));
        }
    }

//
//
//    @Mod.Instance(MODID)
//    public static Shopaholic instance;
//
//    @SidedProxy
//    public static ServerProxy proxy;
//
//    public static class ServerProxy { public void postInit() {}}
//    @SideOnly(Dist.CLIENT)
//    public static class ClientProxy extends ServerProxy {
//        @Override
//        public void postInit() {
//            Page.REGISTRY.put("economy_manager", PageEconomyManager.INSTANCE);
//        }
//    }
//
//    @Mod.EventHandler
//    public void preInit(FMLPreInitializationEvent event) {
//        EconomyAPI.instance = this;
//        logger = event.getModLog();
//        NetworkRegistry.INSTANCE.registerGuiHandler(instance, instance);
//    }
//
//    @Mod.EventHandler
//    public void init(FMLInitializationEvent event) {
//        LootFunctionManager.registerFunction(new SetValue.Serializer());
//        LootFunctionManager.registerFunction(new RatioValue.Serializer());
//        LootFunctionManager.registerFunction(new CapValue.Serializer());
//    }
//
//    @Mod.EventHandler
//    public void postInit(FMLPostInitializationEvent event) {
//        proxy.postInit();
//    }
//
//    @SubscribeEvent
//    public static void onPlayerJoinedWorld(PlayerEvent.PlayerLoggedInEvent event) {
//        Bank.get(event.getPlayer().level).syncToPlayer(event.getPlayer());
//        Market.get(event.getPlayer().level).getShippingForPlayer(event.getPlayer()).syncToPlayer(event.getPlayer());
//    }
//
//    @Nullable
//    @Override
//    public Object getServerGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
//        return null;
//    }
//
//    @Nullable
//    @Override
//    public Object getClientGuiElement(int ID, PlayerEntity player, World world, int x, int y, int z) {
//        return new GuiManager();
//    }
//
//    @Override
//    public IVault getVaultForPlayer(World world, PlayerEntity player) {
//        return Bank.get(world).getVaultForPlayer(player);
//    }
}

