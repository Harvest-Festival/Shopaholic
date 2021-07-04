package uk.joshiejack.shopaholic.shop;

import com.google.common.collect.Maps;
import joptsimple.internal.Strings;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.helpers.StringHelper;
import uk.joshiejack.penguinlib.util.helpers.TimeHelper;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.Shopaholic;
import uk.joshiejack.shopaholic.api.shop.Condition;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.client.ClientStockLevels;
import uk.joshiejack.shopaholic.inventory.DepartmentContainer;
import uk.joshiejack.shopaholic.network.shop.SetPlayerShopSeed;
import uk.joshiejack.shopaholic.network.shop.SyncStockLevelsPacket;
import uk.joshiejack.shopaholic.shop.input.BlockShopInput;
import uk.joshiejack.shopaholic.shop.input.EntityShopInput;
import uk.joshiejack.shopaholic.shop.input.InputMethod;
import uk.joshiejack.shopaholic.shop.inventory.Inventory;
import uk.joshiejack.shopaholic.shop.inventory.Stock;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;

public class Department {
    private static final ITextComponent DEFAULT_OUT_OF = new TranslationTextComponent(Shopaholic.MODID + ".shop.outof");
    public static final Map<String, Department> REGISTRY = Maps.newHashMap();
    private final Map<String, Listing> listings = Maps.newLinkedHashMap();
    private final NonNullList<Condition> conditions = NonNullList.create();
    private final String id;
    private final Shop shop;
    private Icon icon;
    private ITextComponent name;
    private final InputMethod method;
    private ITextComponent outof;

    public Department(Shop shop, String department_id, InputMethod method) {
        this.id = department_id;
        this.shop = shop;
        this.name = new StringTextComponent(Strings.EMPTY);
        this.outof = DEFAULT_OUT_OF;
        this.icon = ItemIcon.EMPTY;
        this.method = method;
        this.shop.getDepartments().add(this);
        Department.REGISTRY.put(department_id, this);
    }

    public Stock getStockLevels(World world) {
        return world.isClientSide ? ClientStockLevels.getStock(this) : Inventory.getStock((ServerWorld) world, this);
    }

    @Nullable
    public Shop getShop() {
        return shop;
    }

    public String id() {
        return id;
    }

    public Department setName(String name) {
        if (name.contains(":")) {
            String[] split = StringHelper.decompose(name, ':');
            this.name = new TranslationTextComponent(split[0] + ".shop.department." + split[1] + ".name");
            this.outof = new TranslationTextComponent(split[0] + ".shop.department." + split[1] + ".outof");
        } else this.name = new TranslationTextComponent(name);

        return this;
    }

    public Department setIcon(Icon icon) {
        this.icon = icon;
        return this;
    }

    public void addListing(Listing listing) {
        listings.put(listing.id(), listing);
    }

    public void addCondition(Condition condition) {
        this.conditions.add(condition);
    }

    public Listing getListingByID(String id) {
        return listings.get(id);
    }

    public Collection<Listing> getListings() {
        return listings.values();
    }

    public Icon getIcon() {
        return icon;
    }

    public ITextComponent getLocalizedName() {
        return name;
    }

    public ITextComponent getOutofText() {
        ITextComponent translated = outof;
        return outof.getContents().equals(translated.getString()) ? StringHelper.localize(Shopaholic.MODID + ".shop.outof") : translated;
    }

    public boolean isValidFor(ShopTarget target, Condition.CheckType type, InputMethod method) {
        return this.method == method && isValidFor(target, type);
    }

    public boolean isValidFor(ShopTarget target, Condition.CheckType type) {
        return conditions.stream().allMatch(condition -> condition.valid(target, type));
    }

    public void open(ShopTarget target, boolean reloadLastDepartment) {
        //Sync supermarket inventories too
        Shop market = Shop.get(this);
        if (market != null) {
            market.getDepartments().forEach(department ->
                    PenguinNetwork.sendToClient(new SyncStockLevelsPacket(department, department.getStockLevels(target.getWorld())), (ServerPlayerEntity) target.getPlayer()));
        } else
            PenguinNetwork.sendToClient(new SyncStockLevelsPacket(this, getStockLevels(target.getWorld())), (ServerPlayerEntity) target.getPlayer()); //Sync the stock levels to the player
        /* Seed the random shopness */
        int seed = 13 * (1 + TimeHelper.getElapsedDays(target.getWorld().getDayTime()));
        target.getPlayer().getPersistentData().putInt("ShopaholicSeed", seed);
        PenguinNetwork.sendToClient(new SetPlayerShopSeed(seed), (ServerPlayerEntity) target.getPlayer());
        /* Open the shop */ //Open after the stock level has been received
        NetworkHooks.openGui((ServerPlayerEntity) target.getPlayer(),
                new SimpleNamedContainerProvider((id, inv, data) ->
                        new DepartmentContainer(id, inv.player, null)
                                .withData(this, target, reloadLastDepartment), name), buf -> {
                    buf.writeBoolean(reloadLastDepartment);
                    buf.writeUtf(id);
                    buf.writeVarLong(target.getPos().asLong());
                    buf.writeVarInt(target.getEntity().getId());
                    buf.writeItemStack(target.getStack(), false);
                    if (target.getInput() instanceof BlockShopInput) buf.writeByte(0);
                    else if (target.getInput() instanceof EntityShopInput) buf.writeByte(1);
                    else buf.writeByte(2);
                    target.getInput().encode(buf);
                });
    }
}
