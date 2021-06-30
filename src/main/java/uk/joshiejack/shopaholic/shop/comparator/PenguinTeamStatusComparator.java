package uk.joshiejack.shopaholic.shop.comparator;

import uk.joshiejack.penguinlib.data.database.Row;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;
import uk.joshiejack.shopaholic.api.shops.Comparator;
import uk.joshiejack.shopaholic.api.shops.ShopTarget;

import javax.annotation.Nonnull;

public class PenguinTeamStatusComparator extends Comparator {
    private String status;

    @Override
    public Comparator create(Row data, String id) {
        PenguinTeamStatusComparator comparator = new PenguinTeamStatusComparator();
        comparator.status = data.get("status");
        return comparator;
    }

    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return PenguinTeams.getTeamForPlayer(target.player).getData().getCompound("PenguinStatuses").getInt(status);
    }
}
