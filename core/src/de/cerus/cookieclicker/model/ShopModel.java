package de.cerus.cookieclicker.model;
/**
 * @author hundun
 * Created on 2021/10/29
 */

import de.cerus.cookieclicker.ui.other.component.ShopUI;
import lombok.Getter;
import lombok.Setter;

public class ShopModel {
    ShopUI parent;
    ModelContext modelContext;
    @Setter
    @Getter
    private long cookies = 0;
    @Setter
    @Getter
    private long clicker = 0;
    @Setter
    @Getter
    private long grandmas = 0;
    @Setter
    @Getter
    private long bakeries = 0;
    @Setter
    @Getter
    private long factories = 0;
    
    private boolean visible = false;
    
    public ShopModel(ShopUI parent, ModelContext modelContext) {
        this.parent = parent;
        this.modelContext = modelContext;
        modelContext.setShopModel(this);
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            parent.setAnimationAlpha(1.0f);
        }
    }
}
