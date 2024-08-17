package org.noear.solon.cloud.gateway.route;

import org.noear.solon.Utils;
import org.noear.solon.core.handle.Context;
import org.noear.solon.core.util.KeyValues;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 上游请求
 *
 * @author noear
 * @since 2.9
 */
public class UpstreamRequest {
    public static final String ATTR_NAME = "cloud-upstream-request";

    public static UpstreamRequest of(Context ctx) {
        return ctx.attr(ATTR_NAME);
    }

    //----------------

    private String method;
    private String queryString;
    private String path;
    private Map<String, KeyValues<String>> headers = new LinkedHashMap<>();
    private String contentType;
    private InputStream body;

    /**
     * 配置方法
     */
    public UpstreamRequest method(String method) {
        this.method = method;
        return this;
    }

    /**
     * 配置路径
     */
    public UpstreamRequest path(String path) {
        this.path = path;
        return this;
    }

    /**
     * 配置查询字符串
     */
    public UpstreamRequest queryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    private KeyValues<String> getHeaderHolder(String key) {
        return headers.computeIfAbsent(key, k -> new KeyValues<>(key));
    }

    /**
     * 配置头
     */
    public UpstreamRequest header(String key, String... values) {
        getHeaderHolder(key).setValues(values);
        return this;
    }

    /**
     * 配置头
     */
    public UpstreamRequest header(String key, List<String> values) {
        getHeaderHolder(key).setValues(values.toArray(new String[values.size()]));
        return this;
    }

    /**
     * 添加头
     */
    public UpstreamRequest headerAdd(String key, String value) {
        getHeaderHolder(key).addValue(value);
        return this;
    }

    /**
     * 配置主体
     */
    public UpstreamRequest body(InputStream body, String contentType) {
        this.body = body;
        this.contentType = contentType;
        return this;
    }

    //----------

    /**
     * 获取方法
     */
    public String getMethod() {
        return method;
    }

    /**
     * 获取查询字符串
     */
    public String getQueryString() {
        return queryString;
    }

    /**
     * 获取路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 获取路径和查询字符串
     */
    public String getPathAndQueryString() {
        if (Utils.isEmpty(queryString)) {
            return path;
        } else {
            return path + "?" + queryString;
        }
    }

    /**
     * 获取头集合
     */
    public Map<String, KeyValues<String>> getHeaders() {
        return headers;
    }

    /**
     * 获取内容类型
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 获取主体
     */
    public InputStream getBody() {
        return body;
    }
}