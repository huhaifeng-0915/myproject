package com.hhf.myrabbitmq.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 分页dto
 *
 * @author _g5niusx
 */
@Data
public class PageDto implements Serializable {
    private static final long serialVersionUID = 7422884351995814372L;
    private int pageNum;
    private int pageSize;
    private int count;
}
