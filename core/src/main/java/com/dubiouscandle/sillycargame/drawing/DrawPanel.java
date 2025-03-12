package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.FloatArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class DrawPanel extends Canvas {
	final float brushSpeed = 2f;
	final float scaleRadius = 0.2f;
	final float thickness = Main.StrokeWeight;

	FloatArray shape = new FloatArray();

	float brushX, brushY;
	float minSegmentLength = 0.01f;

	boolean pMousePressed = false;

	boolean finished = false;
	boolean drawing = false;

	DrawPanel() {
	}

	@Override
	public void act(float delta) {
		if (finished) {
			return;
		}

		if (!mousePressed()) {
			return;
		}

		if (!drawing) {
			drawing = true;
			brushX = getMouseX();
			brushY = getMouseY();
			return;
		}

		float dx = getMouseX() - brushX;
		float dy = getMouseY() - brushY;

		float mag = (float) Math.sqrt(dx * dx + dy * dy);

		if (mag == 0) {
			return;
		}

		float invMag = 1.0f / mag;

		dx *= invMag;
		dy *= invMag;

		float scalar = delta * brushSpeed * MathUtils.clamp(mag / scaleRadius, .01f, 1);

		brushX += dx * scalar;
		brushY += dy * scalar;

		brushX = MathUtils.clamp(brushX, 0, 1);
		brushY = MathUtils.clamp(brushY, 0, 1);

		Vector2 selfIntersect = new Vector2();
		int index = shouldEndDrawing(brushX, brushY, selfIntersect);

		if (index != -1) {
			FloatArray newShape = new FloatArray();
			newShape.add(selfIntersect.x, selfIntersect.y);

			for (int i = index + 2; i < shape.size; i++) {
				newShape.add(shape.get(i));
			}

			shape = newShape;
			drawing = false;
			finished = true;
		} else

		if (shape.size == 0 || Geometry.dist2(shape.items[shape.size - 2], shape.items[shape.size - 1], brushX,
				brushY) >= minSegmentLength * minSegmentLength) {
			shape.add(brushX, brushY);
		}

		pMousePressed = mousePressed();
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		Main.font.setColor(0, 0, 0, 1);
		Main.font.getData().setScale(1);
		Main.font.draw(batch, "draw a cool shape. this will be the body of your car.", 0, Main.WindowHeight * 0.95f,
				Main.WindowWidth, Align.center, true);
	}

	private int shouldEndDrawing(float x, float y, Vector2 out) {
		if (shape.size == 0)
			return -1;

		float lastX = shape.items[shape.size - 2];
		float lastY = shape.items[shape.size - 1];

		for (int i = 0; i < shape.size - 8; i += 2) {
			float x1 = shape.items[(i)];
			float y1 = shape.items[(i + 1) % shape.size];
			float x2 = shape.items[(i + 2) % shape.size];
			float y2 = shape.items[(i + 3) % shape.size];

			if (Geometry.edgeIntersects(x1, y1, x2, y2, x, y, lastX, lastY, out)) {
				return i;
			}
		}

		return -1;
	}

	@Override
	public void drawShape(ShapeRenderer shapeRenderer) {
		super.drawShape(shapeRenderer);
		float scaleFactor = scaleFactor();

		shapeRenderer.begin(ShapeType.Filled);

		if (finished) {
			shapeRenderer.setColor(0, 1, 0, 1);
			fillPoly(shape, shapeRenderer);
			shapeRenderer.setColor(0, 0, 0, 1);
			drawPoly(shape, shapeRenderer, Main.StrokeWeight);
		} else {
			shapeRenderer.setColor(0, 0, 0, 1);
			drawChain(shape, shapeRenderer, Main.StrokeWeight);
		}

		if (drawing) {
			shapeRenderer.setColor(0, 0, 0, 1);
			if (shape.size != 0) {
				shapeRenderer.rectLine(shape.items[shape.size - 2] * scaleFactor,
						shape.items[shape.size - 1] * scaleFactor, brushX * scaleFactor, brushY * scaleFactor,
						Main.StrokeWeight);
			}
			float r = Main.StrokeWeight;
			shapeRenderer.setColor(1, 0, 0, 1);
			shapeRenderer.circle(brushX * scaleFactor, brushY * scaleFactor, r);

		}

		shapeRenderer.end();
	}

	public void reset() {
		finished = false;
		drawing = false;
		shape.clear();
	}

	boolean canContinue() {
		return shape.size >= 6;
	}
}
