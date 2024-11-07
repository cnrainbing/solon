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
package org.noear.solon.data.sqlink.base.session;

import org.noear.solon.data.sqlink.base.toBean.handler.ITypeHandler;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.noear.solon.data.sqlink.core.visitor.ExpressionUtil.cast;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlValue {
    private final Object value;
    private final ITypeHandler<?> typeHandler;

    public SqlValue(Object value, ITypeHandler<?> typeHandler) {
        this.value = value;
        this.typeHandler = typeHandler;
    }

    public void preparedStatementSetValue(PreparedStatement preparedStatement, int index) throws SQLException {
        typeHandler.setValue(preparedStatement, index, cast(value));
    }
}