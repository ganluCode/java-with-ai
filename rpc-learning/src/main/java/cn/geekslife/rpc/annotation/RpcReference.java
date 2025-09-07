package cn.geekslife.rpc.annotation;

import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Autowired
public @interface RpcReference {
    String version() default "";
    String group() default "";
    String loadbalance() default "random";
    int retries() default 2;
}