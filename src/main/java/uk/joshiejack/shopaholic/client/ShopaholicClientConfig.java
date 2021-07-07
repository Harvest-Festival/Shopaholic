package uk.joshiejack.shopaholic.client;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShopaholicClientConfig {
    public static ForgeConfigSpec.BooleanValue enableShippingTicker;
    public static ForgeConfigSpec.BooleanValue enableClockHUD;
    public static ForgeConfigSpec.BooleanValue enableGoldHUD;
    public static ForgeConfigSpec.BooleanValue enableGoldIconHUD;
    public static ForgeConfigSpec.BooleanValue enableInventoryView;
    public static ForgeConfigSpec.IntValue goldHUDX;
    public static ForgeConfigSpec.IntValue goldHUDY;
    public static ForgeConfigSpec.EnumValue<GoldRenderSide> goldRenderSide;
    public static ForgeConfigSpec.BooleanValue enableSellValueTooltip;

    ShopaholicClientConfig(ForgeConfigSpec.Builder builder) {
        builder.push("General");
        enableInventoryView = builder.define("Enable inventory view in shops", true);
        enableShippingTicker = builder.define("Enable shipping daily ticker", true);
        enableSellValueTooltip = builder.define("Enable sell value in tooltips", false);
        builder.pop();
        builder.push("HUD");
        enableClockHUD = builder.define("Enable clock HUD", true);
        enableGoldHUD = builder.define("Enable gold HUD", true);
        enableGoldIconHUD = builder.define("Enable gold icon in HUD", true);
        goldHUDX = builder.defineInRange("Gold HUD X Offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        goldHUDY = builder.defineInRange("Gold HUD Y Offset", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        goldRenderSide = builder.defineEnum("Gold HUD render side", GoldRenderSide.LEFT);
        builder.pop();
    }

    public static ForgeConfigSpec create() {
        return new ForgeConfigSpec.Builder().configure(ShopaholicClientConfig::new).getValue();
    }

    public enum GoldRenderSide {
        LEFT, RIGHT
    }
}
