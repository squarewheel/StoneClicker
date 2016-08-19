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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    private Table scoresTable;
    private Button menuButton;
    private Button upgradesButton;
    private Window upgradesWindow;
    private int screenWidth;
    private int screenHeight;
    private final int stoneMaxWidth;
    private final int stoneMaxHeight;

    // Temp variables for debug purposes
    int ccc = 0;

    public GameScreen(final StoneClicker gameController) {
        this.gameController = gameController;
        images = new TextureAtlas(Gdx.files.internal("android/assets/images.atlas"));
        skin = new Skin();

        skin.add("default", new BitmapFont(Gdx.files.internal("android/assets/fonts/sansman24.fnt")));
        skin.add("sansman16", new BitmapFont(Gdx.files.internal("android/assets/fonts/sansman16.fnt")));
        skin.add("default", new Label.LabelStyle(skin.getFont("default"), Color.GOLD));
        skin.add("buttonUpImg", new NinePatch(images.findRegion("grey_button"), 10, 10, 10, 10));
        skin.add("buttonDownImg", new NinePatch(images.findRegion("grey_button_pressed"), 10, 10, 10, 10));
        skin.add("windowImg", new NinePatch(images.findRegion("grey_box"), 8, 8, 8, 8));
        skin.add("sliderImg", new NinePatch(images.findRegion("grey_slidebar"), 5, 5, 5, 5));
        skin.add("knobImg", new TextureRegion(images.findRegion("gray_slider")));
        skin.add("closeWindowImg", new TextureRegion(images.findRegion("red_cross_small")));
        skin.add("menuButtonGreen", new TextureRegion(images.findRegion("green_menu_button")));
        skin.add("menuButtonGray", new TextureRegion(images.findRegion("gray_menu_button")));
        skin.add("plusButtonGreen", new TextureRegion(images.findRegion("green_plus_button")));
        skin.add("plusButtonGray", new TextureRegion(images.findRegion("gray_plus_button")));
        skin.add("default", new Window.WindowStyle(skin.getFont("sansman16"), Color.GOLD, skin.getDrawable("windowImg")));
        skin.add("default", new Button.ButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg")));
        skin.add("closeWindowButton", new Button.ButtonStyle(
                skin.getDrawable("closeWindowImg"),
                skin.getDrawable("closeWindowImg"),
                skin.getDrawable("closeWindowImg")));
        skin.add("menuButtonStyle", new Button.ButtonStyle(
                skin.getDrawable("menuButtonGray"),
                skin.getDrawable("menuButtonGreen"),
                skin.getDrawable("menuButtonGray")));
        skin.add("plusButtonStyle", new Button.ButtonStyle(
                skin.getDrawable("plusButtonGray"),
                skin.getDrawable("plusButtonGreen"),
                skin.getDrawable("plusButtonGray")));
        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle(
                skin.getDrawable("buttonUpImg"),
                skin.getDrawable("buttonDownImg"),
                skin.getDrawable("buttonUpImg"),
                skin.getFont("default"));
        tbs.pressedOffsetY = -4;
        tbs.fontColor = Color.GOLD;
        ScrollPane.ScrollPaneStyle sps = new ScrollPane.ScrollPaneStyle();
        sps.vScroll = skin.getDrawable("sliderImg");
        sps.vScrollKnob = skin.getDrawable("knobImg");
        skin.add("default", sps);
        skin.add("default", tbs);

        stone = new Image(images.findRegion("stone"));

        mainStage = new Stage(new ScreenViewport()){
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!stone.hasActions())    // Click animation
                    stone.addAction(Actions.parallel(
                        Actions.sequence(
                                Actions.scaleBy(-0.05f, -0.05f, 0.05f),
                                Actions.scaleBy(0.05f, 0.05f, 0.05f)),
                        Actions.sequence(
                                Actions.rotateBy(0.5f, 0.02f),
                                Actions.delay(0.01f),
                                Actions.rotateBy(-1f, 0.04f),
                                Actions.delay(0.01f),
                                Actions.rotateBy(0.5f, 0.02f))
                    ));

                // Each click adds stones
                gameController.getStonesCounter().addStones(gameController.getClickPower());
                return super.touchDown(screenX, screenY, pointer, button);
            }
        };
        uiStage = new Stage(new ScreenViewport());

        stonesCounterLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText("" + gameController.getStonesCounter().getStones());
                super.act(delta);
            }
        };

        stonesPerSecondLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText("" + gameController.getStonesPerSecond());
                super.act(delta);
            }
        };

        clickPowerLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText("" + gameController.getClickPower());
                super.act(delta);
            }
        };

        screenWidth = mainStage.getViewport().getScreenWidth();
        screenHeight = mainStage.getViewport().getScreenHeight();
        stoneMaxWidth = (int) stone.getWidth();
        stoneMaxHeight  = (int) stone.getHeight();

        if (stone.getWidth() > screenWidth)
            stone.setSize(screenWidth, screenWidth); // stone is square
        mainStage.addActor(stone);
        stone.setPosition(screenWidth/2 - stone.getWidth()/2, screenHeight/2 - stone.getHeight()/2);
        stone.setOrigin(stone.getWidth()/2, stone.getHeight()/2);
        //stone.debug();

        Button closeWindowButton = new Button(skin, "closeWindowButton");
        menuButton = new Button(skin, "menuButtonStyle");
        upgradesButton = new Button(skin, "plusButtonStyle");

        Table upgradesTable = new Table(skin);
        //upgradesTable.setFillParent(true);
        upgradesTable.top().padRight(40);
        upgradesTable.add(new TextButton("upgrade 1", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 3", skin)).left().expandX().fillX(); upgradesTable.row();
        upgradesTable.add(new TextButton("upgrade 2", skin)).left().expandX().fillX();
        upgradesTable.debug();

        ScrollPane upgradesScrollPane = new ScrollPane(upgradesTable, skin);
        upgradesScrollPane.setVariableSizeKnobs(false);
        upgradesScrollPane.setFadeScrollBars(false);
        //upgradesScrollPane.setClamp(true);
        //upgradesScrollPane.setFillParent(true);

        upgradesButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upgradesWindow.setVisible(true);
                upgradesWindow.toFront();
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        scoresTable = new Table(skin);
        scoresTable.setFillParent(true);
        scoresTable.top().left().pad(5, 5, 5, 0);
        scoresTable.add(new Label("Stones: ", skin)).right();
        scoresTable.add(stonesCounterLabel).left();
        scoresTable.row();
        scoresTable.add(new Label("Power: ", skin)).right();
        scoresTable.add(clickPowerLabel).left();
        scoresTable.row();
        scoresTable.add(new Label("SPS: ", skin)).right();
        scoresTable.add(stonesPerSecondLabel).left();
        uiStage.addActor(scoresTable);
        scoresTable.debug();

        Table buttonsTable = new Table(skin);
        buttonsTable.setFillParent(true);
        buttonsTable.top().right().pad(5, 0, 5, 5);
        buttonsTable.add(menuButton).top().right();
        buttonsTable.row();
        buttonsTable.add(upgradesButton);
        uiStage.addActor(buttonsTable);
        //buttonsTable.debug();

        upgradesWindow = new Window("Upgrades", skin);
        upgradesWindow.setVisible(false);
        //upgradesWindow.debug();
        upgradesWindow.setX(5);
        //upgradesWindow.setFillParent(true);
        upgradesWindow.padLeft(5).padRight(5).padBottom(5);
        //upgradesWindow.bottom();
        upgradesWindow.padTop(20);
        upgradesWindow.getTitleTable().add(closeWindowButton);
        upgradesWindow.add(upgradesScrollPane).left().padTop(5).expandX().fillX();
        uiStage.addActor(upgradesWindow);

        closeWindowButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("click");
                upgradesWindow.setVisible(false);
                super.touchUp(event, x, y, pointer, button);
            }
        });

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
        upgradesWindow.setSize(screenWidth, screenHeight - (menuButton.getHeight() + upgradesButton.getHeight() + 20));
        upgradesWindow.setX(5);
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
