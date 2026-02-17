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

package me.theentropyshard.elysme.deltachat.request;

import com.google.gson.JsonObject;

import me.theentropyshard.elysme.deltachat.rpc.RpcMethod;
import me.theentropyshard.elysme.deltachat.rpc.RpcRequest;

public class SendMessageRequest extends RpcRequest {
    private int accountId;
    private int chatId;
    private String text;
    private Integer quotedMessageId;
    private String file;
    private String filename;

    public SendMessageRequest() {
        super(RpcMethod.send_msg.name());
    }

    @Override
    public void prepare() {
        this.addParam(this.accountId);
        this.addParam(this.chatId);

        JsonObject object = new JsonObject();

        if (this.text != null) {
            object.addProperty("text", this.text);
        }

        if (this.quotedMessageId != null) {
            object.addProperty("quotedMessageId", this.quotedMessageId);
        }

        if (this.file != null) {
            object.addProperty("file", this.file);
        }

        if (this.filename != null) {
            object.addProperty("filename", this.filename);
        }

        this.addParam(object);
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setChatId(int chatId) {
        this.chatId = chatId;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setQuotedMessageId(Integer quotedMessageId) {
        this.quotedMessageId = quotedMessageId;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}
