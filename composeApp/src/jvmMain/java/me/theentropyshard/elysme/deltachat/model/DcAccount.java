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

public class DcAccount {
    private String addr;
    private String color;
    private String displayName;
    private int id;
    private String kind;
    private String privateTag;
    private String profileImage;

    public DcAccount() {

    }

    public String getAddr() {
        return this.addr;
    }

    public String getColor() {
        return this.color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public int getId() {
        return this.id;
    }

    public String getKind() {
        return this.kind;
    }

    public String getPrivateTag() {
        return this.privateTag;
    }

    public String getProfileImage() {
        return this.profileImage;
    }
}
