package com.ristew.learning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ras on 5/12/15.
 */
public class Player implements InputProcessor {
    ArrayList<Sprite> playerSprites;
    public Sprite currentSprite;
    public float spriteDelta;
    public boolean is_right;
    int ticks;
    boolean is_moving;
    float x, y, speedX, speedY;
    private HashMap<Integer, Boolean> keys;

    public Player(float delta, float x, float y) {
        spriteDelta = delta;
        this.x = x;
        this.y = y;
        ticks = 1;
        is_right = true;
        is_moving = false;
        speedX = 1f;
        Gdx.input.setInputProcessor(this);
        keys = init_keys();
    }

    public void draw(SpriteBatch batch) {
        update();
        batch.draw(currentSprite, x, y);
    }

    public void load(String path) {
        playerSprites = new ArrayList<Sprite>();
        Texture img = new Texture(path);
        for (int i = 0; i < img.getWidth(); i += 32) {
            playerSprites.add(new Sprite(img, i, 0, 32, 32));
        }
        currentSprite = playerSprites.get(0);
    }

    void update() {
        if (keys.get(Keys.RIGHT)) {
            if (!is_right) {
                speedX = 0;
                flip_sprites();
            }
            is_right = true;
            inc_speed_x();
            x += speedX;
        }
        if (keys.get(Keys.LEFT)) {
            if (is_right) {
                speedX = 0;
                flip_sprites();
            }
            is_right = false;
            inc_speed_x();
            x -= speedX;
        }
        if (keys.get(Keys.UP)) {
            y += 0.5f;
        }
        if (keys.get(Keys.DOWN)) {
            y -= 0.5f;
        }
        ticks ++;
        if (!is_moving) {
            currentSprite = playerSprites.get(0);
        }
        else if (ticks > 10) {
            cycle_sprites();
            ticks = 0;
        }
    }

    void inc_speed_x() {
        if (speedX < 2) {
            speedX += 1 / (2 + 2 * speedX * speedX);
        }
    }

    void flip_sprites() {
        for (Sprite s : playerSprites) {
            s.flip(true, false);
        }
    }

    void cycle_sprites() {
        int nextSprite = playerSprites.indexOf(currentSprite) + 1;
        if (nextSprite >= playerSprites.size()) {
            nextSprite = 0;
        }
        currentSprite = playerSprites.get(nextSprite);
    }

    HashMap<Integer, Boolean> init_keys(){
        HashMap ret = new HashMap<Integer, Boolean>();
        ret.put(Keys.RIGHT, false);
        ret.put(Keys.LEFT, false);
        ret.put(Keys.UP, false);
        ret.put(Keys.DOWN, false);
        return ret;
    }

    @Override
    public boolean keyDown(int keycode) {
        keys.put(keycode, true);
        get_is_moving();
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        keys.put(keycode, false);
        get_is_moving();
        return false;
    }

    void get_is_moving() {
        is_moving = keys.get(Keys.LEFT) || keys.get(Keys.RIGHT) || keys.get(Keys.UP) || keys.get(Keys.DOWN);
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
