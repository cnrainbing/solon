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
package org.noear.solon.data.sqlink.base.toBean.handler;

import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class UnKnowTypeHandler<T> implements ITypeHandler<T> {
    @Override
    public T getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException {
        if (c.isEnum()) {
            return (T) Enum.valueOf((Class<Enum>) c, resultSet.getString(index));
        }
        else {
            return (T) resultSet.getObject(index, c);
        }
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, T value) throws SQLException {
        if (value == null) {
            preparedStatement.setNull(index, JDBCType.NULL.getVendorTypeNumber());
        }
        else if (value.getClass().isEnum()) {
            preparedStatement.setString(index, value.toString());
        }
        else {
            preparedStatement.setObject(index, value);
        }
    }
}