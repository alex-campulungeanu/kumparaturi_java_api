package com.alex.kumparaturi.model.audit;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class DateAuditCreate<U> {

    @CreatedDate
    private U createDate;

    public U getCreateDate() {
        return createDate;
    }

    public void setCreateDate(U createDate) {
        this.createDate = createDate;
    }
}

