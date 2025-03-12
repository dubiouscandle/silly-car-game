package com.dubiouscandle.sillycargame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dubiouscandle.sillycargame.drawing.WorldScreen;
import com.dubiouscandle.sillycargame.physics.Wheel;

public class MenuScreen implements Screen {
	Main main;
	Stage stage;
	Label sillyCarGame = new Label("silly car game", Main.skin);
	Label infoLol = new Label("i know that the font is very pixely but im too lazy to fix it", Main.skin);
	Label authors = new Label("by gavin ben moin", Main.skin);
	Label infoR = new Label("press r at any time to return to this screen", Main.skin);
	Label infoCrash = new Label(
			"also this game randomly crashes sometimes, i have no idea why. if that happens just restart im sorry",
			Main.skin);
	Viewport viewport = new FitViewport(Main.WindowWidth, Main.WindowHeight);
	CheckBox funMode = new CheckBox("", Main.skin);

	Label playActualGame = new Label("(pls play the actual game first)", Main.skin);
	TextButton playButton = new TextButton("play", Main.skin);

	public MenuScreen(Main main) {
		funMode.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				if (funMode.isChecked()) {
					WorldScreen.genInterval = 0.1f;
					Wheel.Torque = 0.3f;
					Wheel.MotorSpeed = 10f * MathUtils.PI2;
					Wheel.MaxRadius = 2f;
					funMode.setText("playground mode on");
				} else {
					WorldScreen.genInterval = 0.1f;
					Wheel.Torque = 0.3f;
					Wheel.MotorSpeed = 2f * MathUtils.PI2;
					Wheel.MaxRadius = 0.12f;
					funMode.setText("playground mode off");
				}
			}

		});
		funMode.setChecked(true);
		funMode.setChecked(false);

		viewport.getCamera().translate(Main.WindowWidth * 0.5f, Main.WindowHeight * 0.5f, 0);

		this.main = main;
		this.stage = main.stage;

		playButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				main.setScreen(main.drawScreen);
				return true;
			}
		});

		sillyCarGame.setColor(0, 0, 0, 1);
		sillyCarGame.setFontScale(4);
		sillyCarGame.setAlignment(Align.center);
		sillyCarGame.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.7f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.2f);

		playButton.setBounds(Main.WindowWidth * 0.3f, Main.WindowHeight * 0.5f, Main.WindowWidth * 0.4f,
				Main.WindowHeight * 0.1f);

		CheckBoxStyle style = new CheckBoxStyle();
		style.checkedFocused = funMode.getStyle().checkedFocused;
		style.checkboxOff = funMode.getStyle().checkboxOff;
		style.checkboxOn = funMode.getStyle().checkboxOn;
		style.checkboxOver = funMode.getStyle().checkboxOver;
		style.checkboxOnOver = funMode.getStyle().checkboxOnOver;
		style.font = funMode.getStyle().font;
		style.fontColor = new Color(0, 0, 0, 1);
		funMode.align(Align.center);
		funMode.setStyle(style);
		funMode.setBounds(Main.WindowWidth * 0.3f, Main.WindowHeight * 0.4f, Main.WindowWidth * 0.4f,
				Main.WindowHeight * 0.1f);

		infoLol.setColor(0, 0, 0, 1);
		infoLol.setAlignment(Align.center);
		infoLol.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.2f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.1f);

		infoR.setColor(0, 0, 0, 1);
		infoR.setAlignment(Align.center);
		infoR.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.125f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.1f);

		infoCrash.setColor(0, 0, 0, 1);
		infoCrash.setAlignment(Align.center);
		infoCrash.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.05f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.1f);

		playActualGame.setColor(0, 0, 0, 1);
		playActualGame.setAlignment(Align.center);
		playActualGame.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.35f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.1f);

		authors.setColor(0, 0, 0, 1);
		authors.setAlignment(Align.center);
		authors.setBounds(Main.WindowWidth * 0.1f, Main.WindowHeight * 0.65f, Main.WindowWidth * 0.8f,
				Main.WindowHeight * 0.1f);
	}

	@Override
	public void show() {
		stage.setViewport(viewport);
		stage.addActor(sillyCarGame);
		stage.addActor(playButton);
		stage.addActor(infoLol);
		stage.addActor(infoR);
		stage.addActor(funMode);
		stage.addActor(infoCrash);
		stage.addActor(playActualGame);
		stage.addActor(authors);
	}

	@Override
	public void render(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(1, 1, 1, 1);

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
		stage.clear();
	}

	@Override
	public void dispose() {
	}

}
