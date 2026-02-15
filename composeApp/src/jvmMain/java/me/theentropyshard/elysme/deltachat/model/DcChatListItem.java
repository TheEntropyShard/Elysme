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

public class DcChatListItem {
    private String avatarPath;
    private String chatType;
    private String color;
    private int freshMessageCounter;
    private int id;
    private boolean isArchived;
    private boolean isContactRequest;
    private boolean isDeviceTalk;
    private boolean isEncrypted;
    private boolean isGroup;
    private boolean isMuted;
    private boolean isPinned;
    private boolean isSelfInGroup;
    private boolean isSelfTalk;
    private boolean isSendingLocation;
    private String kind;
    private int lastMessageId;
    private String lastMessageType;
    private long lastUpdated;
    private String name;
    private int summaryStatus;
    private String summaryText1;
    private String summaryText2;
    private boolean wasSeenRecently;

    public DcChatListItem() {

    }

    public String getAvatarPath() {
        return this.avatarPath;
    }

    public String getChatType() {
        return this.chatType;
    }

    public String getColor() {
        return this.color;
    }

    public int getFreshMessageCounter() {
        return this.freshMessageCounter;
    }

    public int getId() {
        return this.id;
    }

    public boolean isArchived() {
        return this.isArchived;
    }

    public boolean isContactRequest() {
        return this.isContactRequest;
    }

    public boolean isDeviceTalk() {
        return this.isDeviceTalk;
    }

    public boolean isEncrypted() {
        return this.isEncrypted;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public boolean isMuted() {
        return this.isMuted;
    }

    public boolean isPinned() {
        return this.isPinned;
    }

    public boolean isSelfInGroup() {
        return this.isSelfInGroup;
    }

    public boolean isSelfTalk() {
        return this.isSelfTalk;
    }

    public boolean isSendingLocation() {
        return this.isSendingLocation;
    }

    public String getKind() {
        return this.kind;
    }

    public int getLastMessageId() {
        return this.lastMessageId;
    }

    public String getLastMessageType() {
        return this.lastMessageType;
    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public String getName() {
        return this.name;
    }

    public int getSummaryStatus() {
        return this.summaryStatus;
    }

    public String getSummaryText1() {
        return this.summaryText1;
    }

    public String getSummaryText2() {
        return this.summaryText2;
    }

    public boolean isWasSeenRecently() {
        return this.wasSeenRecently;
    }
}
