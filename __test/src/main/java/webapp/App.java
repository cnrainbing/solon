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
package webapp;

import cn.dev33.satoken.SaManager;
import freemarker.template.Configuration;
import org.noear.solon.Solon;
import org.noear.solon.SolonApp;
import org.noear.solon.Utils;
import org.noear.solon.annotation.Component;
import org.noear.solon.annotation.Import;
import org.noear.solon.annotation.Inject;
import org.noear.solon.annotation.SolonMain;
import org.noear.solon.boot.http.HttpServerConfigure;
import org.noear.solon.cloud.CloudClient;
import org.noear.solon.core.AppContext;
import org.noear.solon.core.ExtendLoader;
import org.noear.solon.core.event.AppInitEndEvent;
import org.noear.solon.core.handle.MethodType;
import org.noear.solon.scheduling.annotation.EnableAsync;
import org.noear.solon.scheduling.annotation.EnableRetry;
import org.noear.solon.serialization.properties.PropertiesActionExecutor;
import org.noear.solon.web.cors.CrossHandler;
import org.noear.solon.web.staticfiles.StaticMappings;
import org.noear.solon.web.staticfiles.repository.ClassPathStaticRepository;
import org.noear.solon.web.staticfiles.repository.ExtendStaticRepository;
import org.noear.solon.web.staticfiles.repository.FileStaticRepository;
import org.noear.solon.serialization.JsonRenderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webapp.demo6_aop.TestImport;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Properties;

@Component
@EnableAsync
@EnableRetry
@Import(value = TestImport.class, scanPackages = "webapp")
//@EnableCron4j
//@EnableQuartz
//@EnableScheduling
@SolonMain
public class App {

    static Logger logger = LoggerFactory.getLogger(App.class);

    @Inject
    AppContext appContext;

    public static void main(String[] args) throws Exception {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("resource[/]=" + App.class.getResource("/").getPath());
        System.out.println("resource[]=" + App.class.getResource("").getPath());

        //简化方式
        //SolonApp app = Solon.start(TestApp.class, args, x -> x.enableSocketD(true).enableWebSocket(true));


        if(Solon.app() != null){
            return;
        }

        Locale.setDefault(Locale.SIMPLIFIED_CHINESE);

        //构建方式

        Solon.start(App.class, args, x -> {
            System.out.println("user.dir=" + System.getProperty("user.dir"));
            System.out.println("app.dir=" + Utils.appFolder());


            x.enableSocketD(true);
            x.enableWebSocket(true);

            //x.converterManager().register(new CatTypeConverter());

            //x.onStatus(404, c->c.render("404了"));

            x.factoryManager().threadLocalFactory((applyFor, inheritance0)->{
                if(inheritance0) {
                    return new InheritableThreadLocal();
                }else {
                    return new ThreadLocal();
                }
            });

            x.onEvent(JsonRenderFactory.class, e->{
               System.out.println("JsonRenderFactory event: xxxxx: " + e.getClass().getSimpleName());
            });

            x.onEvent(PropertiesActionExecutor.class, e->{
                e.allowPostForm(true);
            });

            x.onEvent(HttpServerConfigure.class, e->{
                //e.enableDebug(true);
            });

            x.onEvent(AppInitEndEvent.class, e->{
                StaticMappings.add("/", new ExtendStaticRepository());
            });

            x.onEvent(Configuration.class, e -> {
                System.out.println("%%%%%%%%%%%%%%%%%%");
                e.setSetting("classic_compatible", "true");
            });

            StaticMappings.add("/file-a/", new ClassPathStaticRepository("static_test"));
            StaticMappings.add("/ext/", new ExtendStaticRepository());
            StaticMappings.add("/sa-token/",new FileStaticRepository("/Users/noear/Downloads/"));
        });

        initApp(Solon.app());
    }

    static void initApp(SolonApp app){


        SaManager.getConfig();

        //NamiAttachment.put("lang","en_US");

        //extend: /Users/noear/WORK/work_github/noear/solon/_test/target/app_ext/
        //System.out.println("extend: " + ExtendLoader.path()+"static");
        System.out.println("extend: " + ExtendLoader.folder());

        System.out.println("testname : " + Solon.cfg().get("testname"));


        System.out.println("生在ID = " + CloudClient.id().generate());

        Properties testP = Utils.loadProperties("test.properties");
        if(testP == null){

        }

//        app.filter((ctx, chain)->{
//            System.out.println("我是过滤器!!!path="+ctx.path());
//            chain.doFilter(ctx);
//        });


//        app.ws("/demof/websocket/{id}",(session,message)->{
//            System.out.println(session.uri());
//            System.out.println("WebSocket-PathVar:Id: " + session.param("id"));
//        });


//        app.ws("/demof/websocket/{id}",(session,message)->{
//            System.out.println(session.uri());
//
//            if(Solon.cfg().isDebugMode()){
//                return;
//            }
//
//            if (session.method() == XMethod.WEBSOCKET) {
//                message.setHandled(true);
//
//                session.getOpenSessions().forEach(s -> {
//                    s.send(message.toString());
//                });
//            } else {
//                System.out.println("X我收到了::" + message.toString());
//                //session.send("X我收到了::" + message.toString());
//            }
//        });

        //预热测试
        //PreheatUtils.preheat("/demo1/run0/");

        logger.debug("测试");


        //socket server
        app.socketd("/seb/test", (c) -> {
            String msg = c.body();
            c.output("收到了...:" + msg);
        });
    }

    void test1() {
        System.setProperty("file.encoding","utf-8");

        //控制渲染的示例 //即拦截执行结果的机制
        //
        SolonApp app = Solon.start(App.class, null);

        //开始之前把上下文置为已泻染
        app.before("/user/**", MethodType.HTTP, c -> c.setRendered(true));

        app.after("/user/**", MethodType.HTTP, c -> {
            //可对 c.result 进行处理 //并输出
        });

        app.after(c -> {
            if (c.getHandled() == false || c.status() == 404) {
                //处理404问题
            }
        });

        //全局添加跨域处理
        app.before(new CrossHandler().allowedOrigins("*"));
    }

    private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());

        String enc = writer.getEncoding();

        return enc;

    }
}
