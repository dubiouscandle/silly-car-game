package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dubiouscandle.sillycargame.Main;

public class DrawScreen implements Screen {
	public static FloatArray shape;
	
	Stage stage;
	Main main;
	Viewport viewport = new FitViewport(Main.WindowWidth, Main.WindowHeight);

	TextButton continueButton = new TextButton("continue", Main.skin);
	TextButton resetButton = new TextButton("reset", Main.skin);

	DrawPanel drawPanel = new DrawPanel();

	private void positionButtons() {
		final float size = 0.4f * (Main.WindowWidth - Main.WindowHeight * 0.8f);
		final float marginX = 0.5f * (0.5f * (Main.WindowWidth - Main.WindowHeight * 0.8f) - size);
		final float marginY = Main.WindowHeight * 0.1f;
		final float vGap = size + 10;

		resetButton.setBounds(marginX, Main.WindowHeight * 0.1f, size,
				Main.WindowHeight - marginY - vGap * 3 - Main.WindowHeight * 0.1f);
		continueButton.setBounds(Main.WindowWidth - marginX - size, Main.WindowHeight * 0.1f, size,
				Main.WindowHeight - marginY - vGap * 3 - Main.WindowHeight * 0.1f);
	}

	public DrawScreen(Main main) {
		viewport.getCamera().translate(Main.WindowWidth * 0.5f, Main.WindowHeight * 0.5f, 0);

		float size = Main.WindowHeight * 0.8f;
		float marginX = 0.5f * (Main.WindowWidth - size);
		float marginY = 0.5f * (Main.WindowHeight - size);
		drawPanel.setBounds(marginX, marginY, size);

		positionButtons();

		resetButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				drawPanel.reset();
				return true;
			}
		});

		continueButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if (drawPanel.canContinue()) {
					shape = drawPanel.shape;
					main.wheelScreen.prep(drawPanel.shape);
					main.setScreen(main.wheelScreen);
				}
				return true;
			}
		});

		this.main = main;
		this.stage = main.stage;
	}

	@Override
	public void show() {
		drawPanel.reset();
		stage.setViewport(viewport);
		stage.addActor(drawPanel);
		stage.addActor(continueButton);
		stage.addActor(resetButton);
	}

	@Override
	public void render(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		main.drawBackground(1, 1, 1);

		stage.act(delta);
		stage.draw();

		drawPanel.drawShape(Main.shapeRenderer);
	}

	@Override
	public void resize(int width, int height) {
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
