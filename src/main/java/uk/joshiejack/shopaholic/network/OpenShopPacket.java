package uk.joshiejack.shopaholic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.api.shop.ShopInput;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.input.ShopInputBlockState;
import uk.joshiejack.shopaholic.shop.input.ShopInputEntity;
import uk.joshiejack.shopaholic.shop.input.ShopInputItem;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class OpenShopPacket extends AbstractPacketSyncDepartment {
    //private ShopTarget target;
    private BlockPos pos;
    private ItemStack stack;
    private ShopInput<?> input;
    private int entityID;

    public OpenShopPacket() {}
    public OpenShopPacket(Department department, ShopTarget target) {
        super(department);
        this.pos = target.getPos();
        this.stack = target.getStack();
        this.entityID = target.getEntity().getId();
        this.input = target.getInput();
    }

    @Override
    public void encode(PacketBuffer buf) {
        super.encode(buf);
        buf.writeLong(pos.asLong());
        buf.writeInt(entityID);
        buf.writeItemStack(stack, false);
        if (input instanceof ShopInputBlockState) buf.writeByte(0);
        else if (input instanceof ShopInputEntity) buf.writeByte(1);
        else buf.writeByte(2);
        input.encode(buf);
    }

    @Override
    public void decode(PacketBuffer buf) {
        super.decode(buf);
        pos = BlockPos.of(buf.readLong());
        entityID = buf.readInt();
        stack = buf.readItem();
        byte type = buf.readByte();
        input = (type == 0 ? new ShopInputBlockState(buf) : type == 1 ? new ShopInputEntity(buf) : new ShopInputItem(buf));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleClientPacket() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        ShopTarget target = new ShopTarget(player.level, pos, player.level.getEntity(entityID), player, stack, input);
        //TODO? mc.setScreen(new ShopScreen(department, target));
    }
}