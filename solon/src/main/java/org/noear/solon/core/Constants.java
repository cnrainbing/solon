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
package org.noear.solon.core;

/**
 * 内部常量（禁止外部引用）
 *
 * @author noear
 * @since 1.2
 */
public interface Constants {
    String PARM_UNDEFINED_VALUE = "\n\t\t\n\t\t\n\ue000\ue001\ue002\n\t\t\t\t\n";

    //@since: 2.5
    String ATTR_CONTROLLER = "controller";
    String ATTR_MAIN_HANDLER = "mainHandler";
    String ATTR_MAIN_STATUS = "mainStatus";
    String ATTR_ACTION = "action";

    //@since: 3.0
    int FT_IDX_CONTEXT_PATH = -990;
    int FT_IDX_I18N = -980;
    int FT_IDX_CROSS = -970;
}
