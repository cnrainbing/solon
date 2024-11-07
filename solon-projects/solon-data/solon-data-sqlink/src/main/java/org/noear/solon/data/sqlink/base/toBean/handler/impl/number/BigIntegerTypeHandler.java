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
package org.noear.solon.data.sqlink.base.toBean.handler.impl.number;

import org.noear.solon.data.sqlink.base.toBean.handler.ITypeHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.JDBCType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class BigIntegerTypeHandler implements ITypeHandler<BigInteger> {
    @Override
    public BigInteger getValue(ResultSet resultSet, int index, Class<?> c) throws SQLException {
        BigDecimal decimal = resultSet.getBigDecimal(index);
        return decimal == null ? null : decimal.toBigInteger();
    }

    @Override
    public void setValue(PreparedStatement preparedStatement, int index, BigInteger bigInteger) throws SQLException {
        if (bigInteger == null) {
            preparedStatement.setNull(index, JDBCType.BIGINT.getVendorTypeNumber());
        }
        else {
            preparedStatement.setBigDecimal(index, new BigDecimal(bigInteger));
        }
    }
}