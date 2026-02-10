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

import me.theentropyshard.elysme.deltachat.rpc.RpcMethod;
import me.theentropyshard.elysme.deltachat.rpc.RpcRequest;

public class GetMessagesRequest extends RpcRequest {
    private int accountId;
    private int[] messageIds;

    public GetMessagesRequest() {
        super(RpcMethod.get_messages.name());
    }

    @Override
    public void prepare() {
        this.addParam(this.accountId);
        this.addParam(this.messageIds);
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setMessageIds(int[] messageIds) {
        this.messageIds = messageIds;
    }
}
