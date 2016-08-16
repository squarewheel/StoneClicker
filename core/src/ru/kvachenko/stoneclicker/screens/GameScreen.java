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

package ru.kvachenko.stoneclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.stoneclicker.StoneClicker;

import java.math.BigInteger;

/**
 * @author Sasha Kvachenko
 *         Created on 15.08.2016.
 *         <p>
 *         File description.
 */
public class GameScreen implements Screen {
    private StoneClicker gameController;
    private TextureAtlas images;
    private Skin skin;
    private Image stone;
    private Stage mainStage;
    private Stage uiStage;
    private Label stonesCounterLabel;
    private Label stonesPerSecondLabel;
    private Label clickPowerLabel;
    private int screenWidth;
    private int screenHeight;
    private final int stoneMaxWidth;
    private final int stoneMaxHeight;

    public GameScreen(final StoneClicker gameController) {
        this.gameController = gameController;
        images = new TextureAtlas(Gdx.files.internal("android/assets/images.atlas"));
        skin = new Skin();

        skin.add("default", new BitmapFont(Gdx.files.internal("android/assets/fonts/sansman16.fnt")));
        skin.add("default", new Label.LabelStyle(skin.getFont("default"), Color.GOLD));
        skin.add("buttonUpImg", new NinePatch(images.findRegion("grey_button"), 10, 10, 10, 10));
        skin.add("buttonDownImg", new NinePatch(images.findRegion("grey_button_pressed"), 10, 10, 10, 10));
        skin.add("default", new Button.ButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg")));
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg"),
                skin.getFont("default"));
        tbs.pressedOffsetY = -4;
        tbs.fontColor = Color.GOLD;
        skin.add("default", tbs);

        stone = new Image(images.findRegion("stone"));

        mainStage = new Stage(new ScreenViewport()){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                stone.addAction(Actions.sequence(Actions.scaleBy(0.05f, 0.05f, 0.05f), Actions.scaleBy(-0.05f, -0.05f, 0.05f)));
                gameController.addStones(gameController.getClickPower());
                return super.touchDown(screenX, screenY, pointer, button);
            }
        };
        uiStage = new Stage(new ScreenViewport());

        stonesCounterLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText("" + gameController.getStonesCounter());
                super.act(delta);
            }
        };
        stonesCounterLabel.setAlignment(Align.right);

        screenWidth = mainStage.getViewport().getScreenWidth();
        screenHeight = mainStage.getViewport().getScreenHeight();
        stoneMaxWidth = (int) stone.getWidth();
        stoneMaxHeight  = (int) stone.getHeight();

        if (stone.getWidth() > screenWidth)
            stone.setSize(screenWidth, screenWidth); // stone is square
        mainStage.addActor(stone);
        stone.setPosition(screenWidth/2 - stone.getWidth()/2, screenHeight/2 - stone.getHeight()/2);
        stone.setOrigin(stone.getWidth()/2, stone.getHeight()/2);
        stone.debug();

        TextButton upgradesButton = new TextButton("Upgrades", skin);
        upgradesButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return false;
            }
        });

        Table uiTable = new Table(skin);
        uiStage.addActor(uiTable);
        uiTable.setFillParent(true);
        uiTable.top().pad(5);
        uiTable.add(new Label("Stones: ", skin)).left();
        uiTable.add(stonesCounterLabel).left().expandX();
        uiTable.row().expandY();
        uiTable.add(upgradesButton).left().bottom();
        uiTable.debug();

        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, mainStage));
    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        // Update game state
        mainStage.act(delta);
        uiStage.act(delta);

        // Clean screen and draw stages
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        mainStage.draw();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainStage.getViewport().update(width, height, true);
        uiStage.getViewport().update(width, height, true);
        if (width < height)
            stone.setSize(MathUtils.clamp(width, 240, stoneMaxWidth), MathUtils.clamp(width, 240, stoneMaxWidth));
        else
            stone.setSize(MathUtils.clamp(height, 240, stoneMaxHeight), MathUtils.clamp(height, 240, stoneMaxHeight));
        stone.setOrigin(stone.getWidth()/2, stone.getHeight()/2);
        screenWidth = mainStage.getViewport().getScreenWidth();
        screenHeight = mainStage.getViewport().getScreenHeight();
        stone.setPosition((float) screenWidth/2 - stone.getWidth()/2, (float) screenHeight/2 - stone.getHeight()/2);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }
}
