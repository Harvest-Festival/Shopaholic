package uk.joshiejack.shopaholic.network;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import uk.joshiejack.penguinlib.network.packet.AbstractSyncCompoundNBTPacket;
import uk.joshiejack.penguinlib.util.PenguinLoader;
import uk.joshiejack.penguinlib.util.helpers.generic.StringHelper;
import uk.joshiejack.shopaholic.client.Shipped;
import uk.joshiejack.shopaholic.client.gui.ShopScreen;
import uk.joshiejack.shopaholic.shipping.Shipping;

import java.util.Set;

@PenguinLoader.Packet(NetworkDirection.PLAY_TO_CLIENT)
public class ShipPacket extends AbstractSyncCompoundNBTPacket {
    public ShipPacket(){}
    public ShipPacket(CompoundNBT tag) {
        super(tag);
    }

    @Override
    public void handle(PlayerEntity player) {
        Set<Shipping.SoldItem> toSell = Sets.newHashSet();
        Shipping.readHolderCollection(tag.getList("ToSell", 10), toSell);
        MinecraftForge.EVENT_BUS.register(new TrackingRenderer(toSell));
        //Merge in the newly sold items to the sold list
        Set<Shipping.SoldItem> merged = Sets.newHashSet();
        for (Shipping.SoldItem holder: toSell) {
            for (Shipping.SoldItem sold : Shipped.getSold()) {
                if (sold.matches(holder.getStack())) {
                    sold.merge(holder); //Merge in the holder
                    merged.add(holder);
                }
            }
        }

        Shipped.clearCountCache();
        toSell.stream().filter(s -> !merged.contains(s)).forEach(r -> Shipped.getSold().add(r));
    }

    @SuppressWarnings("unused") //TODO: Add sound effect for shipping, "kerching"
    public static class TrackingRenderer {
        private boolean loading;
        private int ticker = 0;
        private int yOffset = 0;
        private long last;
        private final Set<Shipping.SoldItem> sold;

        TrackingRenderer(Set<Shipping.SoldItem> sold) {
            this.sold = sold;
            this.loading = true;
        }

        private void renderAt(Minecraft mc, MatrixStack matrix, Shipping.SoldItem stack, int x, int y) {
            mc.getItemRenderer().renderGuiItem(stack.getStack(), x + 4, y - 24);
            //TODO? SCALE UP? StackRenderHelper.drawStack(stack.getStack(), x + 4, y - 24, 1.25F);
            mc.getTextureManager().bind(ShopScreen.EXTRA);
            mc.gui.blit(matrix, x + 30, y - 16, 244, 244, 12, 12);
            String text = StringHelper.convertNumberToString(stack.getValue());
            mc.font.drawShadow(matrix, text, x + 44, y - 13, 0xFFFFFFFF);
        }

        private boolean hasFinishedOrUpdateTickerUp() {
            long current = System.currentTimeMillis();
            if (current - last >= 30) {
                if (yOffset + 1 >= sold.size() * 20) {
                    if (current - last >= 1500) {
                        last = System.currentTimeMillis();
                        return true;
                    }
                } else {
                    last = System.currentTimeMillis();
                    yOffset++;
                }
            }

            return false;
        }

        private void moveItemsDown() {
            long current = System.currentTimeMillis();
            if (current - last >= 100) {
                if (yOffset <= 0) {
                    MinecraftForge.EVENT_BUS.unregister(this);
                } else {
                    last = System.currentTimeMillis();
                    yOffset -= 2;
                }
            }
        }

        @SubscribeEvent
        public void onGuiRender(RenderGameOverlayEvent.Pre event) {
            if (event.getType() == RenderGameOverlayEvent.ElementType.HOTBAR) {
                int maxHeight = event.getWindow().getGuiScaledHeight();
                Minecraft mc = Minecraft.getInstance();
                event.getMatrixStack().pushPose();
                int y = 0;
                int currentY = maxHeight + (20 * sold.size()) - yOffset;
                for (Shipping.SoldItem stack: sold) {
                    renderAt(mc, event.getMatrixStack(), stack, 0, currentY - y);
                    y += 20; //Increase the y
                }

                event.getMatrixStack().popPose();
                if (loading && hasFinishedOrUpdateTickerUp()) loading = false;
                else if (!loading) moveItemsDown();
            }
        }
    }
}
