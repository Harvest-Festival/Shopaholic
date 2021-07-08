package uk.joshiejack.shopaholic.shop.listing;

import joptsimple.internal.Strings;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ICommandSource;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ReportedException;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.Lazy;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;
import uk.joshiejack.penguinlib.util.icon.ItemIcon;
import uk.joshiejack.shopaholic.api.shop.ListingHandler;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public class CommandListingHandler implements ICommandSource, ListingHandler<String> {
    private static final Lazy<Icon> ICON = Lazy.of(() -> new ItemIcon(Items.COMMAND_BLOCK));
    @Override
    public String getObjectFromDatabase(DatabaseLoadedEvent database, String data) {
        return data;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ITextComponent getDisplayName(String command) {
        return new StringTextComponent(command);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Icon createIcon(String command) {
        return ICON.get();
    }

    private CommandSource createCommandSourceStack(PlayerEntity player) {
        return new CommandSource(this, player.position(), player.getRotationVector(), (ServerWorld) player.level, 2, player.getName().getString(),
                player.getDisplayName(), Objects.requireNonNull(player.level.getServer()), null);
    }

    @Override
    public void purchase(PlayerEntity player, String command) {
        if (!player.level.isClientSide && !Strings.isNullOrEmpty(command)) {
            MinecraftServer minecraftserver = player.level.getServer();
            try {
                CommandSource commandsource = createCommandSourceStack(player);
                assert minecraftserver != null;
                minecraftserver.getCommands().performCommand(commandsource, command);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Executing command purchase");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Command to be executed");
                crashreportcategory.setDetail("Command", () -> command);
                crashreportcategory.setDetail("Name", () -> player.getName().getString());
                throw new ReportedException(crashreport);
            }
        }
    }

    @Override
    public boolean isValid(Object object) {
        return object instanceof String && !((String)object).isEmpty();
    }

    @Override
    public void sendMessage(@Nonnull ITextComponent text, @Nonnull UUID uuid) {}

    @Override
    public boolean acceptsSuccess() {
        return false;
    }

    @Override
    public boolean acceptsFailure() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }
}

