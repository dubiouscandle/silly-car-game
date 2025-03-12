package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.physics.Car;
import com.dubiouscandle.sillycargame.physics.Wheel;
import com.dubiouscandle.sillycargame.physics.WheelPlacement;
import com.dubiouscandle.sillycargame.physics.WheelType;

public class WheelScreen implements Screen {
	Stage stage;
	Main main;

	TextButton resetButton = new TextButton("reset", Main.skin);
	TextButton continueButton = new TextButton("spawn it in!", Main.skin);
	Viewport viewport = new FitViewport(Main.WindowWidth, Main.WindowHeight);
	WheelPanel wheelPanel = new WheelPanel();

	float dragRadius;
	final float wheelPanelSize = Main.WindowHeight * 0.8f;

	WheelType draggingType = null;
	boolean dragging = false;
	Vector2 draggingPos = new Vector2();

	WheelButton[] wheelButtons = new WheelButton[] { new WheelButton(WheelType.WHEELY, wheelPanelSize),
			new WheelButton(WheelType.FLOWER, wheelPanelSize), new WheelButton(WheelType.RECURSIVE, wheelPanelSize),
			new WheelButton(WheelType.DOUBLE, wheelPanelSize), new WheelButton(WheelType.DUBIOUS, wheelPanelSize),
			new WheelButton(WheelType.EGGY, wheelPanelSize), };

	public WheelScreen(Main main) {
		resetButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				wheelPanel.reset();
				return true;
			}
		});
		continueButton.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				Car car = new Car(wheelPanel.shape);
				
				for(WheelPlacement placement : wheelPanel.placements) {
					car.addWheel(placement);
				}
				
				main.worldScreen.prep(car);
				main.setScreen(main.worldScreen);
				return true;
			}
		});

		this.main = main;
		this.stage = main.stage;

		float size = Main.WindowHeight * 0.8f;
		float marginX = 0.5f * (Main.WindowWidth - size);
		float marginY = 0.5f * (Main.WindowHeight - size);
		wheelPanel.setBounds(marginX, marginY, size);

		positionButtons();

		for (WheelButton wheelButton : wheelButtons) {
			wheelButton.setTouchable(Touchable.enabled);
			wheelButton.addListener(new InputListener() {
				@Override
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					dragging = true;
					dragRadius = wheelButton.radius;
					draggingType = wheelButton.wheelType;
					draggingPos.set(x, y);
					wheelButton.localToStageCoordinates(draggingPos);
					wheelPanel.updateDrag(draggingPos, draggingType, wheelButton.radius);
					return true;
				}

				@Override
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					wheelPanel.releaseDrag(draggingPos, draggingType, dragRadius);
					dragging = false;
				}

				@Override
				public void touchDragged(InputEvent event, float x, float y, int pointer) {
					draggingPos.set(x, y);
					wheelButton.localToStageCoordinates(draggingPos);
					wheelPanel.updateDrag(draggingPos, draggingType, wheelButton.radius);
				}

				@Override
				public boolean mouseMoved(InputEvent event, float x, float y) {
					stage.setScrollFocus(wheelButton);
					return true;
				}

				@Override
				public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
					wheelButton.radius += wheelButton.scrollSensitivity * amountY;
					wheelButton.radius = MathUtils.clamp(wheelButton.radius, Wheel.MinRadius, Wheel.MaxRadius);
					return true;
				}
			});
		}
		
	}

	private void positionButtons() {
		final float size = 0.4f * (Main.WindowWidth - wheelPanelSize);
		final float marginX = 0.5f * (0.5f * (Main.WindowWidth - wheelPanelSize) - size);
		final float marginY = Main.WindowHeight * 0.1f;
		final float vGap = size + 10;

		wheelButtons[0].setBounds(marginX, Main.WindowHeight - size - marginY, size, size);
		wheelButtons[1].setBounds(marginX, Main.WindowHeight - size - marginY - vGap, size, size);
		wheelButtons[2].setBounds(marginX, Main.WindowHeight - size - marginY - vGap * 2, size, size);

		wheelButtons[3].setBounds(Main.WindowWidth - size - marginX, Main.WindowHeight - size - marginY, size, size);
		wheelButtons[4].setBounds(Main.WindowWidth - size - marginX, Main.WindowHeight - size - marginY - vGap, size,
				size);
		wheelButtons[5].setBounds(Main.WindowWidth - size - marginX, Main.WindowHeight - size - marginY - vGap * 2,
				size, size);

		resetButton.setBounds(marginX, Main.WindowHeight * 0.1f, size,
				Main.WindowHeight - marginY - vGap * 3 - Main.WindowHeight * 0.1f);

		continueButton.setBounds(Main.WindowWidth - marginX - size, Main.WindowHeight * 0.1f, size,
				Main.WindowHeight - marginY - vGap * 3 - Main.WindowHeight * 0.1f);
	}

	@Override
	public void show() {
		wheelPanel.reset();
		dragging = false;
		
		stage.setViewport(viewport);

		for (WheelButton wheelButton : wheelButtons) {
			stage.addActor(wheelButton);
		}

		stage.addActor(continueButton);
		stage.addActor(resetButton);
		stage.addActor(wheelPanel);

	}

	public void prep(FloatArray shape) {
		wheelPanel.shape = shape;
	}

	@Override
	public void render(float delta) {
		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);
		main.drawBackground(1, 1, 1);

		stage.act(delta);
		stage.draw();

		Main.shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
		for (WheelButton wheelButton : wheelButtons) {
			wheelButton.drawShape(Main.shapeRenderer);
		}

		wheelPanel.drawShape(Main.shapeRenderer);

		if (dragging) {
			Main.shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
			Main.shapeRenderer.begin(ShapeType.Filled);
			Main.shapeRenderer.setColor(1, 0, 0, 1);
			Main.shapeRenderer.circle(draggingPos.x, draggingPos.y, Main.StrokeWeight);
			Main.shapeRenderer.end();
		}
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

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
