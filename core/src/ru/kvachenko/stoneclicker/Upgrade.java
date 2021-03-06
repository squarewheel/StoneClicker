/*
 * Copyright (c) 2016 Kvachenko A. [feedback@kvachenko.ru]
 *
 * This file is part of program StoneClicker.
 *
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.kvachenko.stoneclicker;

import java.math.BigDecimal;

/**
 * @author Sasha Kvachenko
 *         Created on 22.08.2016.
 *         <p>
 *         Upgrades buying for get stones without clicks and for incrase click power.
 */
public class Upgrade {
    static class UpgradeSaveDescriptor {
        String name;
        String baseCost;
        String currentCost;
        String powerBonus;
        String SPSBonus;
        int amount = 0;

//        public UpgradeSaveDescriptor() {
//            String name = null;
//            String baseCost = null;
//            String currentCost = null;
//            String powerBonus = null;
//            String SPSBonus = null;
//            int amount = 0;
//        }
    }

    private static final BigDecimal costMultiplier = new BigDecimal("1.15");
    private UpgradeSaveDescriptor upgradeSaveDescriptor;
    private String name;                // Name of this upgrade
    private BigDecimal baseCost;        // Base cost of this upgrade
    private BigDecimal currentCost;     // Cost of current tier of upgrade
    private BigDecimal powerBonus;      // Bonus to click power
    private BigDecimal SPSBonus;        // Bonus to stones per second
    private int amount;                 // Amount of buying upgrades of this type

    //public Upgrade() {
    //}

    Upgrade(String upgradeName, BigDecimal baseCost, BigDecimal clickPowerBonus, BigDecimal stonesPerSecondBonus) {
        upgradeSaveDescriptor = new UpgradeSaveDescriptor();
        name = upgradeName;
        this.baseCost = baseCost;
        currentCost = baseCost;
        powerBonus = clickPowerBonus;
        SPSBonus = stonesPerSecondBonus;
        amount = 0;
    }

    UpgradeSaveDescriptor getSaveDescriptor() {
        upgradeSaveDescriptor.name = name;
        upgradeSaveDescriptor.baseCost = baseCost.toString();
        upgradeSaveDescriptor.currentCost = currentCost.toString();
        upgradeSaveDescriptor.powerBonus = powerBonus.toString();
        upgradeSaveDescriptor.SPSBonus = SPSBonus.toString();
        upgradeSaveDescriptor.amount = amount;
        return upgradeSaveDescriptor;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getCost() { return currentCost; }

    public BigDecimal getPowerBonus() {
        return powerBonus;
    }

    public BigDecimal getSPSBonus() {
        return SPSBonus;
    }

    public int getAmount() {
        return amount;
    }

    void setAmount(int amount) {
        this.amount = amount;
        //if (this.amount > 0) currentCost = baseCost.multiply(costMultiplier.multiply(new BigDecimal(amount + 1)));
        if (this.amount > 0) currentCost = baseCost.multiply(costMultiplier.pow(amount + 1));
    }

    public void buy(StoneClicker gameController) {
        if (gameController.getScore().getCounter().compareTo(currentCost) < 0) return;

        gameController.getScore().reduceValue(currentCost);
        gameController.getClickPower().increaseValue(powerBonus);
        gameController.getStonesPerSecond().increaseValue(SPSBonus);
        setAmount(++amount);
    }

    public void sell(StoneClicker gameController) {
        if (amount < 1) return;

        //BigDecimal moneyback = (amount == 1) ? baseCost : baseCost.multiply(costMultiplier.multiply(new BigDecimal(amount - 1)));
        BigDecimal moneyback = (amount == 1) ? baseCost : baseCost.multiply(costMultiplier.pow(amount - 1));

        gameController.getScore().increaseValue(moneyback);
        gameController.getClickPower().reduceValue(powerBonus);
        gameController.getStonesPerSecond().reduceValue(SPSBonus);
        amount--;

        currentCost = moneyback;
    }
}
