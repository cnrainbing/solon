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
package org.noear.solon.data.sqlink.base.expression;

import org.noear.solon.data.sqlink.base.IConfig;

/**
 * 二元运算表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlBinaryExpression extends ISqlExpression {
    ISqlExpression getLeft();

    ISqlExpression getRight();

    SqlOperator getOperator();

    @Override
    default ISqlBinaryExpression copy(IConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        return factory.binary(getOperator(), getLeft().copy(config), getRight().copy(config));
    }
}