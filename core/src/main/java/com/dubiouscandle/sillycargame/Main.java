package com.dubiouscandle.sillycargame;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.EarClippingTriangulator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.dubiouscandle.sillycargame.drawing.DrawScreen;
import com.dubiouscandle.sillycargame.drawing.WheelScreen;
import com.dubiouscandle.sillycargame.drawing.WorldScreen;
import com.dubiouscandle.sillycargame.utils.ImageLoader;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all
 * platforms.
 */
public class Main extends Game {
	public static BitmapFont font;
	
	public static Skin skin;
	public static EarClippingTriangulator triangulator;
	public static ShapeRenderer shapeRenderer;

	public static final float WindowWidth = 640;
	
	public static final float WindowHeight = 480;
	public static final float StrokeWeight = 3f;
	public static final float WorldStrokeWeight = StrokeWeight/WindowHeight;

	public Stage stage;

	public MenuScreen menuScreen;
	public DrawScreen drawScreen;
	public WheelScreen wheelScreen;
	public WorldScreen worldScreen;
	
	@Override
	public void create() {
		System.out.println("\n"
				+ "                                                                  \n"
				+ "                                                                  \n"
				+ "                 .----.     .----..--.   _..._                    \n"
				+ "  .--./)          \\    \\   /    / |__| .'     '.                  \n"
				+ " /.''\\\\            '   '. /'   /  .--..   .-.   .                 \n"
				+ "| |  | |      __   |    |'    /   |  ||  '   '  |                 \n"
				+ " \\`-' /    .:--.'. |    ||    |   |  ||  |   |  |                 \n"
				+ " /(\"'`    / |   \\ |'.   `'   .'   |  ||  |   |  |                 \n"
				+ " \\ '---.  `\" __ | | \\        /    |  ||  |   |  |                 \n"
				+ "  /'\"\"'.\\  .'.''| |  \\      /     |__||  |   |  |                 \n"
				+ " ||     ||/ /   | |_  '----'          |  |   |  |                 \n"
				+ " \\'. __// \\ \\._,\\ '/                  |  |   |  |                 \n"
				+ "  `'---'   `--'  `\"                   '--'   '--'                 \n"
				+ "/|              __.....__        _..._                            \n"
				+ "||          .-''         '.    .'     '.                          \n"
				+ "||         /     .-''\"'-.  `. .   .-.   .                         \n"
				+ "||  __    /     /________\\   \\|  '   '  |                         \n"
				+ "||/'__ '. |                  ||  |   |  |                         \n"
				+ "|:/`  '. '\\    .-------------'|  |   |  |                         \n"
				+ "||     | | \\    '-.____...---.|  |   |  |                         \n"
				+ "||\\    / '  `.             .' |  |   |  |                         \n"
				+ "|/\\'..' /     `''-...... -'   |  |   |  |                         \n"
				+ "'  `'-'`            .-'''-.   |  |   |  |                   ___   \n"
				+ "                   '   _    \\ '--'   '--'                .'/   \\  \n"
				+ " __  __   ___    /   /` '.   \\ .--.   _..._             / /     \\ \n"
				+ "|  |/  `.'   `. .   |     \\  ' |__| .'     '.           | |     | \n"
				+ "|   .-.  .-.   '|   '      |  '.--..   .-.   .          | |     | \n"
				+ "|  |  |  |  |  |\\    \\     / / |  ||  '   '  |          |/`.   .' \n"
				+ "|  |  |  |  |  | `.   ` ..' /  |  ||  |   |  |           `.|   |  \n"
				+ "|  |  |  |  |  |    '-...-'`   |  ||  |   |  |            ||___|  \n"
				+ "|  |  |  |  |  |               |  ||  |   |  |            |/___/  \n"
				+ "|__|  |__|  |__|               |__||  |   |  |            .'.--.  \n"
				+ "                                   |  |   |  |           | |    | \n"
				+ "                                   |  |   |  |           \\_\\    / \n"
				+ "                                   '--'   '--'            `''--'  \n"
				+ "");
		
		ImageLoader.init();

		skin = new Skin(Gdx.files.internal("ui/uiskin.json"));
		font = new BitmapFont();
		shapeRenderer = new ShapeRenderer();
		triangulator = new EarClippingTriangulator();

		stage = new Stage();
		
		menuScreen = new MenuScreen(this);
		drawScreen = new DrawScreen(this);
		wheelScreen = new WheelScreen(this);
		worldScreen = new WorldScreen(this);

		setScreen(menuScreen);
		
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		inputMultiplexer.addProcessor(new InputProcessor() {

			@Override
			public boolean keyDown(int keycode) {
				if(keycode == Input.Keys.R) {
					setScreen(menuScreen);
				}
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean keyTyped(char character) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean scrolled(float amountX, float amountY) {
				// TODO Auto-generated method stub
				return false;
			}});
		Gdx.input.setInputProcessor(inputMultiplexer);
	}

	@Override
	public void dispose() {
		menuScreen.dispose();
		drawScreen.dispose();
		stage.dispose();
	}

	public void drawBackground(float r, float g, float b) {
		shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
		shapeRenderer.setColor(r, g, b, 1);
		shapeRenderer.begin(ShapeType.Filled);

		shapeRenderer.rect(0, 0, WindowWidth, WindowHeight);

		shapeRenderer.end();
	}
}
