package uk.joshiejack.shopaholic.network;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;
import uk.joshiejack.shopaholic.shop.Department;
import uk.joshiejack.shopaholic.shop.input.ShopInputBlockState;
import uk.joshiejack.shopaholic.shop.input.ShopInputEntity;
import uk.joshiejack.shopaholic.shop.input.ShopInputItem;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class OpenShopPacket extends AbstractPacketSyncDepartment {
    private ShopTarget target;
    private int entityID;

    public OpenShopPacket() {}
    public OpenShopPacket(Department department, ShopTarget target) {
        super(department);
        this.target = target;
    }

    @Override
    public void encode(PacketBuffer buf) {
        super.encode(buf);
        buf.writeLong(target.pos.asLong());
        buf.writeInt(target.entity.getId());
        buf.writeItemStack(target.stack, false);
        if (target.input instanceof ShopInputBlockState) buf.writeByte(0);
        else if (target.input instanceof ShopInputEntity) buf.writeByte(1);
        else buf.writeByte(2);
        target.input.encode(buf);
    }

    @Override
    public void decode(PacketBuffer buf) {
        super.decode(buf);
        target = new ShopTarget();
        target.pos = BlockPos.of(buf.readLong());
        entityID = buf.readInt();
        target.stack = buf.readItem();
        byte type = buf.readByte();
        target.input = (type == 0 ? new ShopInputBlockState(buf) : type == 1 ? new ShopInputEntity(buf) : new ShopInputItem(buf));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    protected void handleClientPacket() {
        Minecraft mc = Minecraft.getInstance();
        PlayerEntity player = mc.player;
        target.world = player.level;
        target.entity = player.level.getEntity(entityID);
        target.player = player;
        //TODO? mc.setScreen(new ShopScreen(department, target));
    }
}