package com.xiaoju.uemc.turbo.core.executor;

import com.xiaoju.uemc.turbo.core.bo.NodeInstanceBO;
import com.xiaoju.uemc.turbo.core.common.Constants;
import com.xiaoju.uemc.turbo.core.common.ErrorEnum;
import com.xiaoju.uemc.turbo.core.common.NodeInstanceStatus;
import com.xiaoju.uemc.turbo.core.common.RuntimeContext;
import com.xiaoju.uemc.turbo.core.exception.ProcessException;
import com.xiaoju.uemc.turbo.core.model.FlowElement;
import com.xiaoju.uemc.turbo.core.util.FlowModelUtil;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

/**
 * Created by Stefanie on 2019/12/1.
 */
@Service
public class EndEventExecutor extends ElementExecutor {

    @Override
    protected void postExecute(RuntimeContext runtimeContext) throws Exception {
        NodeInstanceBO currentNodeInstance = runtimeContext.getCurrentNodeInstance();
        currentNodeInstance.setInstanceDataId(runtimeContext.getInstanceDataId());
        currentNodeInstance.setStatus(NodeInstanceStatus.COMPLETED);
        runtimeContext.getNodeInstanceList().add(currentNodeInstance);
    }

    @Override
    protected void doRollback(RuntimeContext runtimeContext) throws Exception {
        FlowElement flowElement = runtimeContext.getCurrentNodeModel();
        String nodeName = FlowModelUtil.getElementName(flowElement);
        LOGGER.warn("doRollback: unsupported element type as EndEvent.||flowInstanceId={}||nodeKey={}||nodeName={}||nodeType={}",
                runtimeContext.getFlowInstanceId(), flowElement.getKey(), nodeName, flowElement.getType());
        throw new ProcessException(ErrorEnum.UNSUPPORTED_ELEMENT_TYPE,
                MessageFormat.format(Constants.NODE_INFO_FORMAT, flowElement.getKey(), nodeName, flowElement.getType()));
    }

    @Override
    protected void postRollback(RuntimeContext runtimeContext) throws Exception {
        //do nothing
    }

    @Override
    protected RuntimeExecutor getExecuteExecutor(RuntimeContext runtimeContext) throws Exception {
        LOGGER.info("getExecuteExecutor: no executor after EndEvent.");
        return null;
    }
}
