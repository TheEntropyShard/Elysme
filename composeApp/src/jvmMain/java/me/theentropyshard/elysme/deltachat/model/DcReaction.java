/*
 * Elysme - https://github.com/TheEntropyShard/Elysme
 * Copyright (C) 2026 TheEntropyShard
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
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.elysme.deltachat.model;

public class DcReaction {
    private int count;
    private String emoji;
    private boolean isFromSelf;

    public DcReaction() {

    }

    @Override
    public String toString() {
        return "DcReaction{" +
            "count=" + this.count +
            ", emoji='" + this.emoji + '\'' +
            ", isFromSelf=" + this.isFromSelf +
            '}';
    }

    public int getCount() {
        return this.count;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public boolean isFromSelf() {
        return this.isFromSelf;
    }
}