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
package libs.gateway1;

import org.noear.solon.annotation.Component;
import org.noear.solon.core.handle.Context;
import org.noear.solon.web.reactive.RxFilter;
import org.noear.solon.web.reactive.RxFilterChain;
import reactor.core.publisher.Mono;

@Component
public class RxFilterImpl implements RxFilter {
    @Override
    public Mono<Void> doFilter(Context ctx, RxFilterChain chain) {
        System.out.println("RxFilterImpl.doFilter...............");

        return chain.doFilter(ctx);
    }
}
