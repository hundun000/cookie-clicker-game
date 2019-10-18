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

package de.cerus.cookieclicker.fixes;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

public class CappedArray<T> extends Array<T> {

    private long cap;

    public CappedArray(long cap) {
        this.cap = cap;
    }

    public CappedArray(int capacity, long cap) {
        super(capacity);
        this.cap = cap;
    }

    public CappedArray(boolean ordered, int capacity, long cap) {
        super(ordered, capacity);
        this.cap = cap;
    }

    public CappedArray(boolean ordered, int capacity, Class arrayType, long cap) {
        super(ordered, capacity, arrayType);
        this.cap = cap;
    }

    public CappedArray(Class arrayType, long cap) {
        super(arrayType);
        this.cap = cap;
    }

    public CappedArray(Array<? extends T> array, long cap) {
        super(array);
        this.cap = cap;
    }

    public CappedArray(T[] array, long cap) {
        super(array);
        this.cap = cap;
    }

    public CappedArray(boolean ordered, T[] array, int start, int count, long cap) {
        super(ordered, array, start, count);
        this.cap = cap;
    }

    @Override
    public void add(T value) {
        if (size >= cap) {
            if (value instanceof Disposable)
                ((Disposable) value).dispose();
            return;
        }
        super.add(value);
    }

    @Override
    public void add(T value1, T value2) {
        if (size >= cap) {
            if (value1 instanceof Disposable)
                ((Disposable) value1).dispose();
            if (value2 instanceof Disposable)
                ((Disposable) value2).dispose();
            return;
        }
        super.add(value1, value2);
    }

    @Override
    public void add(T value1, T value2, T value3) {
        if (size >= cap) {
            if (value1 instanceof Disposable)
                ((Disposable) value1).dispose();
            if (value2 instanceof Disposable)
                ((Disposable) value2).dispose();
            if (value3 instanceof Disposable)
                ((Disposable) value3).dispose();
            return;
        }
        super.add(value1, value2, value3);
    }

    @Override
    public void add(T value1, T value2, T value3, T value4) {
        if (size >= cap) {
            if (value1 instanceof Disposable)
                ((Disposable) value1).dispose();
            if (value2 instanceof Disposable)
                ((Disposable) value2).dispose();
            if (value3 instanceof Disposable)
                ((Disposable) value3).dispose();
            if (value4 instanceof Disposable)
                ((Disposable) value4).dispose();
            return;
        }
        super.add(value1, value2, value3, value4);
    }

    @Override
    public void addAll(T[] array, int start, int count) {
        if (size >= cap) {
            for (T value : array) {
                if (value instanceof Disposable)
                    ((Disposable) value).dispose();
            }
            return;
        }
        super.addAll(array, start, count);
    }
}
