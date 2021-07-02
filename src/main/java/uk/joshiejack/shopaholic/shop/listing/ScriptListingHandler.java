package uk.joshiejack.shopaholic.shop.listing;

//@PenguinLoader("script") //TODO: KubeJS support
//public class ScriptListingHandler extends ListingHandler<ResourceLocation> {
//    @Override
//    public String getType() {
//        return "script";
//    }
//
//    @Override
//    public ResourceLocation getObjectFromDatabase(Database database, String data) {
//        return new ResourceLocation(data.replace("/", "_"));
//    }
//
//    @Override
//    public String getStringFromObject(ResourceLocation resourceLocation) {
//        return resourceLocation.toString();
//    }
//
//    @Override
//    public boolean isValid(ResourceLocation created) {
//        return Scripting.scriptExists(created);
//    }
//
//    @Override
//    public String getValidityError() {
//        return "Script does not exist";
//    }
//
//    @Override
//    public String getDisplayName(ResourceLocation resource) {
//        return StringHelper.localize(Scripting.getResult(resource, "getDisplayName", Strings.EMPTY));
//    }
//
//    @Override
//    public ItemStack[] createIcon(ResourceLocation resource) {
//        return new ItemStack[] { Scripting.getResult(resource, "getDisplayStack", ItemStackJS.EMPTY).penguinScriptingObject };
//    }
//
//    @Override
//    public void purchase(PlayerEntity player, ResourceLocation resource) {
//        Scripting.get(resource).callFunction("purchase", player);
//    }
//}
