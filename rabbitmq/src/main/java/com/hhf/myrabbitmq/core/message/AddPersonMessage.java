package com.hhf.myrabbitmq.core.message;

import com.hhf.demo.model.Person;
import com.hhf.myrabbitmq.core.QueueMessage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huhaifeng
 */
@Data
@ApiModel
@EqualsAndHashCode(callSuper = false)
public class AddPersonMessage extends QueueMessage {

    private static final long serialVersionUID = 1578885989731325943L;
    @ApiModelProperty(value = "用户信息")
    Person person;

}
