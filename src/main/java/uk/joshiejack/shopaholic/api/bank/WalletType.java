package uk.joshiejack.shopaholic.api.bank;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.world.teams.PenguinTeams;

public enum WalletType {
    PERSONAL {
        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName(PlayerEntity player) {
            return new TranslationTextComponent("gui.shopaholic.manager.account", player.getName()).getString();
        }
    }, SHARED {
        @OnlyIn(Dist.CLIENT)
        @Override
        public String getName(PlayerEntity player) {
            return new TranslationTextComponent("gui.shopaholic.manager.account", PenguinTeams.getTeamForPlayer(player).getName()).getString();
        }
    };

    @OnlyIn(Dist.CLIENT)
    public String getName(PlayerEntity player) {
        return name();
    }
}