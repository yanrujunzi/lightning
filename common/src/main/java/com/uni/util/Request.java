package com.uni.util;

import com.alibaba.fastjson.JSON;
import lombok.Getter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class Request {
    @Getter
    private Map<String, Object> params;
    @Getter
    private Integer pageNo = 1;
    @Getter
    private Integer pageSize = 10;

    private Request() {

    }

    private Request(Map<String, Object> params) {
        this.params = params;
    }

    public static Request configParams(Map<String, Object> params) {
        Request request = new Request(params);
        if (params.containsKey("pageNo")
                && params.containsKey("pageSize")
        ) {
            request.pageNo = Integer.valueOf(params.get("pageNo") + "");
            request.pageSize = Integer.valueOf(params.get("pageSize") + "");
        }


        params.put("pageNo", request.pageNo);
        params.put("pageSize",  request.pageSize);

        params.put("offset", (request.pageNo - 1) * request.pageSize);
        params.put("limit",  request.pageSize);
        return request;
    }

    /**
     * 手动配置页码
     * @param pageNo
     * @param pageSize
     */
    public void configPage(int pageNo, int pageSize) {
        params.put("offset", (pageNo - 1) * pageSize);
        params.put("limit",  pageSize);
    }
    public void put(String key, Object value) {
        params.put(key, value);
    }

    public String getString(String key) {
        if (params == null || params.get(key) == null) {
            return null;
        }
        Object value = params.get(key);
        if (value instanceof String) {
            return (String) params.get(key);
        }
        return String.valueOf(value);

    }
    public Boolean getBoolean(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Boolean.valueOf(value);
    }
    public Byte getByte(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Byte.valueOf(value);
    }
    public Short getShort(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Short.valueOf(value);
    }
    public Integer getInteger(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Integer.valueOf(value);
    }
    public Long getLong(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Long.valueOf(value);
    }
    public Float getFloat(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Float.valueOf(value);
    }
    public Double getDouble(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return Double.valueOf(value);
    }
    public BigDecimal getDecimal(String key) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        return new BigDecimal(value);
    }
    public Date getDate(String key, String pattern) {
        String value = getString(key);
        if (value ==null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).parse(value);
        } catch (ParseException e) {
            throw new RuntimeException("前端请求时间类型转换错误", e);
        }
    }

    public boolean isNotBlank(String key) {
        return StringUtil.isNotBlank(this.getString(key));
    }
    public boolean isBlank(String key) {
        return !isNotBlank(key);
    }
    public <T> T convertObj(Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(clazz), clazz);
    }
    public Request like(String key) {
        if (StringUtil.isNotBlank((String) this.params.get(key))) {
            this.params.put(key, "%" + this.params.get(key) + "%");
        }
        return this;
    }


}
