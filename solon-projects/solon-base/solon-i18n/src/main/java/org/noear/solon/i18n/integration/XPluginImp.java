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
package org.noear.solon.i18n.integration;

import org.noear.solon.Solon;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.Plugin;
import org.noear.solon.i18n.I18nUtil;
import org.noear.solon.i18n.annotation.I18n;

/**
 * @author noear
 */
public class XPluginImp implements Plugin {
    @Override
    public void start(AppContext context) {
        context.beanInterceptorAdd(I18n.class, I18nInterceptor.instance);

        Solon.app().filter(-980, (ctx, chain) -> {
            //尝试自动完成地区解析
            I18nUtil.getLocaleResolver().getLocale(ctx);
            chain.doFilter(ctx);
        });
    }
}
