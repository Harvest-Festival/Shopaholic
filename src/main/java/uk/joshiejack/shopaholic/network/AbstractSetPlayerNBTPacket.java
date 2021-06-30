package uk.joshiejack.shopaholic.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import uk.joshiejack.penguinlib.network.PenguinPacket;

public abstract class AbstractSetPlayerNBTPacket extends PenguinPacket {
    private final String tagName;

    public AbstractSetPlayerNBTPacket(String tag) {
        this.tagName = tag;
    }

    @Override
    public void handle(PlayerEntity player) {
        if (!player.getPersistentData().contains(tagName))
            player.getPersistentData().put(tagName, new CompoundNBT());
        setData(player.getPersistentData().getCompound(tagName));
    }

    protected abstract void setData(CompoundNBT compound);
}
