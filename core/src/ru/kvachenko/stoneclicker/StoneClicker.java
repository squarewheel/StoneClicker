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
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Base64Coder;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import com.badlogic.gdx.utils.TimeUtils;
import ru.kvachenko.stoneclicker.database.DB;
import ru.kvachenko.stoneclicker.database.Result;
import ru.kvachenko.stoneclicker.screens.GameScreen;
import ru.kvachenko.stoneclicker.screens.LoadingScreen;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;

public class StoneClicker extends Game {
    private static class SaveGameDescriptor {
        private long timestamp;
        private String score = null;
        private float timeElapsed = 0;
        private ArrayList<Upgrade.UpgradeSaveDescriptor> upgradesList  = new ArrayList<Upgrade.UpgradeSaveDescriptor>();

//        SaveGameDescriptor() {
//            score = null;
//            timeElapsed = 0;
//            upgradesList = new ArrayList<Upgrade.UpgradeSaveDescriptor>();
//        }
    }

    private DB db;                                  // Database connection
    private Json json;                              // Json for save and load game
    private SaveGameDescriptor gameSaveDescriptor;  // Object for JSON
    private FileHandle gameSaveFile;                // File for saving game state
    private StonesCounter score;                    // Total amount of stones
    private StonesCounter stonesPerSecond;          //
    private StonesCounter clickPower;               // Num of stones given by one click
    private float timeElapsed;                      //
    private float autoSaveTimer;                    //
    private ArrayList<Upgrade> upgradesList;        //
    private LinkedList<String> messagesList;        // Messages for shown on game screen

    public StoneClicker(DB databaseConnection) {
        super();
        db = databaseConnection;
    }

    @Override
	public void create () {
        setScreen(new LoadingScreen());

        json = new Json();
        json.setOutputType(JsonWriter.OutputType.json);
        gameSaveFile = Gdx.files.local("save.json");

        stonesPerSecond = new StonesCounter();
        clickPower = new StonesCounter(1);
        autoSaveTimer = 0;
        upgradesList = new ArrayList<Upgrade>();
        messagesList = new LinkedList<String>();

        // Check saved game and load if save file exists
        if (gameSaveFile.exists()) loadGame();
        else newGame();

        GameScreen gameScreen = new GameScreen(this);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
        // Each second add stonesPerSecond value to score
        timeElapsed += Gdx.graphics.getDeltaTime();
        if (timeElapsed >= 1) {
            score.addStones(stonesPerSecond.getCounter());
            timeElapsed = 0;
        }

        // Auto save game every minute
        autoSaveTimer += Gdx.graphics.getDeltaTime();
        if (autoSaveTimer >= 60) {
            autoSaveTimer = 0;
            saveGame();
        }

		super.render();
	}
	
	@Override
	public void dispose () {
        // TODO: game dispose
	}

    public void newGame() {
        gameSaveDescriptor = new SaveGameDescriptor();
        score = new StonesCounter();
        timeElapsed = 0;

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
    }

    public void saveGame() {
        gameSaveDescriptor.timestamp = TimeUtils.millis();
        gameSaveDescriptor.score = score.getCounter().toString();
        gameSaveDescriptor.timeElapsed = timeElapsed;
        if (gameSaveDescriptor.upgradesList != null) gameSaveDescriptor.upgradesList.clear();
        for (Upgrade u: upgradesList) gameSaveDescriptor.upgradesList.add(u.getSaveDescriptor());
        gameSaveFile.writeString(Base64Coder.encodeString(json.toJson(gameSaveDescriptor)), false);
        //System.out.println("game saved");
    }

    public void loadGame() {
        gameSaveDescriptor = json.fromJson(SaveGameDescriptor.class, Base64Coder.decodeString(gameSaveFile.readString()));
        //System.out.println(json.prettyPrint(gameSaveDescriptor));
        score = new StonesCounter(new BigDecimal(gameSaveDescriptor.score));
        timeElapsed = gameSaveDescriptor.timeElapsed;

        for (Upgrade.UpgradeSaveDescriptor u: gameSaveDescriptor.upgradesList) {
            Upgrade upgrade = new Upgrade(u.name, new BigDecimal(u.baseCost), new BigDecimal(u.powerBonus), new BigDecimal(u.SPSBonus));
            upgrade.setAmount(u.amount);
            clickPower.addStones(upgrade.getPowerBonus().multiply(new BigDecimal(upgrade.getAmount())));
            stonesPerSecond.addStones(upgrade.getSPSBonus().multiply(new BigDecimal(upgrade.getAmount())));
            upgradesList.add(upgrade);
        }

        double offlineTime = (TimeUtils.millis() - gameSaveDescriptor.timestamp) / 1000;
        BigDecimal offlineBonus = stonesPerSecond.getCounter().multiply(new BigDecimal(offlineTime));
        messagesList.addLast("Offline Bonus: " + offlineBonus.toString());
        //System.out.println("offline time: " + offlineTime);
        //System.out.println("offline bonus: " + offlineBonus);
        score.addStones(offlineBonus);
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

    public LinkedList<String> getMessagesList() {
        return messagesList;
    }
}
