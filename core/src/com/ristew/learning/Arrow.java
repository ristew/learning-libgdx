package com.ristew.learning;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;

/**
 * Created by riley on 5/14/2015.
 */
public class Arrow {
    public static int BOWTILE = 0;
    public static int ARROWTILE = 1;
    public static int HITTILE = 2;
    ArrayList<Sprite> arrowSprites;
    Sprite currentSprite;
    float dist, accuracy;
    float x, y;
    float orig_y;
    float speedX, speedY;
    float angle;
    boolean is_fired;
    boolean is_alive;
    public Arrow(float x, float y, float sX, float sY) {
        arrowSprites = new ArrayList<>();
        this.x = x;
        this.y = this.orig_y = y;
        this.speedX = sX;
        this.speedY = sY;
        calc_angle();
        is_alive = true;
        is_fired = false;
        accuracy = (float) Math.random() - 0.5f;
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
        if (Math.abs(speedX) < 5) {
            speedX += 0.1 * (speedX > 0 ? 1 : -1);
            currentSprite.setScale(Math.abs(speedX) / 4, 1f);

        }
        currentSprite.setRotation(5 * speedY * (speedX > 0 ? 1 : -1));
    }

    public void inc_angle() {
        speedY += 0.01;
        speedX -= 0.01 * (speedX > 0 ? 1 : -1);
    }

    public void fire() {
        currentSprite = arrowSprites.get(ARROWTILE);
        is_fired = true;
        speedX += accuracy;
        speedY += accuracy / 2;
    }

    void calc_angle() {
        angle = (float) Math.atan(speedY / Math.abs(speedX)) * 180 / (float) Math.PI;
    }

    public boolean update(float newX, float newY, boolean is_right) {
        if (is_fired) {
            dist += Math.sqrt(speedX * speedX + speedY * speedY);
            x += speedX;
            y += speedY;
            speedY -= 0.05;
            calc_angle();
            currentSprite.setRotation(angle * (speedX > 0 ? 1 : -1));
        }
        else if (!is_fired) {
            this.x = newX;
            this.y = this.orig_y = newY;
        }
        if (!(is_right ^ speedX < 0) && !is_fired) {
            speedX *= -1;
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
        return is_alive;
    }

    public void flip_sprites() {
        for (Sprite s : arrowSprites) {
            s.flip(true, false);
        }
    }

    public void draw(SpriteBatch sb) {
        currentSprite.draw(sb);
    }

    /*
    x2 + y2 = s2
    s2 = nx2 + ny2
     */
    public void set_angle_mouse(float mX, float mY) {

        mX = mX * 640 / Gdx.graphics.getWidth();
        mY = mY * 360 / Gdx.graphics.getHeight();
        System.out.println(mX + ", " + mY);
        float dX = mX - x - 260;
        float dY = (360 - mY) - y;
        if (dX == 0) {
            dX = 1;
        }
        float ratio = dY / dX;
        float speedTotOld = magnitude(speedX, speedY);
        float speedTotNew = magnitude(dX, dY);
        System.out.println("old ds: " + dX + ", " + dY);
        speedX = (float) Math.cos(Math.atan(ratio)) * speedTotOld;
        speedY = (float) Math.sin(Math.atan(ratio)) * speedTotOld;
        System.out.println("new ds: " + speedX + ", " + speedY);
    }

    float magnitude(float x, float y) {
        return (float) Math.sqrt(x * x + y * y);
    }

}
