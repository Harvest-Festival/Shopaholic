package uk.joshiejack.shopaholic;

import net.minecraftforge.common.ForgeConfigSpec;

public class ShopaholicServerConfig {
    public static ForgeConfigSpec.LongValue minGold;
    public static ForgeConfigSpec.LongValue maxGold;
    public static ForgeConfigSpec.BooleanValue shipCommandEnabled;

    ShopaholicServerConfig(ForgeConfigSpec.Builder builder) {
        minGold = builder.defineInRange("Minimum gold", 0L, Long.MIN_VALUE, Long.MAX_VALUE);
        maxGold = builder.defineInRange("Maximum gold", Long.MAX_VALUE, Long.MIN_VALUE, Long.MAX_VALUE);
        shipCommandEnabled = builder.define("Enable '/shopaholic ship' command", true);
    }

    public static ForgeConfigSpec create() {
        return new ForgeConfigSpec.Builder().configure(ShopaholicServerConfig::new).getValue();
    }
}