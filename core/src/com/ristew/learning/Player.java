package com.ristew.learning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.utils.Timer;

import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

/**
 * Created by ras on 5/12/15.
 */
public class Player extends MovingEntity implements InputProcessor {
    ArrayList<Sprite> spriteList;
    ArrayList<Sprite> playerSprites;
    public Sprite currentSprite;
    public float spriteDelta;
    public boolean is_right;
    int ticks;
    boolean is_moving, can_fire;
    float x, y, speedX, speedY;

    Arrow playerArrow;
    ArrayList<Arrow> arrows;
    // maps Keys.TYPE to true/false for maximum laziness
    private HashMap<Integer, Boolean> keys;

    public Player(float delta, float x, float y) {
        spriteDelta = delta;
        this.x = x;
        this.y = y;
        ticks = 1;
        is_right = true;
        is_moving = false;
        can_fire = true;
        speedX = 1f;
        Gdx.input.setInputProcessor(this);
        // have to set keys used in map initially to false or else bad things happen
        keys = init_keys();
        arrows = new ArrayList<>();
        load("core/assets/fox-idle.png");
        spriteList = playerSprites;
    }

    public void draw(SpriteBatch sb) {
        update();
        currentSprite.draw(sb);
        for (Arrow a : arrows) {
            a.draw(sb);
        }
    }

    public void load(String path) {
        playerSprites = new ArrayList<>();
        Texture img = new Texture(path);
        for (int i = 0; i < img.getWidth(); i += 32) {
            playerSprites.add(new Sprite(img, i, 0, 32, 32));
        }
        currentSprite = playerSprites.get(0);
    }

    public void update() {
        if (get(Keys.RIGHT)) {
            if (!is_right) {
                speedX = 0;
                flip_sprites();
            }
            is_right = true;
            inc_speed_x();
        }
        if (get(Keys.LEFT)) {
            if (is_right) {
                speedX = 0;
                flip_sprites();
            }
            is_right = false;
            dec_speed_x();
        }
        if (get(Keys.UP)) {
            speedY = 0.5f;
        }
        if (get(Keys.DOWN)) {
            speedY = -0.5f;
        }
        if (get(Keys.SPACE) && can_fire) {
            attack();
        }
        else if (!get(Keys.SPACE) && playerArrow != null && !playerArrow.is_fired) {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    can_fire = true;
                }
            }, 0.5f);
            playerArrow.fire();
        }
        if (get(Keys.NUM_1) && playerArrow != null) {
            playerArrow.inc_angle();
        }
        if (get(Keys.NUM_2) && playerArrow != null) {
            playerArrow.dec_angle();
        }
        else if (playerArrow != null) {
            playerArrow.inc_speed_x();
        }
        ticks++;
        if (!is_moving) {
            currentSprite = playerSprites.get(0);
        }
        else if (ticks > 10) {
            cycle_sprites();
            ticks = 0;
        }
        if (playerArrow != null) {
            playerArrow.update_position(x + 8, y + 8, is_right);
            playerArrow.update();
            if (!playerArrow.is_alive) {
                arrows.remove(playerArrow);
                playerArrow = null;
            }
        }
        Vector<Integer> removals = new Vector<>();
        for (Arrow a : arrows) {
            if (a != playerArrow) {
                a.update();
                if (!a.is_alive) {
                    removals.add(arrows.indexOf(a));
                }
            }
        }
        for (Integer i : removals) {
            Arrow temp = arrows.get(i);
            arrows.remove(temp);
        }
        if (is_moving) {
            move_player();
        }
        currentSprite.setPosition(x, y);
    }

    void inc_speed_x() {
        if (speedX < 1) {
            speedX += 1 / (2 + 2 * speedX * speedX);
        }
    }

    void dec_speed_x() {
        if (speedX > -1) {
            speedX -= 1 / (2 + 2 * speedX * speedX);
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

    void move_player() {
        if (World.instance().get_tile_type(x + speedX, y) == World.GRASSTILE) {
            x += speedX;
        }
        if (World.instance().get_tile_type(x, y + speedY) == World.GRASSTILE) {
            y += speedY;
        }

    }

    void attack() {
        can_fire = false;
        playerArrow = new Arrow(x + 16, y + 8, 2 * (is_right ? 1 : -1), 0.5f);
        arrows.add(playerArrow);
    }

    boolean get(Integer key) {
        if (!keys.containsKey(key)) {
            keys.put(key, false);
        }
        return keys.get(key);
    }

    HashMap<Integer, Boolean> init_keys(){
        HashMap ret = new HashMap<>();
        ret.put(Keys.RIGHT, false);
        ret.put(Keys.LEFT, false);
        ret.put(Keys.UP, false);
        ret.put(Keys.DOWN, false);
        ret.put(Keys.SPACE, false);
        ret.put(Keys.NUM_1, false);
        ret.put(Keys.NUM_2, false);
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
        is_moving = get(Keys.LEFT) || get(Keys.RIGHT) || get(Keys.UP) || get(Keys.DOWN);
        if (!get(Keys.LEFT) || get(Keys.RIGHT)) {
            speedX = 0f;
        }
        if (!get(Keys.UP) || get(Keys.DOWN)) {
            speedY = 0f;
        }
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
