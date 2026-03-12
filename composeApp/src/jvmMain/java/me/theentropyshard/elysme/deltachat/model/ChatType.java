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

public enum ChatType {
    Single,
    Group,
    Mailinglist,
    InBroadcast,
    OutBroadcast;

    public static ChatType getByName(String name) {
        return switch (name) {
            case "Single" -> ChatType.Single;
            case "Group" -> ChatType.Group;
            case "Mailinglist" -> ChatType.Mailinglist;
            case "InBroadcast" -> ChatType.InBroadcast;
            case "OutBroadcast" -> ChatType.OutBroadcast;

            default -> throw new RuntimeException("Unsupported chat type: " + name);
        };
    }
}
