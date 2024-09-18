/*
 * Copyright 2017-2024 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.noear.solon.boot.vertx.websocket;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.ServerWebSocket;
import org.noear.solon.Utils;
import org.noear.solon.core.util.RunUtil;
import org.noear.solon.net.websocket.WebSocketTimeoutBase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

/**
 * @author noear
 * @since 3.0
 */
public class VxWebSocketImpl extends WebSocketTimeoutBase {
    private ServerWebSocket real;

    public VxWebSocketImpl(ServerWebSocket real) {
        this.real = real;
        this.init(buildUri(real));
    }

    public URI buildUri(ServerWebSocket req) {
        if (Utils.isEmpty(req.query())) {
            return URI.create(req.uri());
        } else {
            if (req.uri().contains("?")) {
                return URI.create(req.uri());
            } else {
                return URI.create(req.uri() + "?" + req.query());
            }
        }
    }

    @Override
    public boolean isValid() {
        return isClosed() == false;
    }

    @Override
    public boolean isSecure() {
        return real.isSsl();
    }

    @Override
    public InetSocketAddress remoteAddress() throws IOException {
        return (InetSocketAddress) real.remoteAddress();
    }

    @Override
    public InetSocketAddress localAddress() throws IOException {
        return (InetSocketAddress) real.localAddress();
    }

    @Override
    public void send(String text) {
        CallbackFuture future = new CallbackFuture();
        real.writeFinalTextFrame(text, future);
        onSend();
    }

    @Override
    public void send(ByteBuffer binary) {
        CallbackFuture future = new CallbackFuture();
        real.writeBinaryMessage(Buffer.buffer(binary.array()), future);
        onSend();
    }

    @Override
    public void close() {
        super.close();
        RunUtil.runAndTry(real::close);
    }
}
