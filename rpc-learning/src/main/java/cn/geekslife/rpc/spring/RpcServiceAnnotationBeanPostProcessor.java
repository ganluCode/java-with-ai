package cn.geekslife.rpc.spring;

import cn.geekslife.rpc.annotation.RpcService;
import cn.geekslife.rpc.protocol.RpcServiceExporter;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class RpcServiceAnnotationBeanPostProcessor implements BeanPostProcessor {
    
    @Autowired
    private RpcServiceExporter rpcServiceExporter;
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> clazz = bean.getClass();
        RpcService rpcService = clazz.getAnnotation(RpcService.class);
        if (rpcService != null) {
            // 导出服务
            rpcServiceExporter.exportService(bean, rpcService);
        }
        return bean;
    }
}