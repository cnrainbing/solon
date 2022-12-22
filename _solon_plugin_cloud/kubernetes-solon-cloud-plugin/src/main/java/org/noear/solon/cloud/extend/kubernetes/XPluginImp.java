package org.noear.solon.cloud.extend.kubernetes;


import org.noear.solon.Utils;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.cloud.CloudManager;
import org.noear.solon.cloud.CloudProps;
import org.noear.solon.cloud.extend.kubernetes.service.CloudConfigServiceK8sImpl;
import org.noear.solon.core.AopContext;
import org.noear.solon.core.Plugin;

import java.util.Timer;


/**
 * @author noear
 * @since 1.10
 */
public class XPluginImp implements Plugin {
    private Timer clientTimer = new Timer();

    /*
    https://support.huaweicloud.com/sdkreference-cci/cci_09_0004.html
    https://github.com/kubernetes-client/java/wiki/3.-Code-Examples
    */

    @Override
    public void start(AopContext context) {
        CloudProps cloudProps = new CloudProps(context, "kubernetes");

        if (Utils.isEmpty(cloudProps.getServer())) {
            return;
        }

        //1.登记配置服务
        if (cloudProps.getConfigEnable()) {
            CloudConfigServiceK8sImpl serviceImp = new CloudConfigServiceK8sImpl(cloudProps);
            CloudManager.register(serviceImp);

            if (serviceImp.getRefreshInterval() > 0) {
                long interval = serviceImp.getRefreshInterval();
                clientTimer.schedule(serviceImp, interval, interval);
            }

            //1.1.加载配置
            CloudClient.configLoad(cloudProps.getConfigLoad());
        }
    }
}
