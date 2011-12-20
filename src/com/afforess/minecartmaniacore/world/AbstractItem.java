package com.afforess.minecartmaniacore.world;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

/**
 * An abstract representation of an item in MC.
 */
@Deprecated
public class AbstractItem {
    final private Item item;
    private int amount = -1;
    
    public AbstractItem(final Item item) {
        if (item == null)
            throw new UnsupportedOperationException("The item can not be null!");
        this.item = item;
    }
    
    public AbstractItem(final Item item, final int amount) {
        if (item == null)
            throw new UnsupportedOperationException("The item can not be null!");
        this.item = item;
        this.amount = amount;
    }
    
    public Item type() {
        return item;
    }
    
    public int getId() {
        return item.getId();
    }
    
    public int getData() {
        return item.getData();
    }
    
    public boolean hasData() {
        return item.hasData();
    }
    
    public boolean isInfinite() {
        return amount == -1;
    }
    
    public int getAmount() {
        return amount;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public Material toMaterial() {
        return item.toMaterial();
    }
    
    public ItemStack toItemStack() {
        final ItemStack item = this.item.toItemStack();
        if (!isInfinite()) {
            item.setAmount(getAmount());
        }
        return item;
    }
    
    public static List<AbstractItem> getItem(final int id) {
        final List<Item> list = Item.getItem(id);
        return itemListToAbstractItemList(list);
    }
    
    public static AbstractItem getItem(final int id, final int data) {
        final Item i = Item.getItem(id, data);
        if (i != null)
            return new AbstractItem(i);
        return null;
    }
    
    public static List<AbstractItem> itemListToAbstractItemList(final List<Item> list) {
        final List<AbstractItem> aList = new ArrayList<AbstractItem>(list.size());
        for (final Item i : list) {
            aList.add(new AbstractItem(i));
        }
        return aList;
    }
    
    public boolean equals(final Item item) {
        return equals(item, false);
    }
    
    public boolean equals(final Item item, final boolean allowWildcards) {
        return this.item.equals(item, allowWildcards);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof AbstractItem)
            return item.equals(((AbstractItem) o).type());
        if (o instanceof Item)
            return item.equals(o);
        return false;
    }
    
    @Override
    public String toString() {
        return type().toString();
    }
    
    public boolean isWildcard() {
        // TODO Auto-generated method stub
        return item.isWildcard;
    }
    
    public void setData(final int data) {
        item.setData(data);
    }
}