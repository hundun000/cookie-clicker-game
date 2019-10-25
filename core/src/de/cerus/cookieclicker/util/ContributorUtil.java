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

import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ContributorUtil {

    private static List<ContributorData> contributors = new ArrayList<>();
    private static ExecutorService service = Executors.newCachedThreadPool();

    private static boolean isLoading;
    private static boolean isLoaded;

    private ContributorUtil() {
        throw new UnsupportedOperationException();
    }

    public static void load() {
        load(aVoid -> {
        });
    }

    public static void load(Consumer<Void> callback) {
        isLoading = true;
        service.submit(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new java.net.URL("https://api.github.com/" +
                        "repos/RealCerus/cookie-clicker-game/contributors").openConnection();

                connection.setRequestProperty("User-Agent", "CookieClickerGameAgent");
                connection.setRequestProperty("Authorization", "416aa97d2d2cdcb0b9cdf156f65a8b9dd058645a");

                connection.setDoInput(true);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder builder = new StringBuilder();

                String s;
                while ((s = reader.readLine()) != null)
                    builder.append(s).append("\n");

                JsonReader jsonReader = new JsonReader();
                JsonValue value = jsonReader.parse(builder.toString());

                for (JsonValue jsonValue : value) {
                    contributors.add(new ContributorData(jsonValue.getString("login"), jsonValue.getInt("contributions")));
                }

                callback.accept(null);

                isLoading = false;
                isLoaded = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public static boolean isLoading() {
        return isLoading;
    }

    public static boolean isLoaded() {
        return isLoaded;
    }

    public static List<ContributorData> getContributors() {
        return contributors;
    }

    public static class ContributorData {

        private String name;
        private int commits;

        private ContributorData(String name, int commits) {
            this.name = name;
            this.commits = commits;
        }

        public String getName() {
            return name;
        }

        public int getCommits() {
            return commits;
        }
    }
}
