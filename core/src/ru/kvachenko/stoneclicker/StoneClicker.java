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

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ru.kvachenko.stoneclicker.screens.GameScreen;

import java.math.BigInteger;

public class StoneClicker extends Game {
    private StonesCounter stonesCounter;
    private BigInteger stonesPerSecond;
    private BigInteger clickPower;
    private float timeElapsed;

	@Override
	public void create () {
        stonesCounter = new StonesCounter();
        stonesPerSecond = new BigInteger("0");
        clickPower = new BigInteger("1");
        timeElapsed = 0;

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
        timeElapsed += Gdx.graphics.getDeltaTime();
        if (timeElapsed >= 1) {
            stonesCounter.addStones(stonesPerSecond);
            timeElapsed = 0;
        }

		super.render();
	}
	
	@Override
	public void dispose () {

	}

    public StonesCounter getStonesCounter() {
        return stonesCounter;
    }

    public BigInteger getStonesPerSecond() {
        return stonesPerSecond;
    }

    public BigInteger getClickPower() {
        return clickPower;
    }

}
