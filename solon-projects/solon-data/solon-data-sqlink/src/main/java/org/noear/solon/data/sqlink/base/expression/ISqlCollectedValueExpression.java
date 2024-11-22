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

import org.noear.solon.data.sqlink.base.SqLinkConfig;
import org.noear.solon.data.sqlink.base.session.SqlValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 集合值表达式
 *
 * @author kiryu1223
 * @since 3.0
 */
public interface ISqlCollectedValueExpression extends ISqlValueExpression {
    /**
     * 获取集合值
     */
    Collection<?> getCollection();

    /**
     * 设置元素之间的分隔符
     */
    void setDelimiter(String delimiter);

    /**
     * 元素之间的分隔符
     */
    String getDelimiter();

    @Override
    default boolean nouNull() {
        return getCollection() != null;
    }

    @Override
    default ISqlCollectedValueExpression copy(SqLinkConfig config) {
        SqlExpressionFactory factory = config.getSqlExpressionFactory();
        List<Object> newValues = new ArrayList<>(getCollection());
        ISqlCollectedValueExpression value = factory.value(newValues);
        value.setDelimiter(getDelimiter());
        return value;
    }
}
