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

public class DcChat {
    private boolean archived;
    private boolean canSend;
    private String chatType;
    private String color;
    private int[] contactIds;
    private DcContact[] contacts;
    private int ephemeralTimer;
    private int freshMessageCounter;
    private int id;
    private boolean isContactRequest;
    private boolean isDeviceChat;
    private boolean isEncrypted;
    private boolean isMuted;
    private boolean isSelfTalk;
    private boolean isUnpromoted;
    private Object mailingListAddress;
    private String name;
    private int[] pastContactIds;
    private boolean pinned;
    private String profileImage;
    private boolean selfInGroup;
    private boolean wasSeenRecently;
    private String lastMessageType;
    private int lastMessageId;

    public DcChat() {
    
    }

    public boolean isArchived() {
        return this.archived;
    }

    public boolean isCanSend() {
        return this.canSend;
    }

    public String getChatType() {
        return this.chatType;
    }

    public String getColor() {
        return this.color;
    }

    public int[] getContactIds() {
        return this.contactIds;
    }

    public DcContact[] getContacts() {
        return this.contacts;
    }

    public int getEphemeralTimer() {
        return this.ephemeralTimer;
    }

    public int getFreshMessageCounter() {
        return this.freshMessageCounter;
    }

    public int getId() {
        return this.id;
    }

    public boolean isIsContactRequest() {
        return this.isContactRequest;
    }

    public boolean isIsDeviceChat() {
        return this.isDeviceChat;
    }

    public boolean isIsEncrypted() {
        return this.isEncrypted;
    }

    public boolean isIsMuted() {
        return this.isMuted;
    }

    public boolean isIsSelfTalk() {
        return this.isSelfTalk;
    }

    public boolean isIsUnpromoted() {
        return this.isUnpromoted;
    }

    public Object getMailingListAddress() {
        return this.mailingListAddress;
    }

    public String getName() {
        return this.name;
    }

    public int[] getPastContactIds() {
        return this.pastContactIds;
    }

    public boolean isPinned() {
        return this.pinned;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public boolean isSelfInGroup() {
        return this.selfInGroup;
    }

    public boolean isWasSeenRecently() {
        return this.wasSeenRecently;
    }

    public String getLastMessageType() {
        return this.lastMessageType;
    }

    public int getLastMessageId() {
        return this.lastMessageId;
    }
}