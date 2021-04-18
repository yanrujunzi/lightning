package com.uni.util;

import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 在这里编写说明
 *
 * @author: CL
 * @email: caolu@sunseaaiot.com
 * @date: 2019-03-28 13:31:00
 */
public class Page<T> implements Serializable {
    /**
     * 页码
     */
    @Getter
    @Setter
    private int pageNo;
    /**
     * 每页大小
     */
    @Getter
    @Setter
    private int pageSize;

    /**
     * 总量
     */
    @Getter
    @Setter
    private int totalCount;
    /**
     * 结果
     */
    @Getter
    @Setter
    private List<T> result;

    /**
     * 总共多少页
     */
    @Getter
    private int totalPage;


    public Page(int pageNo, int pageSize, int totalCount, List<T> result) {
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        this.result = result;

        //设置只有get方法的参数值
        setGetParams();

    }

    /**
     * 设置只有get方法的值
     */
    private void setGetParams() {
        if(pageSize==0){
            this.totalPage=0;
        }else{
            this.totalPage = (this.totalCount % this.pageSize) == 0
                    ?
                    (this.totalCount / this.pageSize)
                    :
                    (this.totalCount / this.pageSize + 1);
        }
    }




    public static void main(String[] args) {
        String s = new Gson().toJson(new Page(10, 10, 100, new ArrayList()));
        System.out.println(s);
    }


}
