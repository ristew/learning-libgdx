package com.ristew.learning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by riley on 5/14/2015.
 */
public class Arrow extends MovingEntity {
    public static int BOWTILE = 0;
    public static int ARROWTILE = 1;
    public static int HITTILE = 2;
    ArrayList<Sprite> spriteList;
    ArrayList<Sprite> arrowSprites;
    Sprite currentSprite;
    public float accuracy;
    float x, y;
    float orig_y;
    float speedX, speedY, speed;
    float angle;
    boolean is_fired;
    public boolean is_alive;
    boolean is_right;
    public Arrow(float x, float y, float sX, float sY) {
        arrowSprites = new ArrayList<>();
        spriteList = arrowSprites;
        this.x = x;
        this.y = this.orig_y = y;
        this.speedX = sX;
        this.speedY = sY;
        calc_angle();
        speed = Math.abs(speedX) + Math.abs(speedY);
        is_alive = true;
        is_fired = false;
        accuracy = (float) Math.random() * 2 - 1f;
        load();
    }

    public void load() {
        Texture img = new Texture("core/assets/arrow.png");
        for (int i = 0; i < img.getWidth(); i += 16) {
            arrowSprites.add(new Sprite(img, i, 0, 16, 16));
        }
        if (speedX < 0) {
            flip_sprites();
        }
        currentSprite = arrowSprites.get(BOWTILE);
    }

    public void inc_speed_x() {
        if (Math.abs(speed) < 5) {
            speed += 0.1;
            currentSprite.setScale(speed / 4, 1f);

        }
        set_rotation();
    }

    public void dec_angle() {
        angle -= 0.02 * signX();
        set_rotation();
    }

    public void inc_angle() {
        angle += 0.02 * signX();
        set_rotation();
    }

    public void fire() {
        currentSprite = arrowSprites.get(ARROWTILE);
        currentSprite = arrowSprites.get(ARROWTILE);
        is_fired = true;
        speedX = (float) Math.cos(angle) * speed * (speedX > 0 ? 1 : -1);
        speedY = (float) Math.sin(angle) * speed * (speedX > 0 ? 1 : -1);
        System.out.println(angle + ", " + speedX + ", " + speedY);
    }

    void calc_angle() {
        angle = (float) Math.atan(((speedY / speedX)));

        set_rotation();
        //if (!is_right && currentSprite != null) currentSprite.setRotation((float) (180 - 180 / Math.PI * angle));
    }

    void set_rotation() {
        if (currentSprite != null) {
            currentSprite.setRotation((float) (angle * 180 / Math.PI));
        }
    }

    public void update_position(float posX, float posY, boolean right) {
        is_right = right;
        if (!is_fired) {
            x = posX;
            y = orig_y = posY;
        }
    }

    public void update() {
        if (is_fired) {
            x += speedX;
            y += speedY;
            speedY -= 0.05;
            calc_angle();
        }

        if (!(is_right ^ speedX < 0) && !is_fired) {
            speedX  = -speedX;
            angle = -angle;
            flip_sprites();
        }
        currentSprite.setX(x);
        currentSprite.setY(y);
        if (is_alive && (y < orig_y - 16 + accuracy)) {
            is_alive = false;

            if (World.instance().get_tile_type(x, y) != -1) {
                arrowSprites.get(HITTILE).setPosition(currentSprite.getX(), currentSprite.getY());
                arrowSprites.get(HITTILE).setRotation(currentSprite.getRotation());
                currentSprite = arrowSprites.get(HITTILE);
                World.instance().add_detritus(currentSprite);
            }
            speedX = speedY = 0;
        }

    }

    public void flip_sprites() {
        for (Sprite s : arrowSprites) {
            s.flip(true, true);
        }
    }

    float signX() {
        return (speedX > 0) ? 1 : -1;
    }

    public void draw(SpriteBatch sb) {
        currentSprite.draw(sb);
    }

}
