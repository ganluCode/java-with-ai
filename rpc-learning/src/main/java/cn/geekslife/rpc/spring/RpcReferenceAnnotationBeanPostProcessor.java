package cn.geekslife.rpc.spring;

import cn.geekslife.rpc.annotation.RpcReference;
import cn.geekslife.rpc.proxy.RpcServiceReferer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class RpcReferenceAnnotationBeanPostProcessor implements BeanPostProcessor {
    
    @Autowired
    private RpcServiceReferer rpcServiceReferer;
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                // 引用服务
                Object proxy = rpcServiceReferer.referService(field.getType(), rpcReference);
                field.setAccessible(true);
                try {
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Failed to inject service reference", e);
                }
            }
        }
        return bean;
    }
}