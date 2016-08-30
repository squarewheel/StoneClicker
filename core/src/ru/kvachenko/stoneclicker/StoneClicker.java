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
import ru.kvachenko.stoneclicker.database.DB;
import ru.kvachenko.stoneclicker.database.Result;
import ru.kvachenko.stoneclicker.screens.GameScreen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.TreeMap;

public class StoneClicker extends Game {
    private DB db;                          // Database connection
    private StonesCounter score;
    private StonesCounter stonesPerSecond;
    private StonesCounter clickPower;
    private float timeElapsed;
    private ArrayList<Upgrade> upgradesList;

    public StoneClicker(DB databaseConnection) {
        super();
        db = databaseConnection;
    }

    @Override
	public void create () {
        score = new StonesCounter();
        stonesPerSecond = new StonesCounter();
        clickPower = new StonesCounter(1);
        timeElapsed = 0;
        upgradesList = new ArrayList<Upgrade>();

        // Get upgrades list from DB
        Result res = db.query("SELECT * FROM upgrades;");
        while (res.next()) {
            upgradesList.add(
                    new Upgrade(res.getString(res.findColumn("name")),
                        new BigDecimal(res.getInt(res.findColumn("base_cost"))),
                        new BigDecimal(res.getInt(res.findColumn("click_power_bonus"))),
                        new BigDecimal(res.getInt(res.findColumn("stones_per_second_bonus"))))
            );
        }

		setScreen(new GameScreen(this));
	}

	@Override
	public void render () {
        // Each second add stonesPerSecond value to score
        timeElapsed += Gdx.graphics.getDeltaTime();
        if (timeElapsed >= 1) {
            score.addStones(stonesPerSecond.getCounter());
            timeElapsed = 0;
        }

		super.render();
	}
	
	@Override
	public void dispose () {
        // TODO: game dispose
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

    public ArrayList<Upgrade> getUpgradesList() {
        return upgradesList;
    }

}
