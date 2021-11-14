package de.cerus.cookieclicker.model;

import java.util.function.Supplier;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import de.cerus.cookieclicker.CookieClickerGame;
import de.cerus.cookieclicker.ui.other.component.CookieUI;
import de.cerus.cookieclicker.ui.other.component.ShopUI;
import de.cerus.cookieclicker.ui.screens.GameScreen;
import de.cerus.cookieclicker.util.SaveUtil;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hundun
 * Created on 2021/10/29
 */
public class GameScreenModel {
    
    private GameScreen parent;
    //private Shop modelContext.getShopScreenModel();
    @Getter
    private double cookiesPerSecond;
    @Getter
    private  long lastCookies;
    @Getter
    @Setter
    private int clickerAnimationIndex = -1;
    
    private ModelContext modelContext;
    
    private static final int WOKER_DELAY_FRAME = 15;
    int workDelayCount = 0;
    
    public GameScreenModel(GameScreen parent, ModelContext modelContext) {
        this.parent = parent;
        this.modelContext = modelContext;
        modelContext.setGameScreenModel(this);
    }
    
    public void logicFrame() {

        workDelayCount++;
        if (workDelayCount < WOKER_DELAY_FRAME) {
            return;
        } else {
            workDelayCount -= WOKER_DELAY_FRAME;
        }
        
        cookiesPerSecond = modelContext.getShopModel().getCookies() - lastCookies;
        lastCookies = modelContext.getShopModel().getCookies();
        
        clickerAnimationIndex++;
        if (clickerAnimationIndex >= modelContext.getShopModel().getClicker()) {
            clickerAnimationIndex = 0;
        }

        
        generatorWork(() -> modelContext.getShopModel().getClicker(), 1, "Clicker");
        generatorWork(() -> modelContext.getShopModel().getGrandmas(), 2, "Grandma");
        generatorWork(() -> modelContext.getShopModel().getBakeries(), 4, "Bakerie");
        generatorWork(() -> modelContext.getShopModel().getFactories(), 10, "Factoriy");
        
    }
    
    
    private void generatorWork(Supplier<Long> wokerNumGetter, int increase, String name) {

        long sum = wokerNumGetter.get() * increase;
        System.out.println(name + " increase " + sum);
        modelContext.getShopModel().setCookies(modelContext.getShopModel().getCookies() + sum * increase);
        for (int i = 0; i < sum; i++) {
            parent.addCookieUI();
        }

    }

    public long shopGetCookies() {
        return modelContext.getShopModel().getCookies();
    }

    public void shopAddCookies(int i) {
        modelContext.getShopModel().setCookies(modelContext.getShopModel().getCookies() + i);
    }

    public boolean shopIsNotVisible() {
        return !modelContext.getShopModel().isVisible();
    }

    public void shopToggleVisible() {
        modelContext.getShopModel().setVisible(!modelContext.getShopModel().isVisible());
    }

    public void shopSetSaveData(long cookies, long clicker, long grandmas, long bakeries, long factories) {
        modelContext.getShopModel().setCookies(cookies);
        modelContext.getShopModel().setClicker(clicker);
        modelContext.getShopModel().setGrandmas(grandmas);
        modelContext.getShopModel().setBakeries(bakeries);
        modelContext.getShopModel().setFactories(factories);
    }

    private void addShutdownHookSaveData() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> SaveUtil.saveProgress(
                modelContext.getShopModel().getCookies(),
                modelContext.getShopModel().getClicker(),
                modelContext.getShopModel().getGrandmas(),
                modelContext.getShopModel().getBakeries(),
                modelContext.getShopModel().getFactories()
        )));
    }


    public long shopGetClicker() {
        return modelContext.getShopModel().getClicker();
    }

    public void loadDataForShop() {
        Object[] objects = SaveUtil.loadProgress();
        this.shopSetSaveData(
                objects[0] instanceof Long ? (Long) objects[0] : (Integer) objects[0],
                objects[1] instanceof Long ? (Long) objects[1] : (Integer) objects[1],
                objects[2] instanceof Long ? (Long) objects[2] : (Integer) objects[2],
                objects[3] instanceof Long ? (Long) objects[3] : (Integer) objects[3],
                objects[4] instanceof Long ? (Long) objects[4] : (Integer) objects[4]
                        );
        this.addShutdownHookSaveData();
            

    }
    

}
