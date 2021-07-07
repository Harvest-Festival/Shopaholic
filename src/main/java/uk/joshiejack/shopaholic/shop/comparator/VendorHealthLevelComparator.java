package uk.joshiejack.shopaholic.shop.comparator;

import net.minecraft.entity.LivingEntity;
import uk.joshiejack.shopaholic.api.shop.IImutableComparator;
import uk.joshiejack.shopaholic.api.shop.ShopTarget;

import javax.annotation.Nonnull;

public class VendorHealthLevelComparator implements IImutableComparator {
    @Override
    public int getValue(@Nonnull ShopTarget target) {
        return target.getEntity() instanceof LivingEntity ? (int) ((LivingEntity) target.getEntity()).getHealth() : 0;
    }
}