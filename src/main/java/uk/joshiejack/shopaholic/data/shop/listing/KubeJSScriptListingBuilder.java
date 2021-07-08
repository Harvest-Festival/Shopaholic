package uk.joshiejack.shopaholic.data.shop.listing;

import uk.joshiejack.shopaholic.data.ShopaholicDatabase;

public class KubeJSScriptListingBuilder extends SublistingBuilder<KubeJSScriptListingBuilder> {
    public KubeJSScriptListingBuilder(String script) {
        super("kubejs", script);
    }

    @Override
    public void save(ShopaholicDatabase data) {}
}
