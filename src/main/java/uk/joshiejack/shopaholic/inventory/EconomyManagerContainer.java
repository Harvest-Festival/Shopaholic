package uk.joshiejack.shopaholic.inventory;

import uk.joshiejack.penguinlib.inventory.AbstractBookContainer;
import uk.joshiejack.shopaholic.Shopaholic;

public class EconomyManagerContainer extends AbstractBookContainer {
    public EconomyManagerContainer(int windowID) {
        super(Shopaholic.ShopaholicContainers.BOOK.get(), windowID);
    }
}
