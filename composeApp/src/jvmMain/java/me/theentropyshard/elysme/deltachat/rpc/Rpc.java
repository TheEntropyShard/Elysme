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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Rpc {
    private final String rpcServerPath;
    private final String dcAccountsPath;

    private Process process;
    private Thread readThread;
    private Thread writeThread;
    private Thread eventThread;
    private BlockingDeque<RpcRequest> outboundRequestsQueue;
    private Map<Integer, BlockingDeque<JsonObject>> eventQueues;
    private Map<Integer, BlockingDeque<RpcResponse>> responseMap;
    private AtomicBoolean running;
    private AtomicInteger idCounter;

    public Rpc(String rpcServerPath, String dcAccountsPath) {
        this.rpcServerPath = rpcServerPath;
        this.dcAccountsPath = dcAccountsPath;
    }

    private Process createProcess() throws IOException {
        ProcessBuilder builder = new ProcessBuilder(this.rpcServerPath);
        builder.environment().put("DC_ACCOUNTS_PATH", this.dcAccountsPath);

        return builder.start();
    }

    public synchronized void start() {
        try {
            this.process = this.createProcess();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.outboundRequestsQueue = new LinkedBlockingDeque<>();
        this.eventQueues = new ConcurrentHashMap<>();
        this.responseMap = new ConcurrentHashMap<>();

        this.idCounter = new AtomicInteger(1);
        this.running = new AtomicBoolean(true);

        this.readThread = new Thread(this::readLoop);
        this.readThread.start();

        this.writeThread = new Thread(this::writeLoop);
        this.writeThread.start();

        this.eventThread = new Thread(this::eventsLoop);
        this.eventThread.start();

        this.send(RpcMethod.start_io_for_all_accounts.makeRequest());
    }

    public void stop() {
        try {
            this.running.set(false);
            this.send(RpcMethod.stop_io_for_all_accounts.makeRequest(this.idCounter.getAndIncrement()));
            this.readThread.join();
            this.writeThread.join();
            this.eventThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public RpcResponse send(RpcRequest request) {
        request.setId(this.idCounter.getAndIncrement());
        LinkedBlockingDeque<RpcResponse> queue = new LinkedBlockingDeque<>();
        this.responseMap.put(request.getId(), queue);
        this.outboundRequestsQueue.offer(request);
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void readLoop() {
        try {
            Gson gson = new GsonBuilder().disableJdkUnsafe().disableHtmlEscaping().create();
            BufferedReader reader = new BufferedReader(new InputStreamReader(this.process.getInputStream(), StandardCharsets.UTF_8));

            String line;
            while ((line = reader.readLine()) != null) {
                RpcResponse rpcResponse = gson.fromJson(line, RpcResponse.class);
                this.responseMap.get(rpcResponse.getId()).offer(rpcResponse);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeLoop() {
        try {
            Gson gson = new GsonBuilder().disableJdkUnsafe().disableHtmlEscaping().create();
            OutputStream outputStream = this.process.getOutputStream();
            while (true) {
                RpcRequest request = this.outboundRequestsQueue.take();
                request.prepare();
                outputStream.write((gson.toJson(request) + "\n").getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void eventsLoop() {
        while (this.running.get()) {
            RpcResponse r = this.send(RpcMethod.get_next_event.makeRequest(this.idCounter.getAndIncrement()));
            JsonObject event = r.getResult().getAsJsonObject();
            int accountId = event.get("contextId").getAsInt();
            BlockingDeque<JsonObject> queue = this.eventQueues.computeIfAbsent(accountId, i -> new LinkedBlockingDeque<>());
            queue.offer(event.get("event").getAsJsonObject());
        }
    }

    public JsonObject waitForEvent(int accountId) {
        try {
            return this.eventQueues.computeIfAbsent(accountId, i -> new LinkedBlockingDeque<>()).take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public AtomicBoolean getRunning() {
        return this.running;
    }
}
