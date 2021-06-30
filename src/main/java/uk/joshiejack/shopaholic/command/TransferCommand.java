package uk.joshiejack.shopaholic.command;

//
//public class TransferCommand extends ShopaholicCommands {
//    @Nonnull
//    @Override
//    public String getName() {
//        return "transfer";
//    }
//
//    @Nonnull
//    @Override
//    public String getUsage(@Nonnull ICommandSender sender) {
//        return MODID + ".commands.transfer.usage";
//    }
//
//    @Override
//    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
//        if (sender instanceof PlayerEntity && args.length == 1) {
//            PlayerEntity player = (EntityPlayer) sender;
//            PenguinTeam team = PenguinTeams.getTeamForPlayer(player);
//            UUID playerUUID = PlayerHelper.getUUIDForPlayer(player);
//            long transfer = Long.parseLong(args[0]); //We take the maximum we can
//            if (transfer > 0) {
//                boolean shared = player.getEntityData().hasKey(Shopaholic.MODID + "Settings") &&
//                        player.getEntityData().getCompound(Shopaholic.MODID + "Settings").getBoolean("SharedGold"); //Player's Shared gold status
//                Vault teamVault = Bank.get(sender.getEntityWorld()).getVaultForTeam(team.getID());
//                Vault playerVault = Bank.get(sender.getEntityWorld()).getVaultForTeam(playerUUID);
//                if (shared) {
//                    long actual = Math.min(transfer, playerVault.getBalance());
//                    playerVault.personal().setBalance(player.level, playerVault.getBalance() - actual);
//                    teamVault.shared().setBalance(player.level, teamVault.getBalance() + actual);
//                } else {
//                    long actual = Math.min(transfer, teamVault.getBalance());
//                    teamVault.shared().setBalance(player.level, teamVault.getBalance() - actual);
//                    playerVault.personal().setBalance(player.level, playerVault.getBalance() + actual);
//                }
//            }
//        }
//    }
//}
