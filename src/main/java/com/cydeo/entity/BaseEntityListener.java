package com.cydeo.entity;

import com.cydeo.entity.common.UserPrincipal;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class BaseEntityListener extends AuditingEntityListener {

    //before any persistence action (database)
    @PrePersist
    public void onPrePersist(BaseEntity baseEntity){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.insertDateTime= LocalDateTime.now();
        baseEntity.lastUpdateDateTime=LocalDateTime.now();
//        baseEntity.insertUserId=1L;
//        baseEntity.lastUpdateUserId=1L;

        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.insertUserId = ((UserPrincipal) principal).getId();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }
    }

    @PreUpdate
    //before any update action (database)
    public void onPreUpdate(BaseEntity baseEntity){

        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        baseEntity.lastUpdateDateTime=LocalDateTime.now();
//        baseEntity.lastUpdateUserId=1L;

        if (authentication != null && !authentication.getName().equals("anonymousUser")){
            Object principal = authentication.getPrincipal();
            baseEntity.lastUpdateUserId = ((UserPrincipal) principal).getId();
        }
    }
}
