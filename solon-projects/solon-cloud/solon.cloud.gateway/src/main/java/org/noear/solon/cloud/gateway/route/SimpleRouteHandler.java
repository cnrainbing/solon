package org.noear.solon.cloud.gateway.route;

import org.noear.solon.core.exception.StatusException;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.KeyValues;
import org.noear.solon.net.http.HttpUtils;
import reactor.core.publisher.Mono;

import java.net.URI;

/**
 * 简单的分布式路由处理
 *
 * @author noear
 * @since 2.9
 */
public class SimpleRouteHandler implements RouteHandler {
    /**
     * 处理
     */
    @Override
    public Mono<Void> handle(Context ctx) {
        Route route = Route.of(ctx);
        UpstreamRequest request = UpstreamRequest.of(ctx);

        URI uri = route.getUri();

        //构建请求工具
        HttpUtils httpUtils;
        if ("lb".equals(uri.getScheme())) {
            httpUtils = HttpUtils.http(uri.getHost(), request.getPathAndQueryString());
        } else {
            String url = uri + request.getPathAndQueryString();
            httpUtils = HttpUtils.http(url);
        }


        try {
            //同步 header
            for (KeyValues<String> kv : request.getHeaders().values()) {
                for (String val : kv.getValues()) {
                    httpUtils.headerAdd(kv.getKey(), val);
                }
            }

            //同步 body（流复制）
            httpUtils.bodyRaw(request.getBody(), request.getContentType());

            return Mono.create(monoSink -> {
                //异步 执行
                httpUtils.execAsync(request.getMethod(), (isSuccessful, resp, error) -> {
                    try {
                        if (resp != null) {
                            //code
                            ctx.status(resp.code());
                            //contentType
                            ctx.contentType(resp.contentType());
                            //header
                            for (String name : resp.headerNames()) {
                                for (String v : resp.headers(name)) {
                                    ctx.headerAdd(name, v);
                                }
                            }
                            //body 输出（流复制）
                            ctx.output(resp.body());
                            monoSink.success();
                        } else {
                            monoSink.error(error);
                        }
                    } catch (Throwable ex) {
                        monoSink.error(ex);
                    }
                });
            });
        } catch (Throwable ex) {
            //如查出错，说明客户端发的数据有问题
            return Mono.error(new StatusException(ex, 400));
        }
    }
}