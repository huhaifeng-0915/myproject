package com.hhf.myrabbitmq.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
public class ConsumerDto extends PageDto implements Serializable {
    private static final long serialVersionUID = 1668143549419760760L;
    private String  consumerId;
    private String  producerId;
    private String  queue;
    private Integer consumerTimes;
    private Date    startCreateTime;
    private Date    endCreateTime;
}
