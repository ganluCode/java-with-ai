package cn.geekslife.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RpcService {
    Class<?> interfaceClass() default void.class;
    String version() default "";
    String group() default "";
}