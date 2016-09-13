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

import com.badlogic.gdx.math.MathUtils;

import java.math.BigDecimal;

/**
 * @author Sasha Kvachenko
 *         Created on 09.09.2016.
 *         <p>
 *
 */
public class Bonus {
    private boolean active;
    private StoneClicker gameController;
    private BigDecimal bonusValue;

    public Bonus(StoneClicker s) {
        active = false;
        gameController = s;
        bonusValue = new BigDecimal(0);
    }

    public void takeBonus() {
        BigDecimal randomValue = new BigDecimal(MathUtils.random(100, 10000));
        bonusValue =  gameController.getClickPower().getCounter().multiply(randomValue);
        gameController.getScore().increaseValue(bonusValue);
        gameController.getMessagesList().addLast("Korovan Robbery" + "\n" + "Bonus " + Counter.shortedValueOf(bonusValue));
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
