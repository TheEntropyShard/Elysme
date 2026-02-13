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

public class DcQuote {
    private String authorDisplayColor;
    private String authorDisplayName;
    private int chatId;
    private Object image;
    private boolean isForwarded;
    private String kind;
    private int messageId;
    private Object overrideSenderName;
    private String text;
    private String viewType;

    public DcQuote() {

    }

    public String getAuthorDisplayColor() {
        return this.authorDisplayColor;
    }

    public void setAuthorDisplayColor(String authorDisplayColor) {
        this.authorDisplayColor = authorDisplayColor;
    }

    public String getAuthorDisplayName() {
        return this.authorDisplayName;
    }

    public void setAuthorDisplayName(String authorDisplayName) {
        this.authorDisplayName = authorDisplayName;
    }

    public int getChatId() {
        return this.chatId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public Object getImage() {
        return this.image;
    }

    public void setImage(Object image) {
        this.image = image;
    }

    public boolean isForwarded() {
        return this.isForwarded;
    }

    public void setForwarded(boolean forwarded) {
        this.isForwarded = forwarded;
    }

    public String getKind() {
        return this.kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public Object getOverrideSenderName() {
        return this.overrideSenderName;
    }

    public void setOverrideSenderName(Object overrideSenderName) {
        this.overrideSenderName = overrideSenderName;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getViewType() {
        return this.viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }
}