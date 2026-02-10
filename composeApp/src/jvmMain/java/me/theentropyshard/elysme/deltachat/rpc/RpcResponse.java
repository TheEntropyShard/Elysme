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

package me.theentropyshard.elysme.deltachat.rpc;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

public class RpcResponse {
    @SerializedName("jsonrpc")
    private String jsonRpc;

    @SerializedName("id")
    private int id;

    @SerializedName("result")
    private JsonElement result;

    @SerializedName("error")
    private RpcError error;

    public RpcResponse() {

    }

    public String getJsonRpc() {
        return this.jsonRpc;
    }

    public int getId() {
        return this.id;
    }

    public JsonElement getResult() {
        return this.result;
    }

    public RpcError getError() {
        return this.error;
    }
}
