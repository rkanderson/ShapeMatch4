package com.shsappdev.sm4;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Main extends Game {
	public static final int WIDTH=480, HEIGHT=800;
	public static final String title = "SHAPE MATCH 4";

	Main game;
	/* WARNING Using AssetManager in a static way can cause issues, especially on Android.
	Instead you may want to pass around Assetmanager to those the classes that need it.
	We will use it in the static context to save time for now. */

	@Override
	public void create () {
		game = this;
		setScreen(new MenuScreen(this));
	}


	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render () {

		super.render();
	}
}

