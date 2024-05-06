package net.c0ffee1.quartz.core.config.nodes;

import com.fasterxml.jackson.databind.JsonNode;

public class JacksonNode implements ConfigNode {
    private final JsonNode jsonNode;

    public JacksonNode(JsonNode jsonNode) {
        this.jsonNode = jsonNode;
    }

    @Override
    public ConfigNode path(String fieldName) {
        return new JacksonNode(jsonNode.path(fieldName));
    }

    @Override
    public String asText() {
        return jsonNode.asText();
    }

    @Override
    public boolean isMissingNode() {
        return jsonNode.isMissingNode();
    }


    public JsonNode getJsonNode() {
        return jsonNode;
    }

    public static ConfigNode of(JsonNode jsonNode){
        return new JacksonNode(jsonNode);
    }
}
