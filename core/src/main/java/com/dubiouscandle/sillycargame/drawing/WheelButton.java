package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.physics.Wheel;
import com.dubiouscandle.sillycargame.physics.WheelType;

public class WheelButton extends Actor {
	final GlyphLayout layout = new GlyphLayout();
	final float drawScaleFactor;

	final float scrollSensitivity = 0.03f;

	float radius = Wheel.MaxRadius / 2;
	final WheelType wheelType;

	TextButton plus = new TextButton("+", Main.skin);
	TextButton minus = new TextButton("-", Main.skin);

	public WheelButton(Stage stage, WheelType wheelType, float drawScaleFactor) {
		this.wheelType = wheelType;
		this.drawScaleFactor = drawScaleFactor;

		final float interval = 0.01f;
		plus.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				radius += interval;
				if (radius > Wheel.MaxRadius) {
					radius = Wheel.MaxRadius;
				}
				return true;
			}
		});

		minus.addListener(new InputListener() {
			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				radius -= interval;
				if (radius < Wheel.MinRadius) {
					radius = Wheel.MinRadius;
				}

				return true;
			}
		});
	}

	@Override
	public void act(float delta) {
		float w = getWidth() / 5;
		float y = getY() + getHeight() / 2 - w / 2;
		minus.setBounds(getX(), y, w, w);
		plus.setBounds(getX() + getWidth() - w, y, w, w);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		Main.font.getData().setScale(1);
		layout.setText(Main.font, wheelType.name);
		float w = layout.width;
		float margin = getWidth() * 0.05f;
		Main.font.setColor(0, 0, 0, 1);
		Main.font.draw(batch, wheelType.name, getX() + getWidth() / 2 - w / 2, getY() + getHeight() * 0.9f);

		Main.font.getData().setScale(.7f);
		layout.setText(Main.font, wheelType.name);
		Main.font.setColor(0.8f, 0.8f, 0.8f, 1);
		Main.font.draw(batch, wheelType.desc, getX() + margin, getY() + getHeight() * 0.3f, getWidth() - margin * 2,
				Align.center, true);
	}

	public void drawShape(ShapeRenderer shapeRenderer) {
		float w = getWidth();
		float h = getHeight();

		wheelType.drawIcon(shapeRenderer, getX() + w / 2, getY() + h / 2, radius * drawScaleFactor);

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
		shapeRenderer.end();
	}
}
