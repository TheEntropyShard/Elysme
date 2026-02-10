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

public class DcContact {
    private String address;
    private String authName;
    private String color;
    private String displayName;
    private boolean e2eeAvail;
    private int id;
    private boolean isBlocked;
    private boolean isBot;
    private boolean isKeyContact;
    private boolean isVerified;
    private float lastSeen;
    private String name;
    private String nameAndAddr;
    private String profileImage;
    private String status;
    private int verifierId;
    private boolean wasSeenRecently;

    public DcContact() {

    }

    public String getAddress() {
        return this.address;
    }

    public String getAuthName() {
        return this.authName;
    }

    public String getColor() {
        return this.color;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isE2eeAvail() {
        return this.e2eeAvail;
    }

    public int getId() {
        return this.id;
    }

    public boolean isIsBlocked() {
        return this.isBlocked;
    }

    public boolean isIsBot() {
        return this.isBot;
    }

    public boolean isIsKeyContact() {
        return this.isKeyContact;
    }

    public boolean isIsVerified() {
        return this.isVerified;
    }

    public float getLastSeen() {
        return this.lastSeen;
    }

    public String getName() {
        return this.name;
    }

    public String getNameAndAddr() {
        return this.nameAndAddr;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public String getStatus() {
        return this.status;
    }

    public int getVerifierId() {
        return this.verifierId;
    }

    public boolean isWasSeenRecently() {
        return this.wasSeenRecently;
    }
}