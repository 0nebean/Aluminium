package net.onebean.aluminium.common.dictionary;

import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;


/**
 * @Auther 0neBean
 * spring 初始化资源
 */
@Component("BeanDefineConfigue")
public class BeanDefineConfigue implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private DictionaryUtils dicDictionaryUtils;


    private final static Logger logger = (Logger) LoggerFactory.getLogger(BeanDefineConfigue.class);

    //当一个ApplicationContext被初始化或刷新触发 加载字典到内存中
    public void onApplicationEvent(ContextRefreshedEvent event) {
        logger.info("start init custom spring config");
        dicDictionaryUtils.init();
        logger.info("init custom spring config done");
    }



}