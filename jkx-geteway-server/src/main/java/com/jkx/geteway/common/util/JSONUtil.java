package com.jkx.geteway.common.util;


import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;

/**
 * Created by Taoxs on 2017/12/22.
 */
public final class JSONUtil {

    public final static Integer NON_INDEX = -1;

    private JSONUtil() {

    }

    /**
     * Object 转换为 JSON字符串
     */
    public static String obj2Json(Object obj) {
        try {
            return getInstance().writeValueAsString(obj);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * JSON字符串 转换为 Object
     */
    public static final <T> T json2Obj(String json, Class<T> clazz) {
        try {
            return getInstance().readValue(json, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * JSON字符串 转换为 Object
     * 例如:
     * JavaType aa= SingletonHolder.instance.getTypeFactory().constructParametricType(ArrayList.class, Student.class);
     */
    public static <T> T json2Obj(String json, JavaType typeReference) {
        try {
            return getInstance().readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 同上
     *
     * @param collectionClass  数组的类,例如ArrayList.class
     * @param parameterClasses 集合中元素的类例如Student.class
     */
    public static <T> T json2Obj(String json, Class<T> collectionClass, Class<?> parameterClasses) {
        JavaType collectionType = SingletonHolder.instance.getTypeFactory().constructParametricType(collectionClass,
                parameterClasses);
        return json2Obj(json, collectionType);
    }

    private static ObjectMapper getInstance() {
        ObjectMapper instance = SingletonHolder.instance;
        instance.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        instance.configure(FAIL_ON_EMPTY_BEANS, false);
        return instance;
    }

    private static final class SingletonHolder {

        private static final ObjectMapper instance = new ObjectMapper();
    }

    public static int getObjNum(String json) {
        try {
            JsonNode node = getValue(json);
            if (node.isArray()) {
                ArrayNode arrayNode = (ArrayNode) node;
                return arrayNode.size();
            } else {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getObj(int pos, String json) {
        try {
            JsonNode node = getValue(json);
            if (node.isArray()) {
                ArrayNode arrayNode = (ArrayNode) node;
                if (null == arrayNode.get(pos)) {
                    return "";
                }
                return arrayNode.get(pos).toString();
            } else {
                return json;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return json;
        }
    }

    public static String getValue(int pos, String fieldName, String json) {
        JsonNode node = getValueByPosition(pos, fieldName, json);
        if (node.isTextual()) {
            return node.textValue();
        } else {
            if (node.toString().equals("null")) {
                return "";
            } else {
                return node.toString();
            }
        }
    }

    public static String getValue(String fieldName, String json) {
        try {
            JsonNode node = getValue(json);
            if (node.get(fieldName).isTextual()) {
                return node.get(fieldName).textValue();
            } else {
                if (node.get(fieldName).toString().equals("null")) {
                    return "";
                } else {
                    return node.get(fieldName).toString();
                }
            }
        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
            return "";
        }
    }

    public static JsonNode getValueByPosition(int pos, String fieldName, String json) {
        try {
            JsonNode node = getValue(json);
            if (node.isArray() && pos > NON_INDEX.intValue()) {
                JsonNode childNode = node.get(pos);
                return childNode.get(fieldName);
            } else {
                return node.get(fieldName);
            }
        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

    public static JsonNode getValue(String json) {
        ObjectMapper instance = SingletonHolder.instance;
        try {
            return instance.readTree(json);
        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
        }
        return null;
    }

}
