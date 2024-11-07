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
package org.noear.solon.data.sqlink.core.sqlExt.pgsql;

import org.noear.solon.data.sqlink.base.IConfig;
import org.noear.solon.data.sqlink.base.expression.ISqlExpression;
import org.noear.solon.data.sqlink.base.expression.ISqlTypeExpression;
import org.noear.solon.data.sqlink.base.sqlExt.BaseSqlExtension;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.noear.solon.data.sqlink.core.visitor.ExpressionUtil.*;

/**
 * @author kiryu1223
 * @since 3.0
 */
public class PostgreSQLCastExtension extends BaseSqlExtension {
    @Override
    public ISqlExpression parse(IConfig config, Method sqlFunc, List<ISqlExpression> args) {
        List<String> templates = new ArrayList<>();
        List<ISqlExpression> sqlExpressions = new ArrayList<>();
        ISqlExpression expression = args.get(1);
        ISqlTypeExpression typeExpression = (ISqlTypeExpression) expression;
        Class<?> type = typeExpression.getType();
        templates.add("");
        sqlExpressions.add(args.get(0));
        String unit;
        if (isByte(type) || isShort(type)) {
            unit = "INT2";
        }
        else if (isInt(type)) {
            unit = "INT4";
        }
        else if (isLong(type)) {
            unit = "INT8";
        }
        else if (isFloat(type)) {
            unit = "FLOAT4";
        }
        else if (isDouble(type)) {
            unit = "FLOAT8";
        }
        else if (isDecimal(type)) {
            unit = "NUMERIC";
        }
        else if (isChar(type)) {
            unit = "CHAR";
        }
        else if (isString(type)) {
            unit = "VARCHAR";
        }
        else if (isTime(type)) {
            unit = "TIME";
        }
        else if (isDate(type)) {
            unit = "DATE";
        }
        else if (isDateTime(type)) {
            unit = "TIMESTAMP";
        }
        else {
            throw new UnsupportedOperationException("不支持的Java类型:" + type.getName());
        }
        templates.add("::" + unit);
        return config.getSqlExpressionFactory().template(templates, sqlExpressions);
    }
}