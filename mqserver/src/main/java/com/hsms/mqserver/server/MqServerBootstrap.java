package com.hsms.mqserver.server;

import com.hsms.mqserver.config.ServerConfig;
import com.hsms.mqserver.data.ConsumerQueueManger;
import com.hsms.mqserver.reactor.ObjectReactor;

/**
 * @author ：河神
 * @date ：Created in 2021/7/24 2:04 下午
 */
public class MqServerBootstrap {

    public Thread worker;

    public void start() {
        //启动注册topic
        ServerConfig.TopicConfig.forEach((topic,config)->{
            ConsumerQueueManger.registerTopic(config);
        });
        //恢复消费组消费序列
        ConsumerQueueManger.recoveryConsumer();
        //启动服务
        worker = new Thread(new ObjectReactor(ServerConfig.Port));
        worker.start();
    }
}