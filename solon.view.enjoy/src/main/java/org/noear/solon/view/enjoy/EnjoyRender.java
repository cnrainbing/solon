package org.noear.solon.view.enjoy;

import com.jfinal.template.Directive;
import com.jfinal.template.Engine;
import com.jfinal.template.Template;
import com.jfinal.template.source.ClassPathSourceFactory;
import com.jfinal.template.source.FileSourceFactory;
import org.noear.solon.XApp;
import org.noear.solon.XUtil;
import org.noear.solon.core.XRender;
import org.noear.solon.core.ModelAndView;
import org.noear.solon.core.XContext;

import java.io.File;
import java.io.PrintWriter;
import java.net.URI;

public class EnjoyRender implements XRender {

    private static EnjoyRender _global;
    public static EnjoyRender global(){
        if(_global==null){
            _global = new EnjoyRender();
        }

        return _global;
    }



    Engine engine = Engine.use();

    private String _baseUri ="/WEB-INF/view/";
    //不要要入参，方便后面多视图混用
    //
    public EnjoyRender() {

        String baseUri = XApp.global().prop().get("slon.mvc.view.prefix");

        if(XUtil.isEmpty(baseUri)==false){
            _baseUri = baseUri;
        }


        if (XApp.global().prop().argx().getInt("debug") == 0) {
            initForRuntime();
        }else {
            //添加调试模式
            String dirroot = XUtil.getResource("/").toString().replace("target/classes/", "");
            String dir_str = dirroot + "src/main/resources"+_baseUri;
            File dir = new File(URI.create(dir_str));
            if (!dir.exists()) {
                dir_str = dirroot + "src/main/webapp"+_baseUri;
                dir = new File(URI.create(dir_str));
            }

            try {
                if (dir.exists()) {
                    engine.setDevMode(true);
                    engine.setBaseTemplatePath(dir.getPath());
                    engine.setSourceFactory(new FileSourceFactory());
                }else{
                    initForRuntime();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        XApp.global().onSharedAdd((k,v)->{
            setSharedVariable(k,v);
        });
    }

    private void initForRuntime(){
        try {
            engine.setBaseTemplatePath(_baseUri);
            engine.setSourceFactory(new ClassPathSourceFactory());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void addDirective(String name, Class<? extends Directive> clz){
        try {
            engine.addDirective(name, clz);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void setSharedVariable(String name,Object value) {
        try {
            engine.addSharedObject(name, value);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void render(Object obj, XContext ctx) throws Exception {
        if(obj == null){
            return;
        }

        if (obj instanceof ModelAndView) {
            render_mav((ModelAndView) obj, ctx);
        }else{
            ctx.output(obj.toString());
        }
    }

    public void render_mav(ModelAndView mv, XContext cxt) throws Exception {
        cxt.contentType("text/html;charset=utf-8");

        Template template = engine.getTemplate(mv.view());

        PrintWriter writer = new PrintWriter(cxt.outputStream());
        template.render(mv.model(), writer);
        writer.flush();
    }
}
