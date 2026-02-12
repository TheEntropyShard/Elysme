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

public class DcMessage {
    private int chatId;
    private int dimensionsHeight;
    private int dimensionsWidth;
    private String downloadState;
    private int duration;
    private Object error;
    private String file;
    private long fileBytes;
    private String fileMime;
    private String fileName;
    private int fromId;
    private boolean hasDeviatingTimestamp;
    private boolean hasHtml;
    private boolean hasLocation;
    private int id;
    private int infoContactId;
    private boolean isBot;
    private boolean isEdited;
    private boolean isForwarded;
    private boolean isInfo;
    private boolean isSetupmessage;
    private String kind;
    private int originalMsgId;
    private String overrideSenderName;
    private int parentId;
    private DcQuote quote;
    private DcReactions reactions;
    private float receivedTimestamp;
    private int savedMessageId;
    private DcContact sender;
    private Object setupCodeBegin;
    private boolean showPadlock;
    private float sortTimestamp;
    private int state;
    private String subject;
    private String systemMessageType;
    private String text;
    private int timestamp;
    private Object vcardContact;
    private String viewType;
    private Object webxdcHref;

    public DcMessage() {

    }

    public boolean hasText() {
        return this.text != null && !this.text.trim().isEmpty();
    }

    public boolean hasQuote() {
        return this.quote != null;
    }

    public boolean hasAttachment() {
        return this.file != null;
    }

    public int getChatId() {
        return this.chatId;
    }

    public int getDimensionsHeight() {
        return this.dimensionsHeight;
    }

    public int getDimensionsWidth() {
        return this.dimensionsWidth;
    }

    public String getDownloadState() {
        return this.downloadState;
    }

    public int getDuration() {
        return this.duration;
    }

    public Object getError() {
        return this.error;
    }

    public String getFile() {
        return this.file;
    }

    public long getFileBytes() {
        return this.fileBytes;
    }

    public String getFileMime() {
        return this.fileMime;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getFromId() {
        return this.fromId;
    }

    public boolean isHasDeviatingTimestamp() {
        return this.hasDeviatingTimestamp;
    }

    public boolean isHasHtml() {
        return this.hasHtml;
    }

    public boolean isHasLocation() {
        return this.hasLocation;
    }

    public int getId() {
        return this.id;
    }

    public int getInfoContactId() {
        return this.infoContactId;
    }

    public boolean isIsBot() {
        return this.isBot;
    }

    public boolean isIsEdited() {
        return this.isEdited;
    }

    public boolean isIsForwarded() {
        return this.isForwarded;
    }

    public boolean isIsInfo() {
        return this.isInfo;
    }

    public boolean isIsSetupmessage() {
        return this.isSetupmessage;
    }

    public String getKind() {
        return this.kind;
    }

    public int getOriginalMsgId() {
        return this.originalMsgId;
    }

    public String getOverrideSenderName() {
        return this.overrideSenderName;
    }

    public int getParentId() {
        return this.parentId;
    }

    public DcQuote getQuote() {
        return this.quote;
    }

    public DcReactions getReactions() {
        return this.reactions;
    }

    public float getReceivedTimestamp() {
        return this.receivedTimestamp;
    }

    public int getSavedMessageId() {
        return this.savedMessageId;
    }

    public DcContact getSender() {
        return this.sender;
    }

    public Object getSetupCodeBegin() {
        return this.setupCodeBegin;
    }

    public boolean isShowPadlock() {
        return this.showPadlock;
    }

    public float getSortTimestamp() {
        return this.sortTimestamp;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getSystemMessageType() {
        return this.systemMessageType;
    }

    public String getText() {
        return this.text;
    }

    public int getTimestamp() {
        return this.timestamp;
    }

    public Object getVcardContact() {
        return this.vcardContact;
    }

    public String getViewType() {
        return this.viewType;
    }

    public Object getWebxdcHref() {
        return this.webxdcHref;
    }
}