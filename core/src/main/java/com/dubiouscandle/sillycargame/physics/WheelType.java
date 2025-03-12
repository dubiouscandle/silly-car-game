package com.dubiouscandle.sillycargame.physics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.dubiouscandle.sillycargame.Main;
import com.dubiouscandle.sillycargame.utils.Geometry;

public enum WheelType {

	WHEELY("Wheel wheel", "Ol' reliable") {
		@Override
		public Wheel createWheel(float radius) {
			return new WheelyWheel(radius);
		}

		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			final Color fillColor = new Color(0, 0, 1, 1);
			final float strokeWeight = Main.StrokeWeight;

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(fillColor);
			shapeRenderer.circle(x, y, radius);

			shapeRenderer.setColor(0, 0, 0, 1);
			final int iterations = 30;
			float interval = MathUtils.PI2 / iterations;
			for (int i = 0; i < iterations; i++) {
				float ptheta = (i - 1) * interval;
				float theta = i * interval;

				float x1 = x + radius * MathUtils.cos(theta);
				float y1 = y + radius * MathUtils.sin(theta);
				float x2 = x + radius * MathUtils.cos(ptheta);
				float y2 = y + radius * MathUtils.sin(ptheta);

				shapeRenderer.rectLine(x1, y1, x2, y2, strokeWeight);
				shapeRenderer.circle(x2, y2, strokeWeight * 0.5f, iterations);
			}

			shapeRenderer.circle(x + 0.8f * radius * MathUtils.cos(0), y + 0.8f * radius * MathUtils.sin(0),
					radius * 0.1f, 30);
			shapeRenderer.circle(x + 0.8f * radius * MathUtils.cos(MathUtils.PI2 / 3),
					y + 0.8f * radius * MathUtils.sin(MathUtils.PI2 / 3), radius * 0.1f, 30);
			shapeRenderer.circle(x + 0.8f * radius * MathUtils.cos(MathUtils.PI2 * 2 / 3),
					y + 0.8f * radius * MathUtils.sin(MathUtils.PI2 * 2 / 3), radius * 0.1f, 30);
			shapeRenderer.end();

			shapeRenderer.end();
		}
	},
	DUBIOUS("Dubious wheel", "I hope you have a strong spine.") {
		final float halfSqrt2 = (float) Math.sqrt(2) * 0.5f;

		@Override
		public Wheel createWheel(float radius) {
			return new DubiousWheel(radius);
		}

		// technically for this i and using the radius as the apothem, since if i were
		// to use it as it was it will be too big
		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			radius *= halfSqrt2;

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 1, 1, 1);
			shapeRenderer.rect(x - radius, y - radius, radius * 2, radius * 2);
			shapeRenderer.setColor(0, 0, 0, 1);
			shapeRenderer.rectLine(x - radius, y + radius, x + radius, y + radius, Main.StrokeWeight);
			shapeRenderer.rectLine(x - radius, y - radius, x + radius, y - radius, Main.StrokeWeight);
			shapeRenderer.rectLine(x + radius, y - radius, x + radius, y + radius, Main.StrokeWeight);
			shapeRenderer.rectLine(x - radius, y - radius, x - radius, y + radius, Main.StrokeWeight);
			shapeRenderer.circle(x + radius, y + radius, Main.StrokeWeight / 2);
			shapeRenderer.circle(x - radius, y - radius, Main.StrokeWeight / 2);
			shapeRenderer.circle(x + radius, y - radius, Main.StrokeWeight / 2);
			shapeRenderer.circle(x - radius, y + radius, Main.StrokeWeight / 2);
			shapeRenderer.end();
		}
	},
	FLOWER("Flower wheel", "Flower power.") {
		@Override
		public Wheel createWheel(float radius) {
			return new FlowerWheel(radius);
		}

		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			FloatArray points = new FloatArray();

			final int iterations = 100;
			float interval = MathUtils.PI2 / iterations;
			for (int i = 0; i < iterations; i++) {
				float theta = interval * i;
				float mag = radius * (MathUtils.sin(5 * theta) + 2) * .4f;

				points.add(x + MathUtils.cos(theta) * mag, y + MathUtils.sin(theta) * mag);
			}
			shapeRenderer.begin(ShapeType.Filled);

			ShortArray indices = Main.triangulator.computeTriangles(points.items, 0, points.size);

			shapeRenderer.setColor(1, .7529f, .7961f, 1);
			for (int i = 0; i < indices.size; i += 3) {
				int index1 = 2 * indices.get(i);
				int index2 = 2 * indices.get(i + 1);
				int index3 = 2 * indices.get(i + 2);

				shapeRenderer.triangle(points.get(index1), points.get(index1 + 1), points.get(index2),
						points.get(index2 + 1), points.get(index3), points.get(index3 + 1));
			}

			shapeRenderer.setColor(0, 0, 0, 1);
			for (int i = 0; i < points.size; i += 2) {
				float x1 = points.items[(i) % points.size];
				float y1 = points.items[(i + 1) % points.size];
				float x2 = points.items[(i + 2) % points.size];
				float y2 = points.items[(i + 3) % points.size];

				shapeRenderer.rectLine(x1, y1, x2, y2, Main.StrokeWeight);
				shapeRenderer.circle(x1, y1, Main.StrokeWeight * 0.5f);
			}
			shapeRenderer.setColor(1, 1, 0, 1);
			shapeRenderer.circle(x, y, radius * 0.25f);
			shapeRenderer.end();

		}
	},
	RECURSIVE("Recursive wheel", "Wheelception.") {
		@Override
		public Wheel createWheel(float radius) {
			return new RecursiveWheel(radius);
		}

		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			// im lazy lol
			RecursiveWheel wheel = new RecursiveWheel(radius);

			World temp = new World(new Vector2(0, 0), false);
			wheel.addToWorld(temp);
			wheel.body.setType(BodyType.StaticBody);
			wheel.body.setTransform(x, y, 0);

			Vector2 pos = wheel.body.getPosition();

			wheel.transformedShape.clear();
			for (int i = 0; i < wheel.shape.size; i += 2) {
				wheel.vertex.set(wheel.shape.get((i) % wheel.shape.size), wheel.shape.get((i + 1) % wheel.shape.size));

				wheel.vertex.rotateRad(wheel.body.getAngle());
				wheel.vertex.add(pos.x, pos.y);

				wheel.transformedShape.add(wheel.vertex.x, wheel.vertex.y);
			}

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(0, 1, 0, 1);
			Geometry.fillPoly(shapeRenderer, wheel.transformedShape);
			shapeRenderer.setColor(0, 0, 0, 1);
			Geometry.drawPoly(shapeRenderer, wheel.transformedShape, Main.StrokeWeight);

			shapeRenderer.end();
		}

	}, // octo springs
	EGGY("Eggy wheel", "Symmetry is overrated.") {
		@Override
		public Wheel createWheel(float radius) {
			return new EggyWheel(radius);
		}

		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			FloatArray points = new FloatArray();

			final int iterations = 100;
			float interval = 2.5f / iterations;
			for (int i = 0; i < iterations; i++) {
				float paraY = -1.25f + interval * i;
				float paraX = (float) (0.78 * Math.pow(0.75, paraY) * Math.sqrt(1 - .64f * paraY * paraY));
				points.add(x + radius * paraX, y + radius * paraY);
			}
			for (int i = 0; i < iterations; i++) {
				float paraY = 1.25f - interval * i;
				float paraX = -(float) (0.78 * Math.pow(0.75, paraY) * Math.sqrt(1 - .64f * paraY * paraY));
				points.add(x + radius * paraX, y + radius * paraY);
			}

			shapeRenderer.begin(ShapeType.Filled);
			shapeRenderer.setColor(.792f, .659f, .573f, 1);
			Geometry.fillPoly(shapeRenderer, points);
			shapeRenderer.setColor(0, 0, 0, 1);
			Geometry.drawPoly(shapeRenderer, points, Main.StrokeWeight);

			shapeRenderer.end();
		}

	}, // egg shaped but offset
	DOUBLE("Infinity wheel", "Never stops... until it does") {
		@Override
		public Wheel createWheel(float radius) {
			return new InfinityWheel(radius);
		}

		@Override
		public void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius) {
			radius /= 2;

			final Color fillColor = new Color(1, .60f, .11f, 1);
			shapeRenderer.begin(ShapeType.Filled);

			shapeRenderer.setColor(fillColor);
			final int iterations = 100;
			float interval = MathUtils.PI2 / iterations;

			FloatArray shape = new FloatArray();

			for (int i = 0; i < iterations; i++) {
				float theta = i * interval;
				float r = radius * (1 + MathUtils.cos(2 * theta));
				shape.add(x + r * MathUtils.cos(theta), y + r * MathUtils.sin(theta));
			}

			shapeRenderer.setColor(fillColor);
			Geometry.fillPoly(shapeRenderer, shape);
			shapeRenderer.setColor(0, 0, 0, 1);
			Geometry.drawPoly(shapeRenderer, shape, Main.StrokeWeight);
			shapeRenderer.end();
		}

	};// two wheels stuck

	public final String name, desc;

	WheelType(String name, String desc) {
		this.name = name;
		this.desc = desc;
	}

	public abstract Wheel createWheel(float radius);

	public abstract void drawIcon(ShapeRenderer shapeRenderer, float x, float y, float radius);
}

/*
 */
