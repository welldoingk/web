package com.kmpc.web.common.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodeId implements Serializable{

    private String classCode;
    private String codeNo;
}
