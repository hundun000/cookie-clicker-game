package de.cerus.cookieclicker.ui.other.component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Ellipse;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.model.ModelContext;
import de.cerus.cookieclicker.ui.screens.GameScreen;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2021/10/29
 */
public class CookieBoardUI implements Disposable {
    
    private static int MINICOOKIE_WIDTH = 25;
    private static int MINICOOKIE_HEIGHT = 25;
    private static float MINICOOKIE_THRESHOLD = 10;
    private static float MINICOOKIE_SPEED = 0.8f;
    private static float MINICOOKIE_ROTATION_SPEED = 0.25f;

    private float COOKIE_WIDTH = 200;
    private float COOKIE_HEIGHT = 200;
    private float COOKIE_MAX_WIDTH = 200;
    private float COOKIE_MAX_HEIGHT = 200;

    
    private ModelContext modelContext;
    
    CookieClickerGame game;
    GameScreen parent;
    private Texture cookieTexture;
    private Texture clickerTexture;
    
    private Queue<CookieUI> cookieUIs = new ConcurrentLinkedQueue<>();
    
    private float generalRotation = 0;
    @Getter
    private Ellipse cookieRepresentation;
    
    public CookieBoardUI(GameScreen parent, CookieClickerGame game, ModelContext modelContext) {
        this.modelContext = modelContext;
        this.game = game;
        this.parent = parent;
        this.cookieTexture = new Texture(Gdx.files.internal("cookie.png"));
        this.clickerTexture = new Texture(Gdx.files.internal("pointer.png"));
        
        this.cookieRepresentation = new Ellipse();
    }
    
    public int getSize() {
        return cookieUIs.size();
    }
    
    public void boardRender(Camera camera) {
        game.getBatch().begin();
        cookieUIs.forEach(miniCookie -> {

            game.getBatch().draw(cookieTexture, miniCookie.getX(), miniCookie.getY(), MINICOOKIE_WIDTH, MINICOOKIE_HEIGHT);
            
        });
        game.getBatch().end();
        
        renderClicker();
        
        
    }

    public void addCookieUI(Camera camera) {
        if (MINICOOKIE_THRESHOLD == -1 || cookieUIs.size() <= MINICOOKIE_THRESHOLD) {
            cookieUIs.add(new CookieUI(MathUtils.random(5, camera.viewportWidth - 30), camera.viewportHeight + MINICOOKIE_HEIGHT, MathUtils.random(0.0f, 360.0f)));
        }
    }
    
    public void cookiesLogicFrame() {
        cookieUIs.forEach(miniCookie -> {
            
            if (miniCookie.getY() <= -MINICOOKIE_HEIGHT) {
                cookieUIs.remove(miniCookie);
            } else {
                miniCookie.setY(miniCookie.getY() - MINICOOKIE_SPEED);
                miniCookie.setRotation((miniCookie.getRotation() + MINICOOKIE_ROTATION_SPEED) % 360.0f);
            }
            
        });
        
        
    }

    @Override
    public void dispose() {
        cookieTexture.dispose();
        clickerTexture.dispose();
    }
    

    private void renderClicker() {
        System.out.println("renderClicker() clickerAnimationIndex = " + modelContext.getGameScreenModel().getClickerAnimationIndex());
        
        if (modelContext.getGameScreenModel().shopGetClicker() == 0) {
            return;
        }

        generalRotation += 0.1f;
        int row = 0;

        for (int i = 0; i < modelContext.getGameScreenModel().shopGetClicker(); i++) {
            if (i == 18 || i == 36) {
                row++;
            }

            float rotation = i * 20 + generalRotation;
            if (rotation > 360) {
                rotation = rotation % 360;
            }

            double angle = Math.toRadians(rotation);

            game.getBatch().begin();
            Vector2 center = new Vector2(
                    cookieRepresentation.x + COOKIE_MAX_WIDTH / 2f,
                    cookieRepresentation.y + COOKIE_MAX_HEIGHT / 2f
            );

            float x1 = cookieRepresentation.x + COOKIE_MAX_WIDTH / 2f - center.x;
            float y1 = center.y - COOKIE_MAX_HEIGHT / 2f - ((30 + (50 * row)) - (modelContext.getGameScreenModel().getClickerAnimationIndex() == i ? 10 : 0)) - center.y;

            float x2 = (float) (x1 * Math.cos(angle) - y1 * Math.sin(angle));
            float y2 = (float) (x1 * Math.sin(angle) + y1 * Math.cos(angle));

            Vector2 vec = new Vector2();
            vec.x = x2 + center.x;
            vec.y = y2 + center.y;

            game.getBatch().draw(
                    clickerTexture,
                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                    vec.y - (clickerTexture.getHeight() / 50f) / 2f,
                    (clickerTexture.getWidth() / 50f) / 2f,
                    (clickerTexture.getHeight() / 50f) / 2f,
                    clickerTexture.getWidth() / 50f,
                    clickerTexture.getHeight() / 50f,
                    1,
                    1,
                    getAngle(
                            center,
                            new Vector2(
                                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                                    vec.y - (clickerTexture.getHeight() / 50f) / 2f
                            )
                    ) + 90,
                    0,
                    0,
                    clickerTexture.getWidth(),
                    clickerTexture.getHeight(),
                    false,
                    false
            );
            game.getBatch().end();
        }
    }
    
    private float getAngle(Vector2 source, Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    public void renderPart2(Camera camera) {
        cookieRepresentation.set((camera.viewportWidth / 2f) - (COOKIE_WIDTH / 2f) - (COOKIE_WIDTH < 200 ? 5 : 0),
                (camera.viewportHeight / 2f) - (COOKIE_HEIGHT / 2f) - (COOKIE_HEIGHT < 200 ? 5 : 0), COOKIE_WIDTH, COOKIE_HEIGHT);
        game.getBatch().draw(cookieTexture, cookieRepresentation.x, cookieRepresentation.y,
                COOKIE_WIDTH, COOKIE_HEIGHT);
    }

    public void inputLogicFrame() {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && cookieRepresentation.contains(parent.getUnprojectedScreenCoords(100))
                && modelContext.getGameScreenModel().shopIsNotVisible()) {
            COOKIE_HEIGHT -= 10;
            COOKIE_WIDTH -= 10;

            modelContext.getGameScreenModel().shopAddCookies(1);
            parent.addCookieUI();
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && COOKIE_WIDTH < 200 && COOKIE_HEIGHT < 200
                && modelContext.getGameScreenModel().shopIsNotVisible()) {
            COOKIE_WIDTH = 200;
            COOKIE_HEIGHT = 200;
        }
    }


    


}
