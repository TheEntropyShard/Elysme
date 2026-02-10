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

    public String getAuthorDisplayName() {
        return this.authorDisplayName;
    }

    public int getChatId() {
        return this.chatId;
    }

    public Object getImage() {
        return this.image;
    }

    public boolean isIsForwarded() {
        return this.isForwarded;
    }

    public String getKind() {
        return this.kind;
    }

    public int getMessageId() {
        return this.messageId;
    }

    public Object getOverrideSenderName() {
        return this.overrideSenderName;
    }

    public String getText() {
        return this.text;
    }

    public String getViewType() {
        return this.viewType;
    }
}