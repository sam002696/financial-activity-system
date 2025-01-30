package com.sadman.financial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sadman.financial.enums.RecordStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final Long serialVersionUID = 1L;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CREATED_AT", updatable = false)
    protected Date createdAt;

    @JsonIgnore
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "UPDATED_AT")
    protected Date updatedAt;

    @Version
    @JsonIgnore
    @Column(name = "RECORD_VERSION")
    private Integer recordVersion;

    @JsonIgnore
    @Enumerated(EnumType.STRING)
    @Column(name = "RECORD_STATUS", length = 10)
    private RecordStatus recordStatus;

//    @JsonIgnore
//    @Column(name = "CREATOR", updatable = false)
//    private Long createdBy;
//
//    @JsonIgnore
//    @Column(name = "UPDATER")
//    private Long updatedBy;

    @PrePersist
    public void prePersist() {
        this.createdAt = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }
}
