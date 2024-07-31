package com.techelevator.lists;

import java.math.BigDecimal;

public class ToBuy extends ListItem {

    private BigDecimal cost;

    public ToBuy(String description, BigDecimal cost) {
        super(description, false);
        this.cost = cost;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }


    public String toString() {
        return super.getDescription() + " Estimated Cost: $" + this.cost + "\n";
    }
}
