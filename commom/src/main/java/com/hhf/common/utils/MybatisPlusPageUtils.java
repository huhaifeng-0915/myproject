package com.hhf.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hhf.common.response.pojo.CommonPageRequest;
import com.hhf.common.response.pojo.CommonPageResponse;

import java.util.List;

/**
 * 分页查询工具类
 * @author xman
 */
public class MybatisPlusPageUtils {

    /**
     * 创建spring-data通用分页请求对象PageRequest，页码从0开始
     *
     * @param pageIndex  页码，1表示查第一页
     * @param pageSize   每页记录数
     */
    public static IPage newPageRequest(int pageIndex, int pageSize) {
        return new Page(pageIndex, pageSize);
    }

    /**
     * 创建spring-data通用分页请求对象PageRequest，页码从0开始
     * @param pageDto 自定义分页查询请求对象，页码从1开始
     * @param <T> CommonPageRequest的子类
     * @return
     */
    public static <T extends CommonPageRequest> IPage newPageRequest(T pageDto){
        return newPageRequest(pageDto.getPageIndex(), pageDto.getPageSize());
    };

    /**
     * 将spring-data通用分页对象转换为自定义的CommonPageRequest
     * @param page
     * @return
     */
    public static CommonPageRequest newCommonPageRequest(IPage page){
        return new CommonPageRequest((int)page.getCurrent() , (int)page.getSize());
    }

    /**
     * 将spring-data通用分页对象转换为自定义的CommonPageResponse
     * @param page
     * @param <T>
     * @return
     */
    public static <T> CommonPageResponse<T> newCommonPageResponse(IPage<T> page){
        return new CommonPageResponse<T>(page.getRecords(), newCommonPageRequest(page), page.getTotal());
    }

    /**
     * 创建自定义分页返回对象CommonPageResponse
     * @param pageRequest 查询条件对象
     * @param list 查询结果列表
     * @param total 总记录数
     * @param <R> 查询条件对象类型，为CommonPageRequest的子类
     * @param <T> 返回结果类型
     * @return
     */
    public static <R extends CommonPageRequest, T> CommonPageResponse<T> newCommonPageResponse(R pageRequest, List<T> list, Long total){
        return new CommonPageResponse<T>(list, pageRequest, total);
    }
}
