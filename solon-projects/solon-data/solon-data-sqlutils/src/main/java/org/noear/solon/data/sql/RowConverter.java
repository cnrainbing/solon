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
package org.noear.solon.data.sql;

import org.noear.snack.ONode;

import java.sql.SQLException;

/**
 * 行转换器
 *
 * @author noear
 * @since 3.0
 * */
@FunctionalInterface
public interface RowConverter {
    //默认转换器
    static RowConverter DEFAUlT = (r, c) -> ONode.load(r.toMap()).toObject(c);

    /**
     * 转换
     *
     * @param row  行
     * @param type 类型
     */
    Object convert(Row row, Class<?> type) throws SQLException;
}
