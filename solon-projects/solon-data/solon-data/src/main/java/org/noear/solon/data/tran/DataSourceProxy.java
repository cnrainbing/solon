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
package org.noear.solon.data.tran;

import org.noear.solon.data.datasource.DataSourceWrapper;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * 数据源代理（用于事务控制）
 *
 * @author noear
 * @since 2.7
 */
public class DataSourceProxy extends DataSourceWrapper {
    public DataSourceProxy(DataSource real) {
        super(real);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return TranUtils.getConnectionProxy(getReal());
    }
}