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

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class CustomShapeRenderer extends ShapeRenderer {

    /**
     * Draws a rectangle with rounded corners of the given radius.
     */
    public void roundedRect(float x, float y, float width, float height, float radius) {
        // Central rectangle
        super.rect(x + radius, y + radius, width - 2 * radius, height - 2 * radius);

        // Four side rectangles, in clockwise order
        super.rect(x + radius, y, width - 2 * radius, radius);
        super.rect(x + width - radius, y + radius, radius, height - 2 * radius);
        super.rect(x + radius, y + height - radius, width - 2 * radius, radius);
        super.rect(x, y + radius, radius, height - 2 * radius);

        // Four arches, clockwise too
        super.arc(x + radius, y + radius, radius, 180f, 90f);
        super.arc(x + width - radius, y + radius, radius, 270f, 90f);
        super.arc(x + width - radius, y + height - radius, radius, 0f, 90f);
        super.arc(x + radius, y + height - radius, radius, 90f, 90f);
    }
}