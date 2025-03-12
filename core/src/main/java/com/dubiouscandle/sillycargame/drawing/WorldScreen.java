package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.physics.Car;
import com.dubiouscandle.sillycargame.utils.ImageLoader;

public class WorldScreen implements Screen {
	Car car;

	public static float genInterval = 0.1f;
	static TerrainGenerator gen;

	Main main;
	Stage stage;

	FitViewport viewport = new FitViewport(1.6f * 3f, .9f * 3f);

	private World world;

	public WorldScreen(Main main) {
		this.main = main;
		this.stage = main.stage;
	}

	public void prep(Car car) {
		this.car = car;
	}

	@Override
	public void show() {
		gen = new TerrainGenerator();
		world = new World(new Vector2(0, -4f), true);
		gen.create(world, 2, genInterval);
		car.addToWorld(world);
		car.setPos(1, 0.2f);

		car.getMainBody().setType(BodyType.StaticBody);
		for (int i = 0; i < 100; i++) {
			world.step(0.01f, 10, 10);
		}
		car.getMainBody().setType(BodyType.DynamicBody);

		stage.setViewport(viewport);

		stage.addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Input.Keys.R) {
					main.setScreen(main.menuScreen);
				}
				return true;
			}
		});
	}

	Batch batch = new SpriteBatch();
	BitmapFont font = new BitmapFont();
	Vector3 cameraTarget = new Vector3();
	OrthographicCamera textCamera = new OrthographicCamera();

	@Override
	public void render(float delta) {
		for (int i = 0; i < 10; i++) {
			world.step(delta * 0.1f, 16, 10);
		}

		viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		OrthographicCamera camera = (OrthographicCamera) viewport.getCamera();

		cameraTarget.set(car.getMainBody().getWorldCenter(), 0);
//		cameraTarget.set(50, 0, 0);

		camera.position.lerp(cameraTarget, 20 * delta);
		viewport.getCamera().update();

		Main.shapeRenderer.setColor(0, 0, 0, 1);
		Main.shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);

		gen.draw(Main.shapeRenderer);

		car.drawShape(Main.shapeRenderer);

		stage.draw();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		float scale = 0.0127f;
		float w = 8421 * scale;
		float h = 5100 * scale;
//		batch.draw(ImageLoader.redPointer, 0, -0.5f * h, w, h);
		batch.draw(ImageLoader.textOverlay, -2.00f, -0.490f * h, w, h);

		batch.end();
//		new Box2DDebugRenderer().render(world, viewport.getCamera().combined);
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
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		stage.clear();
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
