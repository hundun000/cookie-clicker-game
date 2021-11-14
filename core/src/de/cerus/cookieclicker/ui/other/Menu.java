/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package de.cerus.cookieclicker.ui.other;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;

import java.util.function.Consumer;

public class Menu<T> {

    private Array<T> items;
    private Array<GlyphLayout> layouts;
    private BitmapFont font;
    private int selection;

    private Consumer<T> onAction = null;

    public Menu(BitmapFont font, Array<T> items) {
        this.items = items;
        this.layouts = new Array<>();
        this.font = font;
        this.selection = 0;

        for (T item : items) {
            layouts.add(new GlyphLayout(font, item.toString()));
        }
    }

    public void render(SpriteBatch batch, boolean renderSelection, boolean centered, float x, float y, float xSpace, float ySpace) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            changeSelection(true);
        } else if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            changeSelection(false);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            if (onAction != null) {
                onAction.accept(items.get(selection));
            }
        }

        for (GlyphLayout layout : layouts) {
            font.draw(batch, layout, centered ? x - (layout.width / 2f) : x, centered ? y - (layout.height / 2f) : y);

            if (layouts.indexOf(layout, false) == selection && renderSelection) {
                String selectionIndicator = ">";
                font.draw(batch, selectionIndicator, centered ? x - (layout.width / 2f) - 20 : x - 20, centered ? y - (layout.height / 2f) : y);
            }

            x += xSpace;
            y += ySpace;
        }
    }

    private void changeSelection(boolean up) {
        if (up) {
            selection--;
            if (selection < 0) {
                selection = items.size - 1;
            }
            return;
        }
        selection++;
        if (selection > items.size - 1) {
            selection = 0;
        }
    }

    public T getSelectedItem() {
        return items.get(selection);
    }

    public int getSelection() {
        return selection;
    }

    public void setOnAction(Consumer<T> onAction) {
        this.onAction = onAction;
    }
}
