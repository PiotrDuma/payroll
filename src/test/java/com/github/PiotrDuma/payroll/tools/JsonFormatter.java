package com.github.PiotrDuma.payroll.tools;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Source: <a href="https://www.baeldung.com/java-json-pretty-print">https://www.baeldung.com/java-json-pretty-print</a>
 *
 * Note: use only raw String to compare JSON response in test methods. Pretty format may have
 * different formatting.
 */

public class JsonFormatter {

    public static String getPrettyJson(String rawString) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(rawString, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
    }

    public static String getRawString(String prettyJson) throws JsonProcessingException{
        ObjectMapper objectMapper = new ObjectMapper();
        Object jsonObject = objectMapper.readValue(prettyJson, Object.class);
        return objectMapper.writeValueAsString(jsonObject);
    }
}
