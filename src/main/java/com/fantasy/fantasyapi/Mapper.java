package com.fantasy.fantasyapi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Mapper 
{
    private ObjectMapper objectMapper = getDefaultObjectMapper();

    private ObjectMapper getDefaultObjectMapper()
    {
        ObjectMapper defaultObjectMapper = new ObjectMapper();
        return defaultObjectMapper;
    }

    public JsonNode parse(String json) throws JsonMappingException, JsonProcessingException
    {
        return objectMapper.readTree(json);
    }

    public void hold()
    {
        String jsonSource = "{ \"title\": \"Coder from Scratch\" }";
                Mapper mapper = new Mapper();
                try
                {
                    JsonNode node = mapper.parse(jsonSource);
                    System.out.println(node.get("title").asText());
                }
                catch (JsonProcessingException e) 
                {
                    e.printStackTrace();
                }
    }
        
}
