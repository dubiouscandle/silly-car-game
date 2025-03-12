package com.dubiouscandle.sillycargame.utils;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.FloatArray;
import com.badlogic.gdx.utils.ShortArray;
import com.dubiouscandle.sillycargame.Main;

public class Geometry {
	public static boolean testPoint(FloatArray shape, float x, float y) {
		int count = 0;
		int n = shape.size;

		for (int i = 0; i < n; i += 2) {
			float x1 = shape.items[i];
			float y1 = shape.items[i + 1];
			float x2 = shape.items[(i + 2) % n];
			float y2 = shape.items[(i + 3) % n];

			if (y1 > y2) {
				float tempX = x1, tempY = y1;
				x1 = x2;
				y1 = y2;
				x2 = tempX;
				y2 = tempY;
			}

			if (y > y1 && y <= y2) {
				float intersectX = x1 + (y - y1) * (x2 - x1) / (y2 - y1);
				if (intersectX > x) {
					count++;
				}
			}
		}

		return count % 2 == 1;
	}

	public static boolean edgeIntersects(float ax, float ay, float bx, float by, float cx, float cy, float dx, float dy,
			Vector2 returnVec) {
		final float epsilon = 1e-6f;

		float rx = bx - ax;
		float ry = by - ay;
		float sx = dx - cx;
		float sy = dy - cy;

		float rxs = cross(rx, ry, sx, sy);

		float acxDiff = cx - ax;
		float acyDiff = cy - ay;
		float numerator1 = cross(acxDiff, acyDiff, sx, sy);
		float numerator2 = cross(acxDiff, acyDiff, rx, ry);

		if (Math.abs(rxs) <= epsilon) {
			if (Math.abs(numerator1) >= epsilon) {
				if (returnVec != null) {
					returnVec.set(Float.NaN, Float.NaN);
				}
				return false;
			}

			if (collinearOverlap(ax, ay, bx, by, cx, cy, dx, dy)) {
				if (pointOnSegment(ax, ay, bx, by, cx, cy)) {
					if (returnVec != null) {
						returnVec.set(cx, cy);
					}
					return true;
				} else if (pointOnSegment(ax, ay, bx, by, dx, dy)) {
					if (returnVec != null) {
						returnVec.set(dx, dy);
					}
					return true;
				} else if (pointOnSegment(cx, cy, dx, dy, ax, ay)) {
					if (returnVec != null) {
						returnVec.set(ax, ay);
					}
					return true;
				} else if (pointOnSegment(cx, cy, dx, dy, bx, by)) {
					if (returnVec != null) {
						returnVec.set(bx, by);
					}
					return true;
				}
			}
			if (returnVec != null) {
				returnVec.set(Float.NaN, Float.NaN);
			}
			return false;
		}

		float t = numerator1 / rxs;
		float u = numerator2 / rxs;

		if (t >= 0 && t <= 1 && u >= 0 && u <= 1) {
			float interX = ax + t * rx;
			float interY = ay + t * ry;
			if (returnVec != null) {
				returnVec.set(interX, interY);
			}
			return true;
		} else {
			if (returnVec != null) {
				returnVec.set(Float.NaN, Float.NaN);
			}
			return false;
		}
	}

	private static float cross(float ax, float ay, float bx, float by) {
		return ax * by - ay * bx;
	}

	private static boolean collinearOverlap(float ax, float ay, float bx, float by, float cx, float cy, float dx,
			float dy) {
		return pointOnSegment(ax, ay, bx, by, cx, cy) || pointOnSegment(ax, ay, bx, by, dx, dy)
				|| pointOnSegment(cx, cy, dx, dy, ax, ay) || pointOnSegment(cx, cy, dx, dy, bx, by);
	}

	private static boolean pointOnSegment(float ax, float ay, float bx, float by, float px, float py) {
		final float EPS = 1e-6f;
		return (px <= Math.max(ax, bx) + EPS && px >= Math.min(ax, bx) - EPS && py <= Math.max(ay, by) + EPS
				&& py >= Math.min(ay, by) - EPS);
	}

