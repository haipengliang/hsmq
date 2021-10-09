package com.hsms.mqserver.strategy;

import com.hsmq.data.HsReq;
import com.hsmq.data.HsResp;
import com.hsmq.enums.OperationEnum;
import com.hsmq.error.HsError;
import com.hsmq.protocol.HsDecodeData;
import com.hsmq.protocol.HsEecodeData;
import com.hsms.mqserver.strategy.executors.BaseExecutor;
import com.hsms.mqserver.strategy.executors.impl.CommitOffsetExecutor;
import com.hsms.mqserver.strategy.executors.impl.PullExecutor;
import com.hsms.mqserver.strategy.executors.impl.SendMessageExecutor;
import com.hsms.mqserver.strategy.executors.impl.TopicDataExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ：河神
 * @date ：Created in 2021/6/9 1:41 下午
 */
public class MessageStrategy {


    private static Map<OperationEnum, BaseExecutor<?>> enumExecutorMap ;

    static {
        enumExecutorMap = new HashMap<>();
        enumExecutorMap.put(OperationEnum.SendMessage,new SendMessageExecutor());
        enumExecutorMap.put(OperationEnum.Pull,new PullExecutor());
        enumExecutorMap.put(OperationEnum.CommitOffset,new CommitOffsetExecutor());
        enumExecutorMap.put(OperationEnum.TopicData,new TopicDataExecutor());
        System.out.println("init MessageStrategy");
    }

    public static HsEecodeData executor(HsDecodeData hsDecodeData){
        HsReq<?> hsReq = (HsReq<?>) hsDecodeData.getData();

        OperationEnum operationEnum = hsReq.getOperationEnum();
        if (operationEnum==null){
            return HsEecodeData.resp(HsError.ParameterWrongType);
        }

        BaseExecutor<?> baseExecutor = enumExecutorMap.get(operationEnum);
        HsResp<?> hsResp = baseExecutor.executor0(hsReq);
        return HsEecodeData.resp(hsResp);
    }


}