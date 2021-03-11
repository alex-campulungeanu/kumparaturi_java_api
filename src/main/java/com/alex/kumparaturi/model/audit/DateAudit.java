package com.alex.kumparaturi.model.audit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(
        value = {"createDate", "updateDate"},
        allowGetters = true
)
public class DateAudit<U> {
    @CreatedDate
    private U createDate;

    @LastModifiedDate
    private U updateDate;

    public U getCreateDate() {
        return createDate;
    }

    public void setCreateDate(U createDate) {
        this.createDate = createDate;
    }

    public U getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(U updateDate) {
        this.updateDate = updateDate;
    }
}
