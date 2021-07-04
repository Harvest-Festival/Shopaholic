package uk.joshiejack.shopaholic.api.shop;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import uk.joshiejack.penguinlib.events.DatabaseLoadedEvent;
import uk.joshiejack.penguinlib.util.icon.Icon;

import java.util.List;

public interface ListingHandler<T> {
    /**
     * Create the object from the database, we pass the data field
     * which can either be data you process or you can use it to
     * grab another field from the database system
     * @param database          the database event used to load other tables if neccessary for easier data access
     * @param data              the data field that was passed, may be an id or data itself, your choice
     * @return                  the object we created from the database fields
     */
    T getObjectFromDatabase(DatabaseLoadedEvent database, String data);

    /**
     * Return the count of this object,
     * for most items this will always be 1
     * an example where it's used is in itemstacks
     * @param object        the object this listing creates
     * @return  the count of this object
     */
    default int getCount(T object) { return 1; }

    /**
     * Called when the player purchases this item, to give them the item/do an action, or whatever it is!
     * @param player        the player
     * @param object        the object they purchased
     */
    void purchase(PlayerEntity player, T object);

    /**
     * If the object that was created is a valid one,
     * if not we will discard it. You may end trying to load things that don't exist for example
     * @param object        the object to test
     * @return  if it is valid
     */
    boolean isValid(Object object);

    /**
     * The display name of this object, used in the shop screen, if no custom name is set
     * @param object        the object to get the name of
     * @return  the name of the object
     */

    @OnlyIn(Dist.CLIENT)
    ITextComponent getDisplayName(T object);

    /**
     * Creates an icon to show in the shop
     * @param object        the object to create the icon for
     * @return  the icon for this object
     */
    @OnlyIn(Dist.CLIENT)
    Icon createIcon(T object);

    /**
     * Display the tooltip for this item, often just the name but can contain other data
     * this is only called if the item doesn't have a custom tooltip
     * @param list          the list of components to add to
     * @param object        the object to add the tooltip for
     */
    @OnlyIn(Dist.CLIENT)
    default void addTooltip(List<ITextComponent> list, T object) {}
}