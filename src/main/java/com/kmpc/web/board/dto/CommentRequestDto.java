package com.kmpc.web.board.dto;

import com.kmpc.web.util.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentRequestDto {
    private String content;
    private Long postId;
    private String memberId;
    private Long parentId;

    public String getMemberId() {
        return CommonUtil.getMember().getMemberId();
    }
}