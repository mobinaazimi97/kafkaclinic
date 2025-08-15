package com.mftplus.appointment.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@MappedSuperclass
@NoArgsConstructor
@Setter
@Getter
@SuperBuilder
@ToString
public class Base {

    @Version
    @JsonIgnore
    private Long versionId;
    private boolean deleted = false;
}
