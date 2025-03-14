package com.dubiouscandle.sillycargame.drawing;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.FloatArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.physics.WheelPlacement;
import com.dubiouscandle.sillycargame.physics.WheelType;
import com.dubiouscandle.sillycargame.utils.Geometry;

public class WheelPanel extends Canvas {
	Array<WheelPlacement> placements = new Array<>();

	FloatArray shape;
	WheelType draggingType;
	private boolean dragging = false;
	private float dragX, dragY;
	float dragRadius;

	Vector2 closest = new Vector2();

	@Override
	public void drawShape(ShapeRenderer shapeRenderer) {
		super.drawShape(shapeRenderer);

		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(0, 1, 0, 1);
		fillPoly(shape, shapeRenderer);

		shapeRenderer.setColor(0, 0, 0, 1);
		drawPoly(shape, shapeRenderer, Main.StrokeWeight);
		shapeRenderer.end();

		for (WheelPlacement placed : placements) {
			placed.type.drawIcon(shapeRenderer, placed.x * scaleFactor(), placed.y * scaleFactor(),
					placed.radius * scaleFactor());
		}

		if (dragging && 0 <= dragX && dragX <= 1 && 0 <= dragY && dragY <= 1) {
			draggingType.drawIcon(shapeRenderer, closest.x * scaleFactor(), closest.y * scaleFactor(),
					dragRadius * scaleFactor());
		}
	}

	public void releaseDrag(Vector2 draggingPos, WheelType draggingType, float dragRadius) {
		dragX = (draggingPos.x - getX()) / scaleFactor();
		dragY = (draggingPos.y - getY()) / scaleFactor();

		dragging = false;
		if (dragX < 0 || dragX > 1 || dragY < 0 || dragY > 1) {
			return;
		}

		this.draggingType = draggingType;
		this.dragRadius = dragRadius;
		updateClosest();

		placements.add(new WheelPlacement(draggingType, closest.x, closest.y, dragRadius));

	}

	public void updateDrag(Vector2 draggingPos, WheelType draggingType, float dragRadius) {
		dragX = (draggingPos.x - getX()) / scaleFactor();
		dragY = (draggingPos.y - getY()) / scaleFactor();
		dragging = true;
		this.draggingType = draggingType;
		this.dragRadius = dragRadius;

		updateClosest();
	}

	private void updateClosest() {
		if (Geometry.testPoint(shape, dragX, dragY)) {
			closest.set(dragX, dragY);
		} else {
			Geometry.closestPointPolygonPerim(shape, dragX, dragY, closest);
		}
	}

	public void reset() {
		placements.clear();
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
		
		Main.font.setColor(0, 0, 0, 1);
		Main.font.getData().setScale(1);
		Main.font.draw(batch, "add some goofy wheels by dragging and dropping (or not, i dont really care)", 0,
				Main.WindowHeight * 0.95f, Main.WindowWidth, Align.center, true);
	}

}
