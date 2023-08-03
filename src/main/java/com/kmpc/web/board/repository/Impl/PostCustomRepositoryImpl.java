package com.kmpc.web.board.repository.Impl;

import java.util.List;
import java.util.Map;


import com.kmpc.web.board.dto.MtPostDto;
import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.board.dto.QMtPostDto;
import com.kmpc.web.board.dto.QPostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kmpc.web.board.repository.PostCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.kmpc.web.board.entity.QPost.post;
import static com.kmpc.web.board.entity.QPostImage.postImage;
import static com.kmpc.web.member.entity.QMember.member;


@Repository
@RequiredArgsConstructor
public class PostCustomRepositoryImpl implements PostCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<PostDto> selectPostList(String searchVal, Pageable pageable, Long boardId) {
        List<PostDto> content = getPostMemberDtos(searchVal, pageable, boardId);
        Long count = getCount(boardId);
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public Page<MtPostDto> selectGalleryList(Map<String,String> map, Pageable pageable, Long boardId) {
        List<MtPostDto> content = getPostGalleryDtos(map, pageable, boardId);
        Long count = getCount(boardId);
        return new PageImpl<>(content, pageable, count);
    }

//    @Override
//    public List<PostFileDto> selectPostFileDetail(Long boardId) {
//        List<PostFileDto> content = jpaQueryFactory
//                .select(new QPostFileDto(
//                         postFile.id
//                        ,postFile.file.id
//                        ,postFile.file.originFileName
//                        ,postFile.file.size
//                        ,postFile.file.extension
//                        ,postFile.file.uploadDir
//                ))
//                .from(postFile)
//                .leftJoin(postFile.file)
//                .where(postFile.postId.eq(boardId))
//                .where(postFile.delYn.eq("N"))
//                .fetch();
//        return content;
//    }

    private Long getCount(Long BoardId) {
        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                // .leftJoin(board.member, member) //검색조건 최적화
                .fetchOne();
        return count;
    }

    private List<PostDto> getPostMemberDtos(String searchVal, Pageable pageable, Long boardId) {
        List<PostDto> content = jpaQueryFactory
                .select(new QPostDto(
                        post.id,
                        post.title,
                        post.content,
                        post.createAt,
                        post.modifiedAt,
                        post.viewCount,
                        member.memberName,
                        post.boardId,
                        post.gbVal
                ))
                .from(post)
                .leftJoin(post.member, member)
                .where(containsSearch(searchVal))
                .where(isEqToBoardId(boardId))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    private List<MtPostDto> getPostGalleryDtos(Map<String,String> map, Pageable pageable, Long boardId) {
        List<MtPostDto> content = jpaQueryFactory
                .select(new QMtPostDto(
                        post.id,
                        post.title,
                        post.content,
                        post.createAt,
                        post.modifiedAt,
                        post.viewCount,
                        member.memberName,
                        post.boardId,
                        post.gbVal,
                        postImage.imageUrl
                ))
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.postImages, postImage)
                .where(isEqToGbVal(map.get("mtNo")))
                .where(isEqToBoardId(boardId))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    private BooleanExpression containsSearch(String searchVal) {
        return searchVal != null ? post.title.contains(searchVal) : null;
    }
    private BooleanExpression isEqToBoardId(Long boardId) {
        return boardId != null ? post.boardId.eq(boardId) : post.boardId.notIn(3L, 4L);
    }
    private BooleanExpression isEqToGbVal(String gbVal) {
        return gbVal != null ? post.gbVal.eq(gbVal) : null;
    }
    private BooleanExpression isEqToMemberId(String memberId) {
        return memberId != null ? post.gbVal.eq(memberId) : null;
    }
}