package com.alex.kumparaturi.model.audit;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DateAuditUpdate<U> {

    @LastModifiedDate
    private U updateDate;

    public U getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(U updateDate) {
        this.updateDate = updateDate;
    }
}
