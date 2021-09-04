package com.test;


import org.apache.ibatis.plugin.*;
import org.junit.Test;

import java.util.Properties;

public class PluginTest {

    @Test
    public void pluginWrapTest(){
        MyPlugin myPlugin= new MyPlugin() {
            @Override
            public String sayHello(String name) {
                System.out.println("hello"+name);
                return name+"hello";
            }

            @Override
            public String sayGood(String name) {
                System.out.println("good"+name);
                return name+"good";
            }
        };
        MyPlugin wrap= (MyPlugin) Plugin.wrap(myPlugin,new MyInterceptor());
        wrap.sayHello("ddd");
    }

    public interface MyPlugin{
        String sayHello(String name);

        String sayGood(String name);
    }

    @Intercepts({@Signature(type = MyPlugin.class,args = {String.class},method="sayHello"),@Signature(type = MyPlugin.class,args = {String.class},method="sayGood")})
    public static class MyInterceptor implements Interceptor {

        @Override
        public Object intercept(Invocation invocation) throws Throwable {
            System.out.println("lydon do:"+invocation.getMethod().getName());
            return invocation.proceed();
        }

        @Override
        public Object plugin(Object target) {
            return null;
        }

        @Override
        public void setProperties(Properties properties) {

        }
    }
}
