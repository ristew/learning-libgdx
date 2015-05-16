package com.ristew.learning;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.*;
import java.util.ArrayList;

public class LearningGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	BitmapFont font;
	Player player;
	Viewport viewport;
	Camera camera;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(1, 1, 0, 1);
		player = new Player(0.2f, 100f, 100f);
		camera = new OrthographicCamera();
		viewport = new StretchViewport(640, 360, camera);
		viewport.apply();
	}

	@Override
	public void render () {
		camera.position.x = player.x + camera.viewportWidth / 2 - 150;
		camera.position.y = camera.viewportHeight / 2;
		camera.update();
		batch.setProjectionMatrix(camera.combined);
		Gdx.gl.glClearColor(0.5f, 0.8f, 1f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		World.instance().draw(batch);
		player.draw(batch);
		batch.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
		font.dispose();
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		System.out.println(viewport.getScreenHeight() + " " + height);
		camera.update();
	}
}
