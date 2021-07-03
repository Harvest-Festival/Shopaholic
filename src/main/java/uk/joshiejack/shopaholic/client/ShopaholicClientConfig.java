package uk.joshiejack.shopaholic.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShopaholicClientConfig {
    public static ForgeConfigSpec.BooleanValue enableShippingTicker;
    public static ForgeConfigSpec.BooleanValue enableClockHUD;
    public static ForgeConfigSpec.BooleanValue enableInventoryView;

    ShopaholicClientConfig(ForgeConfigSpec.Builder builder) {
        enableShippingTicker = builder.define("Enable shipping daily ticker", true);
        enableClockHUD = builder.define("Enable clock HUD", true);
        enableInventoryView = builder.define("Enable inventory view in shops", true);
    }

    public static ForgeConfigSpec create() {
        return new ForgeConfigSpec.Builder().configure(ShopaholicClientConfig::new).getValue();
    }
}
