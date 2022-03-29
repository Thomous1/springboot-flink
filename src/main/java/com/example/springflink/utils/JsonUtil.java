package com.example.springflink.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;


@Slf4j
public class JsonUtil {
    private static ObjectMapper mapper = new ObjectMapper();
    private static TypeFactory TYPE_FACTORY = mapper.getTypeFactory();
    static{
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);//设置序列化配置，为null的属性不加入到json中
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);//兼容单引号 但单引号不属于json标准 不建议使用
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 将对象转换成json字符串,如果转换失败则返回null
     * @param o 需要转换为json的对象
     * @return String 转换后的json字符串
     *
     *
     * */
    public static String write2JsonStr(Object o){
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("write2JsonStr() exception: ",e);
        }
        return jsonStr;
    }

    /**
     * 将json转换为对象 如果对象模版为内部类会出现问题，所以不要使用内部类
     * @param json 要转换的json
     * @param clazz 要映射的类型
     * @return 转换成的目标对象，如果转换失败返回null
     * */
    public static Object json2Object(String json, Class<?> clazz){
        try {
            return mapper.readValue(json,clazz);
        } catch (JsonParseException e) {
            log.error("json2Object() parseException: " ,e);
        } catch (JsonMappingException e) {
            log.error("json2Object() mappingException: ",e);
        } catch (IOException e) {
            log.error("json2Object() IOException: " ,e);
        }
        return null;
    }

    /**
     * 将json字符串转换为Map
     * @param  json 需要转换为Map的json字符串 {}开头结尾的
     * @return 转换后的map 如果转换失败返回空HashMap
     * */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> json2Map(String json){
        try {
            if(StringUtils.isBlank(json)) {
                return new HashMap<String, Object>() ;
            }
            return mapper.readValue(json, Map.class);
        } catch (JsonParseException e) {
            log.error("json2Map(),JsonParseException: ",e);
        } catch (JsonMappingException e) {
            log.error("json2Map(),JsonMappingException: " ,e);
        } catch (IOException e) {
            log.error("json2Map(),IOException: " ,e);
        }
        return new HashMap<String, Object>() ;
    }

    /**
     * 将json数组转换为List<Map<String,Object>> json数组格式[{},{}]
     * @param  jsonArray 需要转换的json数组
     * @return 转换后的列表   如果转换失败返回null
     * */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> jsonArray2List(String jsonArray){
        try {
            return mapper.readValue(jsonArray, List.class);
        } catch (JsonParseException e) {
            log.error("jsonArray2List==>" + jsonArray);
            log.error("jsonArray2List() exception, 异常字符串: ", e);
        } catch (JsonMappingException e) {
            log.error("jsonArray2List==>" + jsonArray);
            log.error("jsonArray2List() exception, 异常字符串: ", e);
        } catch (IOException e) {
            log.error("jsonArray2List==>" + jsonArray);
            log.error("jsonArray2List() exception",e);
        }
        return new ArrayList<Map<String, Object>>();
    }

    public static <T> String jsonObjectList2String(List<T> list) {
        try {
            return mapper.writeValueAsString(list);
        } catch (Exception e) {
           e.printStackTrace();
        }

        return "";
    }

    public static <T> List<T> json2ObjectList(String json, Class<T> clazz) {
        try {
            JavaType t  = TYPE_FACTORY.constructParametricType(List.class, clazz);
            return mapper.readValue(json, t);
        } catch (Exception e) {
            return new ArrayList();
        }
    }

    /**
     * 将json数组转换为List<Map<String,Object>> json数组格式[{},{}]
     * @param  jsonArray 需要转换的json数组
     * @param keyword 关键字
     * @return 转换后的列表   如果转换失败返回null
     * */
    @SuppressWarnings("unchecked")
    public static List<Map<String, Object>> jsonArray2List(String jsonArray, String keyword){
        try {
            return mapper.readValue(jsonArray, List.class);
        } catch (JsonParseException e) {
            log.error("JsonUtil exception, keyword: "+keyword+", 异常字符串: " , e);
        } catch (JsonMappingException e) {
            log.error("JsonUtil exception, keyword: "+keyword+", 异常字符串: ", e);
        } catch (IOException e) {
            log.error("JsonUtil exception",e);
        }
        return new ArrayList<Map<String, Object>>();
    }

    public static Set<?> json2ArraySet(String json, TypeReference<?> tr) {
        try {
            return (Set<?>) mapper.readValue(json, tr);
        } catch (Exception e) {
            log.error("json2ArrayObject, JsonUtil exception", e);
        }
        return null;
    }

    public static int obj2Int(Object obj){
        String str = obj.toString();
        int i = 0;
        try{
            i = Integer.parseInt(str);
        }catch(Exception e){
            log.error("对象转int失败",e);
        }
        return i;
    }
    public static long obj2Long(Object obj){
        String str = obj.toString();
        long l = 0;
        try{
            l = Long.parseLong(str);
        }catch(Exception e){
            log.error("对象转int失败",e);
        }
        return l;
    }
}

