package org.noear.solon.core.util;

import org.noear.solon.core.InjectGather;
import org.noear.solon.core.VarHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 顺序位工具类
 *
 * @author noear
 * @since 2.5
 */
public class IndexUtil {
    /**
     * 构建生命周期执行顺序位
     */
    public static int buildLifecycleIndex(Class<?> clz) {
        return new IndexBuilder().buildIndex(clz);
    }

    /**
     * 构建变量收集器的检查顺序位
     */
    public static int buildGatherIndex(InjectGather g1, List<InjectGather> gathers) {
        if (g1.isMethod()) {
            Set<Class<?>> clazzStack = new HashSet<>();
            return buildGatherIndex0(g1, gathers, clazzStack);
        } else {
            return g1.index;
        }
    }

    private static int buildGatherIndex0(InjectGather g1, List<InjectGather> gathers, Set<Class<?>> clazzStack) {
        if (g1.index > 0) {
            return g1.index;
        }

        for (VarHolder v1 : g1.getVars()) {
            if (v1.isDone() == false && v1.getDependencyType() != null) {
                if (clazzStack.contains(v1.getDependencyType())) {
                    for (InjectGather tmp : gathers) {
                        if (v1.getDependencyType().isAssignableFrom(tmp.getOutType())) {
                            int index = tmp.index + 1;
                            if (g1.index < index) {
                                g1.index = index;
                            }
                        }
                    }

                    continue;
                } else {
                    clazzStack.add(v1.getDependencyType());
                }

                for (InjectGather tmp : gathers) {
                    if (v1.getDependencyType().isAssignableFrom(tmp.getOutType())) {
                        int index = buildGatherIndex0(tmp, gathers, clazzStack) + 1;

                        if (g1.index < index) {
                            g1.index = index;
                        }
                    }
                }
            }
        }

        return g1.index;
    }
}