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
package org.noear.solon.data.sqlink.core.visitor.methods;

import org.noear.solon.data.sqlink.base.SqLinkConfig;
import org.noear.solon.data.sqlink.base.expression.ISqlExpression;
import org.noear.solon.data.sqlink.base.expression.SqlExpressionFactory;
import org.noear.solon.data.sqlink.base.expression.SqlOperator;

/**
 * 时间相关函数
 *
 * @author kiryu1223
 * @since 3.0
 */
public class TemporalMethods {

    /**
     * 左时间大于右时间表达式
     */
    public static ISqlExpression isAfter(SqLinkConfig config, ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.GT, thiz, that);
    }

    /**
     * 左时间小于右时间表达式
     */
    public static ISqlExpression isBefore(SqLinkConfig config, ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.LT, thiz, that);
    }

    /**
     * 左时间等于右时间表达式
     */
    public static ISqlExpression isEqual(SqLinkConfig config, ISqlExpression thiz, ISqlExpression that) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(SqlOperator.EQ, thiz, that);
    }
}
