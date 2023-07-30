package com.kmpc.web.board.repository.Impl;

import java.util.List;


import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.dto.QPostDto;
import com.kmpc.web.file.dto.QPostFileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kmpc.web.board.repository.CustomPostRepository;
import com.kmpc.web.file.dto.PostFileDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.kmpc.web.board.entity.QPost.post;
import static com.kmpc.web.file.entity.QPostFile.postFile;
import static com.kmpc.web.member.entity.QMember.member;


@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements CustomPostRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostDto> selectPostList(String searchVal, Pageable pageable) {
        List<PostDto> content = getPostMemberDtos(searchVal, pageable);
        Long count = getCount(searchVal);
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<PostFileDto> selectPostFileDetail(Long boardId) {
        List<PostFileDto> content = jpaQueryFactory
                .select(new QPostFileDto(
                         postFile.id
                        ,postFile.file.id
                        ,postFile.file.originFileName
                        ,postFile.file.size
                        ,postFile.file.extension
                        ,postFile.file.uploadDir
                ))
                .from(postFile)
                .leftJoin(postFile.file)
                .where(postFile.postId.eq(boardId))
                .where(postFile.delYn.eq("N"))
                .fetch();
        return content;
    }

    private Long getCount(String searchVal) {
        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                // .leftJoin(board.member, member) //검색조건 최적화
                .fetchOne();
        return count;
    }

    private List<PostDto> getPostMemberDtos(String searchVal, Pageable pageable) {
        List<PostDto> content = jpaQueryFactory
                .select(new QPostDto(
                        post.id, post.title, post.content, post.createAt, post.modifiedAt, post.viewCount,
                        member.memberName))
                .from(post)
                .leftJoin(post.member, member)
                .where(containsSearch(searchVal))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    private BooleanExpression containsSearch(String searchVal) {
        return searchVal != null ? post.title.contains(searchVal) : null;
    }
}