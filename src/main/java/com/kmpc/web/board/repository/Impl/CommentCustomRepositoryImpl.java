package com.kmpc.web.board.repository.Impl;

import com.kmpc.web.board.dto.CommentDto;
import com.kmpc.web.board.entity.Comment;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.repository.CommentCustomRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.kmpc.web.board.entity.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentCustomRepositoryImpl implements CommentCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Transactional(readOnly = true)
    public List<CommentDto> getAllCommentsByPost(Post post) {

        List<Comment> commentList = findAllByPost(post);

        List<CommentDto> commentDtoList = new ArrayList<>();
        Map<Long, CommentDto> map = new HashMap<>();

        for (Comment comment : commentList) {
            CommentDto dto = CommentDto.convertCommentToDto(comment);
            map.put(dto.getId(), dto);
            if (comment.getParent() != null) map.get(comment.getParent().getId()).getChildren().add(dto);
            else commentDtoList.add(dto);
        }

        return commentDtoList;
    }

    public List<Comment> findAllByPost(Post post) {
        return jpaQueryFactory
                .selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(post.getId()))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.createAt.asc())
                .fetch();
    }
}