	public static void closestPointPolygonPerim(FloatArray shape, float x, float y, Vector2 out) {
		float minDistSqrd = Float.POSITIVE_INFINITY;

		Vector2 query = new Vector2();
		float closestX = x, closestY = y;

		for (int i = 0; i < shape.size; i += 2) {
			float x1 = shape.items[i];
			float y1 = shape.items[i + 1];
			float x2 = shape.items[(i + 2) % shape.size];
			float y2 = shape.items[(i + 3) % shape.size];

			closestPointEdge(x1, y1, x2, y2, x, y, query);

			float dx = query.x - x;
			float dy = query.y - y;
			float len2 = dx * dx + dy * dy;
			if (len2 < minDistSqrd) {
				minDistSqrd = len2;
				closestX = query.x;
				closestY = query.y;
			}
		}

		out.set(closestX, closestY);
	}

	public static void closestPointEdge(float ax, float ay, float bx, float by, float x, float y, Vector2 out) {
		float bxMinusAx = bx - ax;
		float byMinusAy = by - ay;

		float t = ((x - ax) * bxMinusAx + (y - ay) * byMinusAy) / (bxMinusAx * bxMinusAx + byMinusAy * byMinusAy);

		if (t <= 0) {
			out.set(ax, ay);
		} else if (t >= 1) {
			out.set(bx, by);
		} else {
			out.set(ax + t * bxMinusAx, ay + t * byMinusAy);
		}
	}

	public static float dist2(float x1, float y1, float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;

		return dx * dx + dy * dy;
	}

	public static void drawPoly(ShapeRenderer shapeRenderer, FloatArray poly, float thickness) {
		for (int i = 0; i < poly.size; i += 2) {
			float x1 = poly.items[(i) % poly.size];
			float y1 = poly.items[(i + 1) % poly.size];
			float x2 = poly.items[(i + 2) % poly.size];
			float y2 = poly.items[(i + 3) % poly.size];

			shapeRenderer.rectLine(x1, y1, x2, y2, thickness);
			shapeRenderer.circle(x1, y1, thickness * 0.5f);
		}
	}

	public static void fillPoly(ShapeRenderer shapeRenderer, FloatArray poly) {
		ShortArray indices = Main.triangulator.computeTriangles(poly.items, 0, poly.size);

		for (int i = 0; i < indices.size; i += 3) {
			int index1 = 2 * indices.get(i);
			int index2 = 2 * indices.get(i + 1);
			int index3 = 2 * indices.get(i + 2);

			shapeRenderer.triangle(poly.get(index1), poly.get(index1 + 1), poly.get(index2), poly.get(index2 + 1),
					poly.get(index3), poly.get(index3 + 1));
		}
	}

	public static float triangleArea(float x1, float y1, float x2, float y2, float x3, float y3) {
		return 0.5f * Math.abs(x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));
	}

	public static void drawCircle(ShapeRenderer shapeRenderer, float x, float y, float radius, int iterations,
			float thickness) {
		float interval = MathUtils.PI2 / iterations;
		for (int i = 0; i < iterations; i++) {
			float ptheta = (i - 1) * interval;
			float theta = i * interval;

			float x1 = x + radius * MathUtils.cos(theta);
			float y1 = y + radius * MathUtils.sin(theta);
			float x2 = x + radius * MathUtils.cos(ptheta);
			float y2 = y + radius * MathUtils.sin(ptheta);

			shapeRenderer.rectLine(x1, y1, x2, y2, thickness);
			shapeRenderer.circle(x2, y2, thickness * 0.5f, iterations);
		}
	}

	public static void centerOfMass(FloatArray shape, Vector2 out) {
		float areaSum = 0;
		float xSum = 0;
		float ySum = 0;

		for (int i = 0; i < shape.size; i += 2) {
			float x_i = shape.items[i];
			float y_i = shape.items[i + 1];
			float x_ip1 = shape.items[(i + 2) % shape.size];
			float y_ip1 = shape.items[(i + 3) % shape.size];

			float commonTerm = (x_i * y_ip1 - x_ip1 * y_i);
			xSum += (x_i + x_ip1) * commonTerm;
			ySum += (y_i + y_ip1) * commonTerm;
			areaSum += commonTerm;
		}

		areaSum *= 1.0f / 2;
		areaSum = Math.abs(areaSum);
		xSum = Math.abs(xSum);
		ySum = Math.abs(ySum);

		out.x = 1.0f / (6 * areaSum) * xSum;
		out.y = 1.0f / (6 * areaSum) * ySum;
	}
}
