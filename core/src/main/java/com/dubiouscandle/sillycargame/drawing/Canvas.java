package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.MouseListener;

public class Canvas extends Actor {
	private MouseListener mouseListener = new MouseListener() {
		@Override
		public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			boolean returnVal = super.touchDown(event, x, y, pointer, button);
			Canvas.this.touchDown(getMouseX(), getMouseY());
			return returnVal;
		}

		@Override
		public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			super.touchUp(event, x, y, pointer, button);

			Canvas.this.touchUp(getMouseX(), getMouseY());
		}

	};

	Canvas() {
		addListener(mouseListener);
	}

	public void setSize(float size) {
		super.setSize(size, size);
	}

	private Matrix4 mat4;

	@Override
	public void draw(Batch batch, float parentAlpha) {
		mat4 = batch.getProjectionMatrix();
	}

	public void drawShape(ShapeRenderer shapeRenderer) {
		shapeRenderer.setProjectionMatrix(mat4.cpy());
		shapeRenderer.getProjectionMatrix().translate(getX(), getY(), 0);

		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(0, 0, 0, 1);
		shapeRenderer.rect(0, 0, getWidth(), getHeight());

		shapeRenderer.end();
	}

	protected void drawChain(FloatArray vertices, ShapeRenderer shapeRenderer, float thickness) {
		for (int i = 0; i < vertices.size - 2; i += 2) {
			float x1 = vertices.items[(i)] * scaleFactor();
			float y1 = vertices.items[(i + 1)] * scaleFactor();
			float x2 = vertices.items[(i + 2)] * scaleFactor();
			float y2 = vertices.items[(i + 3)] * scaleFactor();

			shapeRenderer.rectLine(x1, y1, x2, y2, thickness);
			shapeRenderer.circle(x1, y1, thickness * 0.5f);
		}
	}
	protected void drawPoly(FloatArray vertices, ShapeRenderer shapeRenderer, float thickness) {
		for (int i = 0; i < vertices.size; i += 2) {
			float x1 = vertices.items[(i) % vertices.size] * scaleFactor();
			float y1 = vertices.items[(i + 1) % vertices.size] * scaleFactor();
			float x2 = vertices.items[(i + 2) % vertices.size] * scaleFactor();
			float y2 = vertices.items[(i + 3) % vertices.size] * scaleFactor();

			shapeRenderer.rectLine(x1, y1, x2, y2, thickness);
			shapeRenderer.circle(x1, y1, thickness * 0.5f);
		}
	}

	protected void fillPoly(FloatArray vertices, ShapeRenderer shapeRenderer) {
		ShortArray indices = Main.triangulator.computeTriangles(vertices.items, 0, vertices.size);

		for (int i = 0; i < indices.size; i += 3) {
			int index1 = 2 * indices.get(i);
			int index2 = 2 * indices.get(i + 1);
			int index3 = 2 * indices.get(i + 2);

			shapeRenderer.triangle(scaleFactor() * vertices.get(index1), scaleFactor() * vertices.get(index1 + 1),
					scaleFactor() * vertices.get(index2), scaleFactor() * vertices.get(index2 + 1),
					scaleFactor() * vertices.get(index3), scaleFactor() * vertices.get(index3 + 1));
		}
	}

	protected float scaleFactor() {
		return getWidth();
	}

	protected float getMouseX() {
		return mouseListener.mouseX / getWidth();
	}

	protected float getMouseY() {
		return mouseListener.mouseY / getHeight();
	}

	protected void touchDown(float x, float y) {

	}

	protected void touchUp(float x, float y) {

	}

	protected boolean mousePressed() {
		return mouseListener.mousePressed;
	}
	
	public void setBounds(float x, float y, float size) {
		super.setBounds(x, y, size, size);
	}

}
