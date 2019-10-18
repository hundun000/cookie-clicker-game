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

package de.cerus.cookieclicker.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Disposable;

import java.lang.reflect.Field;

public class DisposeUtil {

    private DisposeUtil() {
        throw new UnsupportedOperationException();
    }

    public static void dispose(Object obj) {
        for (Field field : obj.getClass().getDeclaredFields()) {
            if (field.getType().getName().equals("com.badlogic.gdx.utils.Disposable")) {
                try {
                    field.setAccessible(true);
                    Disposable disposable = (Disposable) field.get(obj);
                    if(disposable == null) continue;

                    disposable.dispose();
                    Gdx.app.log("Tag lol", "Successfully disposed " + field.getName()
                            + " in class " + obj.getClass().getName());
                } catch (IllegalAccessException e) {
                    Gdx.app.error("Tag? Idk lol", "Failed to dispose " + field.getName()
                            + " in class " + obj.getClass().getName(), e);
                }
            }
        }
    }
}
