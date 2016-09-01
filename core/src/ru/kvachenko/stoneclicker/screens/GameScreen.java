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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import ru.kvachenko.stoneclicker.StoneClicker;
import ru.kvachenko.stoneclicker.StonesCounter;
import ru.kvachenko.stoneclicker.Upgrade;

import java.math.BigDecimal;

/**
 * @author Sasha Kvachenko
 *         Created on 15.08.2016.
 *         <p>
 *         Main game screen.
 */
public class GameScreen implements Screen {
    private class UpgradeWidget extends Table {
        private Upgrade upgrade;
        private Button buyButton;
        private Button sellButton;

        UpgradeWidget(Upgrade u) {
            super();
            upgrade = u;

            Label nameLabel = new Label(upgrade.getName(), skin);
            Label amountLabel = new Label("x0", skin) {
                @Override
                public void act(float delta) {
                    setText("x" + upgrade.getAmount());
                    super.act(delta);
                }
            };
            Label costLabel = new Label("", skin) {
                @Override
                public void act(float delta) {
                    setText("Cost:\n" + StonesCounter.shortedValueOf(upgrade.getCost()));
                    super.act(delta);
                }
            };
            Label bonusesLabel = new Label("", skin, "upgradeLabel") {
                @Override
                public void act(float delta) {
                    setText("Power: " + StonesCounter.shortedValueOf(upgrade.getPowerBonus()) +
                     " SPS: " + StonesCounter.shortedValueOf(upgrade.getSPSBonus()));
                    super.act(delta);
                }
            };

            buyButton = new Button(skin, "plusButtonStyle");
            sellButton = new Button(skin, "minusButtonStyle");
            buyButton.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    upgrade.buy(gameController);
                }
            });
            sellButton.addListener(new InputListener(){
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    return true;
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    upgrade.sell(gameController);
                }
            });

            HorizontalGroup buttonsGroup = new HorizontalGroup();
            buttonsGroup.addActor(costLabel);
            buttonsGroup.addActor(buyButton);
            buttonsGroup.addActor(sellButton);

            this.background(skin.getDrawable("windowImg"));
            this.pad(1, 4, 1, 1);
            this.add(nameLabel).top().left().expandX();
            this.add(amountLabel).top().right().padRight(2);
            this.row().expandY();
            this.add(bonusesLabel).top().left();
            this.row();
            this.add(buttonsGroup).colspan(2).expandX().bottom().right().pad(0, 0, 2, 2);
            //this.debug();

        }

        @Override
        public void act(float delta) {
            if (gameController.getScore().getCounter().compareTo(upgrade.getCost()) < 0) {
                if (upgrade.getAmount() < 1)
                    this.addAction(Actions.alpha(0.2f));
                else {
                    this.addAction(Actions.alpha(0.8f));
                    buyButton.addAction(Actions.alpha(0.2f));
                    sellButton.addAction(Actions.alpha(1f));
                }
            }
            else {
                this.addAction(Actions.alpha(1f));
                buyButton.addAction(Actions.alpha(1f));
                this.addAction(Actions.alpha(1f));
            }
            super.act(delta);
        }
    }

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
    private int stoneMaxWidth;
    private int stoneMaxHeight;

    // Temp variables for debug purposes
    int ccc = 0;

    public GameScreen(final StoneClicker gameController) {
        this.gameController = gameController;
        images = new TextureAtlas(Gdx.files.internal("android/assets/images.atlas"));

        // Skin initialization
        skin = new Skin();
        skin.add("default", new BitmapFont(Gdx.files.internal("android/assets/fonts/sansman24.fnt")));
        skin.add("sansman16", new BitmapFont(Gdx.files.internal("android/assets/fonts/sansman16.fnt")));
        skin.add("default", new Label.LabelStyle(skin.getFont("default"), Color.GOLD));
        skin.add("upgradeLabel", new Label.LabelStyle(skin.getFont("sansman16"), Color.GOLD));
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
        skin.add("minusButtonGreen", new TextureRegion(images.findRegion("green_minus_button")));
        skin.add("minusButtonGray", new TextureRegion(images.findRegion("gray_minus_button")));
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
        skin.add("minusButtonStyle", new Button.ButtonStyle(
                skin.getDrawable("minusButtonGray"),
                skin.getDrawable("minusButtonGreen"),
                skin.getDrawable("minusButtonGray")));
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

        // Stages initialization
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
                gameController.getScore().addStones(gameController.getClickPower().getCounter());
                return super.touchDown(screenX, screenY, pointer, button);
            }
        };
        uiStage = new Stage(new ScreenViewport());

        // Stone initialization
        stone = new Image(images.findRegion("stone"));
        mainStage.addActor(stone);

        // Screen variables initialization
        screenWidth = mainStage.getViewport().getScreenWidth();
        screenHeight = mainStage.getViewport().getScreenHeight();
        stoneMaxWidth = (int) stone.getWidth();
        stoneMaxHeight  = (int) stone.getHeight();

        // Scores table initialization
        stonesCounterLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText(gameController.getScore().getStones());
                super.act(delta);
            }
        };
        stonesPerSecondLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText(gameController.getStonesPerSecond().getStones());
                super.act(delta);
            }
        };
        clickPowerLabel = new Label("--", skin) {
            @Override
            public void act(float delta) {
                setText(gameController.getClickPower().getStones());
                super.act(delta);
            }
        };
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
        //scoresTable.debug();

        // Upgrades window initialization
        Table upgradesTable = new Table(skin);
        upgradesTable.top();
        for (Upgrade u: gameController.getUpgradesList()) {
            UpgradeWidget upgradeWidget = new UpgradeWidget(u);
            upgradesTable.add(upgradeWidget).left().expandX().fillX(); upgradesTable.row();
        }
        ScrollPane upgradesScrollPane = new ScrollPane(upgradesTable, skin);
        upgradesScrollPane.setVariableSizeKnobs(false);
        upgradesScrollPane.setFadeScrollBars(false);
        upgradesWindow = new Window("Upgrades", skin);
        Button closeWindowButton = new Button(skin, "closeWindowButton");
        closeWindowButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                upgradesWindow.setVisible(false);
                super.touchUp(event, x, y, pointer, button);
            }
        });
        upgradesWindow.setVisible(false);
        upgradesWindow.setX(5);
        upgradesWindow.padLeft(5).padRight(5).padBottom(5);
        upgradesWindow.padTop(20);
        upgradesWindow.getTitleTable().add(closeWindowButton);
        upgradesWindow.add(upgradesScrollPane).left().padTop(5).expandX().fillX();
        uiStage.addActor(upgradesWindow);
        //upgradesTable.debug();
        //upgradesWindow.debug();

        // Menu window initialization
        TextButton continueGameButton = new TextButton("CONTINUE GAME", skin);
        TextButton exitGameButton = new TextButton("EXIT", skin);
        final Dialog menuWindow = new Dialog("GAME SAVED", skin);
        menuWindow.padTop(20);
        menuWindow.getButtonTable().add(continueGameButton).fillX();
        menuWindow.getButtonTable().row();
        menuWindow.getButtonTable().add(exitGameButton).fillX();
        continueGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                menuWindow.hide();
            }
        });
        exitGameButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        // Onscreen buttons initialization
        menuButton = new Button(skin, "menuButtonStyle");
        menuButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                gameController.saveGame();
                menuWindow.show(uiStage);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        upgradesButton = new Button(skin, "plusButtonStyle");
        upgradesButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                upgradesWindow.setVisible(true);
                upgradesWindow.toFront();
                return super.touchDown(event, x, y, pointer, button);
            }
        });
        Table buttonsTable = new Table(skin);
        buttonsTable.setFillParent(true);
        buttonsTable.top().right().pad(5, 0, 5, 5);
        buttonsTable.add(menuButton).top().right();
        buttonsTable.row();
        buttonsTable.add(upgradesButton);
        uiStage.addActor(buttonsTable);
        //buttonsTable.debug();

        // Add stages to global events listener
        Gdx.input.setInputProcessor(new InputMultiplexer(uiStage, mainStage));
    }

    @Override
    public void dispose() {
        // TODO: screen dispose
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
