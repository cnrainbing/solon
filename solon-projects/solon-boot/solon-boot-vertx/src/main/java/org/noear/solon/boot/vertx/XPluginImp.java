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
package org.noear.solon.boot.vertx;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.boot.ServerConstants;
import org.noear.solon.boot.ServerProps;
import org.noear.solon.boot.prop.impl.HttpServerProps;
import org.noear.solon.boot.prop.impl.WebSocketServerProps;
import org.noear.solon.core.*;
import org.noear.solon.core.event.EventBus;
import org.noear.solon.core.util.LogUtil;
import org.noear.solon.core.util.ThreadsUtil;

/**
 * @author noear
 * @since 2.9
 */
public class XPluginImp implements Plugin {
    private static Signal _signal;

    public static Signal signal() {
        return _signal;
    }

    public static String solon_boot_ver() {
        return "vertx-http/" + Solon.version();
    }

    private Vertx _vertx;
    private VxHttpServerComb _server;

    @Override
    public void start(AppContext context) throws Throwable {
        if (Solon.app().enableHttp() == false) {
            return;
        }

        VertxOptions vertxOptions = new VertxOptions();
        //vertxOptions.setWorkerPoolSize(20); //暂时默认
        //vertxOptions.setEventLoopPoolSize(2 * Runtime.getRuntime().availableProcessors());
        //vertxOptions.setInternalBlockingPoolSize(20);

        //添加总线扩展
        EventBus.publish(vertxOptions);

        _vertx = Vertx.vertx(vertxOptions);
        context.wrapAndPut(Vertx.class, _vertx);

        context.lifecycle(ServerConstants.SIGNAL_LIFECYCLE_INDEX, () -> {
            start0(Solon.app());
        });
    }

    private void start0(SolonApp app) throws Throwable {
        //初始化属性
        ServerProps.init();

        HttpServerProps props = HttpServerProps.getInstance();
        final String _host = props.getHost();
        final int _port = props.getPort();
        final String _name = props.getName();


        long time_start = System.currentTimeMillis();

        _server = new VxHttpServerComb();
        _server.enableWebSocket(app.enableWebSocket());
        if (props.isIoBound()) {
            //如果是io密集型的，加二段线程池
            if(Solon.cfg().isEnabledVirtualThreads()){
                _server.setExecutor(ThreadsUtil.newVirtualThreadPerTaskExecutor());
            }else{
                _server.setExecutor(props.getBioExecutor("smarthttp-"));
            }
        }

        if (props.isIoBound()) {
            //如果是io密集型的，加二段线程池
            if (Solon.cfg().isEnabledVirtualThreads()) {
                _server.setExecutor(ThreadsUtil.newVirtualThreadPerTaskExecutor());
            } else {
                _server.setExecutor(props.getBioExecutor("vertxhttp-"));
            }
        }

        _server.setHandler(Solon.app()::tryHandle);


        //尝试事件扩展
        EventBus.publish(_server);
        _server.start(_host, _port);


        final String _wrapHost = props.getWrapHost();
        final int _wrapPort = props.getWrapPort();
        _signal = new SignalSim(_name, _wrapHost, _wrapPort, "http", SignalType.HTTP);
        app.signalAdd(_signal);

        long time_end = System.currentTimeMillis();

        String connectorInfo = "solon.connector:main: vertx-http: Started ServerConnector@{HTTP/1.1,[http/1.1]";

        if (app.enableWebSocket()) {
            //有名字定义时，添加信号注册
            WebSocketServerProps wsProps = WebSocketServerProps.getInstance();
            if (Utils.isNotEmpty(wsProps.getName())) {
                SignalSim wsSignal = new SignalSim(wsProps.getName(), _wrapHost, _wrapPort, "ws", SignalType.WEBSOCKET);
                app.signalAdd(wsSignal);
            }

            String wsServerUrl = props.buildWsServerUrl(_server.isSecure());
            LogUtil.global().info(connectorInfo + "[WebSocket]}{" + wsServerUrl + "}");
        }

        String httpServerUrl = props.buildHttpServerUrl(_server.isSecure());
        LogUtil.global().info(connectorInfo + "}{"+ httpServerUrl +"}");
        LogUtil.global().info("Server:main: vertx-http: Started (" + solon_boot_ver() + ") @" + (time_end - time_start) + "ms");
    }

    @Override
    public void stop() throws Throwable {
        if (_server != null) {
            _server.stop();
        }

        if (_vertx != null) {
            _vertx.close();
            _vertx = null;
        }
    }
}
