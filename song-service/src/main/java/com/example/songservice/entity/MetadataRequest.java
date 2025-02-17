package com.example.songservice.entity;

import java.util.Map;

public class MetadataRequest {
    private Long resourceId;
    private Map<String, String> metadata;

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
