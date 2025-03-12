package com.dubiouscandle.sillycargame.utils;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MouseListener extends InputListener {
	public float mouseX;
	public float mouseY;
	public boolean mousePressed = false;

	@Override
	public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
		setMouse(x, y);
		mousePressed = true;
		return true;
	}

	@Override
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		setMouse(x, y);
		mousePressed = false;
	}

	@Override
	public void touchDragged(InputEvent event, float x, float y, int pointer) {
		setMouse(x, y);
	}

	@Override
	public boolean mouseMoved(InputEvent event, float x, float y) {
		setMouse(x, y);
		return true;
	}

	private void setMouse(float x, float y) {
		mouseX = x;
		mouseY = y;
	}
}
