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

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RpcRequest {
    @SerializedName("jsonrpc")
    private final String jsonRpc;

    @SerializedName("id")
    private int id;

    @SerializedName("method")
    private final String method;

    @SerializedName("params")
    private final List<Object> params;

    public RpcRequest(String method) {
        this(0, method);
    }

    public RpcRequest(int id, String method) {
        this(id, method, new ArrayList<>());
    }

    public RpcRequest(int id, String method, List<Object> params) {
        this("2.0", id, method, params);
    }

    public RpcRequest(String jsonRpc, int id, String method, List<Object> params) {
        this.jsonRpc = jsonRpc;
        this.id = id;
        this.method = method;
        this.params = params;
    }

    public void prepare() {

    }

    public void addParam(Object o) {
        this.params.add(o);
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
