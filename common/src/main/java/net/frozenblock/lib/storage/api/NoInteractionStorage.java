/*
 * Copyright (C) 2024 FrozenBlock
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.frozenblock.lib.storage.api;

// TODO: [Liuk] ok now add it
// all it does it just restrict access from blocks like hoppers and copper pipes, its used for the stone chest
public class NoInteractionStorage<T> {
	/*

    @Override
    public long insert(T resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(T resource, long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public Iterator<StorageView<T>> iterator() {
        return new Iterator<>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public StorageView<T> next() {
                return null;
            }
        };
    }*/
}
