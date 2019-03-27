package com.hhf.myrabbitmq.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProducerDto extends PageDto implements Serializable {
    private static final long serialVersionUID = 1989212063482190633L;
    private String producerId;
    private String businessNo;
    private String exchange;
    private String routingKey;
    private Date   startCreateTime;
    private Date   endCreateTime;
}
