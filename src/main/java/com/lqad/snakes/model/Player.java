package com.lqad.snakes.model;

import java.util.ArrayList;
import java.util.List;

public class Player {
    
    private final String name;
    private int position = 0;
    private final List<Ability> inventory = new ArrayList<>();

    public Player(String name) {
        this.name = name;
        
    }

    public String getName(){ return name; }
    public int getPosition(){ return position; }

    public void setNewPosition(int position){
        this.position = position; 
    }

   
    public void addAbility(Ability a) {
        inventory.add(a);
    }

    public boolean hasAbility(Ability a) {
        return inventory.contains(a);
    }

    public void useAbility(Ability a) {
        inventory.remove(a);
    }

    public List<Ability> getInventory() { // to see or list all the abilities
        return inventory;
    }
}