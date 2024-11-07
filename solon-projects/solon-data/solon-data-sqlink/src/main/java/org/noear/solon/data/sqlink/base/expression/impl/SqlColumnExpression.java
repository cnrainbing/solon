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
package org.noear.solon.data.sqlink.base.expression.impl;

import org.noear.solon.data.sqlink.base.IConfig;
import org.noear.solon.data.sqlink.base.IDialect;
import org.noear.solon.data.sqlink.base.expression.ISqlColumnExpression;
import org.noear.solon.data.sqlink.base.metaData.PropertyMetaData;

import java.util.List;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class SqlColumnExpression implements ISqlColumnExpression {
    private final PropertyMetaData propertyMetaData;
    private final int tableIndex;

    public SqlColumnExpression(PropertyMetaData propertyMetaData, int tableIndex) {
        this.propertyMetaData = propertyMetaData;
        this.tableIndex = tableIndex;
    }

    @Override
    public PropertyMetaData getPropertyMetaData() {
        return propertyMetaData;
    }

    @Override
    public int getTableIndex() {
        return tableIndex;
    }

    @Override
    public String getSqlAndValue(IConfig config, List<Object> values) {
        IDialect dbConfig = config.getDisambiguation();
        String t = "t" + getTableIndex();
        return dbConfig.disambiguation(t) + "." + dbConfig.disambiguation(getPropertyMetaData().getColumn());
    }
}