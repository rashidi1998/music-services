package com.example.resourceservice.entity;

import jakarta.persistence.*;

import java.util.Map;

@Entity
@Table(name = "metadata")
public class MetadataRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "resource_id", nullable = false)
    private Long resourceId;

    @ElementCollection
    @CollectionTable(name = "metadata_values", joinColumns = {@JoinColumn(name = "metadata_id")})
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> values;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResourceId() {
        return resourceId;
    }

    public void setResourceId(Long resourceId) {
        this.resourceId = resourceId;
    }

    public Map<String, String> getValues() {
        return values;
    }

    public void setValues(Map<String, String> values) {
        this.values = values;
    }
}
