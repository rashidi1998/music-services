package com.example.resourceservice.repository;

import com.example.resourceservice.entity.MetadataRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataRepository extends JpaRepository<MetadataRecord, Long> {
}
