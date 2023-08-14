package com.kmpc.web.common.entity;

import jakarta.persistence.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.kmpc.web.util.Timestamped;

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

    private String remark;

    private Long orders;


    @Builder
    public Code(String classCode, String codeNo, String codeName, Long orders){
        this.classCode = classCode;
        this.codeNo = codeNo;
        this.codeName = codeName;
        this.orders = orders;
    }
}
