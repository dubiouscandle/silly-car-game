package com.dubiouscandle.sillycargame.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class ImageLoader {
	public static Texture redPointer;
	
	public static Texture textOverlay;
	
	public static void init() {
		redPointer = new Texture(Gdx.files.internal("red pointer.png"));
		textOverlay = new Texture(Gdx.files.internal("textOverlay.png"));
	}

}
