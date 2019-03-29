package com.hhf.myrabbitmq.core.message;

import com.hhf.myrabbitmq.core.QueueMessage;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huhaifeng
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class MyTestMessage extends QueueMessage {
    private static final long serialVersionUID = 1578885989731325943L;
    private String userName;
}
