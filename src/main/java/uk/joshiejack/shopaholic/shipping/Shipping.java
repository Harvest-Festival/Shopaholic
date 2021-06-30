package uk.joshiejack.shopaholic.shipping;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;
import uk.joshiejack.penguinlib.network.PenguinNetwork;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.world.teams.PenguinTeam;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.event.ItemShippedEvent;
import uk.joshiejack.shopaholic.gold.Bank;
import uk.joshiejack.shopaholic.gold.Vault;
import uk.joshiejack.shopaholic.network.ShipPacket;
import uk.joshiejack.shopaholic.network.SyncLastSoldPacket;
import uk.joshiejack.shopaholic.network.SyncSoldPacket;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class Shipping implements INBTSerializable<CompoundNBT> {
    private final Cache<ItemStack, Integer> countCache = CacheBuilder.newBuilder().build();
    private final Market market;
    private final UUID uuid;
    private boolean shared;
    private final Set<SoldItem> lastSell = Sets.newHashSet();
    private final Set<SoldItem> toSell = Sets.newHashSet();
    private final Set<SoldItem> sold = Sets.newHashSet();

    public Shipping(Market market, UUID uuid) {
        this.market = market;
        this.uuid = uuid;
    }

    public Shipping shared() {
        this.shared = true;
        return this;
    }

    public int getCount(ItemStack stack) {
        try {
            return countCache.get(stack, () -> {
                int total = 0;
                for (SoldItem s: sold) {
                    if (s.matches(stack)) return s.getStack().getCount();
                }

                return total;
            });
        } catch (ExecutionException e) {
            return 0;
        }
    }

    public void syncToPlayer(ServerPlayerEntity player) {
        PenguinNetwork.sendToClient(new SyncSoldPacket(sold), player);
        PenguinNetwork.sendToClient(new SyncLastSoldPacket(lastSell), player);
    }

    public Set<SoldItem> getSold() {
        return sold;
    }

    public void add(ItemStack stack) {
        long value = ShippingRegistry.INSTANCE.getValue(stack);
        //Add the tax
        if (value > 0) {
            for (SoldItem sold : toSell) {
                if (sold.matches(stack)) {
                    sold.merge(stack, value * stack.getCount());
                    market.setDirty();
                    return; //Found the match, so exit
                }
            }

            toSell.add(new SoldItem(stack, value * stack.getCount()));
            market.setDirty(); //Save!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        }
    }

    public void onNewDay(ServerWorld world) {
        if (toSell.size() > 0) {
            Vault vault = shared ? Bank.get(world).getVaultForTeam(uuid).shared() : Bank.get(world).getVaultForTeam(uuid).personal();
            toSell.forEach(holder -> vault.increaseGold(world, holder.gold)); //Increase the vault for this players uuid
            CompoundNBT tag = new CompoundNBT();
            tag.put("ToSell", writeHolderCollection(toSell));
            if (shared) PenguinNetwork.sendToTeam(new ShipPacket(tag), world, uuid);
            else PenguinNetwork.sendToClient(new ShipPacket(tag), (ServerPlayerEntity) world.getPlayerByUUID(uuid));

            PenguinTeam team = shared ? PenguinTeams.getTeamFromID(world, uuid) : PenguinTeams.getTeamForPlayer(world, uuid);
            toSell.forEach(holder -> {
                MinecraftForge.EVENT_BUS.post(new ItemShippedEvent(world, team, holder.stack, holder.gold)); //Statistics go to the team, no matter what
                boolean merged = false;
                for (SoldItem sold: this.sold) {
                    if (sold.matches(holder.stack)) {
                        sold.merge(holder); //Merge in the holder
                        merged = true;
                    }
                }

                if (!merged) this.sold.add(holder); //Add the holder if it doesn't exist yet
            });

            lastSell.clear();
            lastSell.addAll(toSell); //Add everything we just sold
            if (shared) PenguinNetwork.sendToTeam(new SyncLastSoldPacket(lastSell), world, uuid); //Send the new last sold info to the clients
            else PenguinNetwork.sendToClient(new SyncLastSoldPacket(lastSell), (ServerPlayerEntity) world.getPlayerByUUID(uuid));
            countCache.invalidateAll();
            toSell.clear();
            market.setDirty();
        }
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        tag.put("LastSold", writeHolderCollection(lastSell));
        tag.put("ToSell", writeHolderCollection(toSell));
        tag.put("Sold", writeHolderCollection(sold));
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        readHolderCollection(nbt.getList("LastSold", 10), lastSell);
        readHolderCollection(nbt.getList("ToSell", 10), toSell);
        readHolderCollection(nbt.getList("Sold", 10), sold);
    }

    public static void readHolderCollection(ListNBT list, Collection<SoldItem> collection) {
        collection.clear();
        list.forEach(data -> collection.add(new SoldItem((CompoundNBT)data)));
    }

    public static ListNBT writeHolderCollection(Set<SoldItem> set) {
        ListNBT list = new ListNBT();
        set.forEach(item -> list.add(item.serializeNBT()));
        return list;
    }

    @PenguinLoader
    public static class SoldItem implements INBTSerializable<CompoundNBT> {
        private ItemStack stack;
        private long gold;

        public SoldItem(CompoundNBT data) {
            this.deserializeNBT(data);
        }

        public SoldItem(ItemStack stack, long gold) {
            this.stack = stack;
            this.gold = gold;
        }

        public ItemStack getStack() {
            return stack;
        }

        public long getValue() {
            return gold;
        }

        public boolean matches(ItemStack stack) {
            return ItemStack.isSame(stack, this.stack) && ItemStack.tagMatches(stack, this.stack);
        }

        public void merge(SoldItem holder) {
            this.stack.grow(holder.stack.getCount());
            this.gold += holder.getValue();
        }

        void merge(ItemStack stack, long value) {
            this.stack.grow(stack.getCount());
            this.gold += value;
        }

        @Override
        public CompoundNBT serializeNBT() {
            CompoundNBT tag = new CompoundNBT();
            tag.put("Item", stack.serializeNBT());
            tag.putInt("ItemCount", stack.getCount());
            tag.putLong("Gold", gold);
            return tag;
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            stack = ItemStack.of(nbt.getCompound("Item"));
            stack.setCount(nbt.getInt("ItemCount"));
            gold = nbt.getLong("Gold");
        }
    }
}
