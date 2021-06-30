package uk.joshiejack.shopaholic.command;

//
//public class ShareCommand extends ShopaholicCommands {
//    @Nonnull
//    @Override
//    public String getName() {
//        return "share";
//    }
//
//    @Nonnull
//    @Override
//    public String getUsage(@Nonnull ICommandSender sender) {
//        return MODID + ".commands.share.usage";
//    }
//
//    private static void setShared(PlayerEntity player, boolean shared) {
//        if (!player.getEntityData().hasKey(Shopaholic.MODID + "Settings")) {
//            player.getEntityData().put(Shopaholic.MODID + "Settings", new CompoundNBT());
//        }
//
//        player.getEntityData().getCompound(Shopaholic.MODID + "Settings").setBoolean("SharedGold", shared);
//        PenguinNetwork.sendToClient(new SetActiveWalletPacket(shared), player);
//    }
//
//    @Override
//    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
//        if (args.length == 1) {
//            PlayerEntity player = getCommandSenderAsPlayer(sender);
//            if (args[0].equals("enable")) {
//                setShared(player, true);
//            } else  if (args[0].equals("disable")) {
//                setShared(player, false);
//            }
//
//            //Resync the new values
//            Bank.get(sender.getEntityWorld()).syncToPlayer(player);
//            Market.get(sender.getEntityWorld()).getShippingForPlayer(player).syncToPlayer(player);
//        }
//    }
//}
