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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import ru.kvachenko.stoneclicker.screens.GameScreen;

public class StoneClicker extends Game {
    private StonesCounter score;
    private StonesCounter stonesPerSecond;
    private StonesCounter clickPower;
    private float timeElapsed;

	@Override
	public void create () {
        score = new StonesCounter();
        stonesPerSecond = new StonesCounter();
        clickPower = new StonesCounter(1);
        timeElapsed = 0;

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
        timeElapsed += Gdx.graphics.getDeltaTime();
        if (timeElapsed >= 1) {
            score.addStones(stonesPerSecond.getCounter());
            timeElapsed = 0;
        }

		super.render();
	}
	
	@Override
	public void dispose () {

	}

    public StonesCounter getScore() {
        return score;
    }

    public StonesCounter getStonesPerSecond() {
        return stonesPerSecond;
    }

    public StonesCounter getClickPower() {
        return clickPower;
    }

/*

    public void addStonesPerSecond(BigDecimal val) {
        stonesPerSecond = stonesPerSecond.add(val);
    }

    public void addClickPower(BigDecimal val) {
        clickPower = clickPower.add(val);
    }

    public void removeStonesPerSecond(BigDecimal val) {
        stonesPerSecond = stonesPerSecond.subtract(val);
    }

    public void removeClickPower(BigDecimal val) {
        clickPower = clickPower.subtract(val);
    }
*/


}
