package com.kmpc.web.common.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kmpc.web.util.Timestamped;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@IdClass(CodeId.class)
public class Code extends Timestamped {
    @Id
    private String classCode;
    @Id
    private String codeNo;
    private String codeName;


    @Builder
    public Code(String classCode, String codeNo, String codeName){
        this.classCode = classCode;
        this.codeNo = codeNo;
        this.codeName = codeName;
    }
}
