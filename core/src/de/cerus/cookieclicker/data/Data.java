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

package de.cerus.cookieclicker.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;

import java.io.*;

public class Data {

    private static File file = new File(System.getenv("APPDATA")+"//CookieClicker//data.json");;

    public static Object[] loadProgress() {
        if(!file.exists()) return new Object[]{0, 0, 0, 0};

        try {
            JsonReader reader = new JsonReader();
            JsonValue value = reader.parse(new FileInputStream(file));

            return new Object[]{
                    value.get("cookies") == null ? 0 : value.get("cookies").asLong(),
                    value.get("clickers") == null ? 0 : value.get("clickers").asLong(),
                    value.get("grandmas") == null ? 0 : value.get("grandmas").asLong(),
                    value.get("bakeries") == null ? 0 : value.get("bakeries").asLong(),
                    value.get("factories") == null ? 0 : value.get("factories").asLong()
            };
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Object[]{0, 0, 0, 0};
    }

    public static void saveProgress(long cookies, long clickers, long grandmas, long bakeries, long factories) {
        try {
            file.getParentFile().mkdirs();
            file.createNewFile();

            JsonValue value = new JsonValue(JsonValue.ValueType.object);
            value.addChild("cookies", new JsonValue(cookies));
            value.addChild("clickers", new JsonValue(clickers));
            value.addChild("grandmas", new JsonValue(grandmas));
            value.addChild("bakeries", new JsonValue(bakeries));
            value.addChild("factories", new JsonValue(factories));

            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(value.toJson(JsonWriter.OutputType.json));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
