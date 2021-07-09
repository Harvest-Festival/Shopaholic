package uk.joshiejack.shopaholic.shop.listing;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.tuple.Pair;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.shop.Comparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

public class TeamStatusListingHandler extends AbstractPenguinStatusListingHandler {
    @Override
    public void purchase(PlayerEntity player, Pair<String, Comparator> object) {
        CompoundNBT data = PenguinTeams.getTeamForPlayer(player).getData();
        if (!data.contains("PenguinStatuses"))
            data.put("PenguinStatuses", new CompoundNBT());
        data.getCompound("PenguinStatuses").putInt(object.getLeft(), object.getRight().getValue(ShopTarget.fromPlayer(player)));
    }
}
