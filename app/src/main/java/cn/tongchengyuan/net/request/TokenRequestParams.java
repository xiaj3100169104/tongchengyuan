package cn.tongchengyuan.net.request;

import java.util.HashMap;
import java.util.Map;

/**
 *带token的请求对象，token里面包含了userId
 */
public class TokenRequestParams {
    public Map<String, Object> map;

    public TokenRequestParams() {
        map = new HashMap<>();
    }

    public void addParameter(String key, Object value) {
        map.put(key, value);
    }
}
