package uk.joshiejack.shopaholic.api.shop;

import javax.annotation.Nonnull;

public interface Comparator {
    /**
     * Return the value of this comparator
     * Often things can be just true (1) or (0) false
     * @param target    the target object
     * @return  the value this is considered to have
     */
    int getValue(@Nonnull ShopTarget target);
}
