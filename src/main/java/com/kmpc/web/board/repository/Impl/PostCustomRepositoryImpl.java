package com.kmpc.web.board.repository.Impl;

import com.kmpc.web.board.dto.*;
import com.kmpc.web.board.entity.QPostFile;
import com.kmpc.web.board.repository.PostCustomRepository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static com.kmpc.web.board.entity.QPost.post;
import static com.kmpc.web.board.entity.QPostFile.postFile;
import static com.kmpc.web.member.entity.QMember.member;


@Slf4j
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
    public Page<MtPostDto> selectGalleryList(Map<String, String> map,
                                              Pageable pageable, Long boardId, String memberId) {
        List<MtPostDto> content = getPostGalleryDtos(map, pageable, boardId, memberId);
        Long count = getCount(boardId);
        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<PostFileDto> selectPostFileDetail(Long postId) {
        List<PostFileDto> content = jpaQueryFactory
                .select(new QPostFileDto(
                        postFile.createAt,
                        postFile.modifiedAt,
                        postFile.id,
                        postFile.fileUrl,
                        postFile.storeFilename
                ))
                .from(postFile)
                .leftJoin(postFile.post)
                .where(postFile.post.id.eq(postId))
                .fetch();
        return content;
    }

    private Long getCount(Long BoardId) {
        Long count = jpaQueryFactory
                .select(post.count())
                .from(post)
                .where(isEqToBoardId(BoardId))
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
                .where(post.delYn.ne("Y"))
                .orderBy(post.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }

    private List<MtPostDto> getPostGalleryDtos(Map<String, String> map, Pageable pageable, Long boardId, String memberId) {
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
                        postFile.fileUrl,
                        member.memberId
                ))
                .from(post)
                .leftJoin(post.member, member)
                .leftJoin(post.postFiles, postFile)
                .where(isEqToGbVal(map.get("mtNo")))
                .where(isEqToBoardId(boardId))
                .where(isEqToMemberId(memberId))
                .where(afterCreateAtByStartDate(map.get("startDate")))
                .where(post.delYn.ne("Y"))
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
        return memberId != null ? post.member.memberId.eq(memberId) : null;
    }

    private BooleanExpression isEqToPostId(Long postId) {
        return postId != null ? post.id.eq(postId) : null;
    }

    private BooleanExpression afterCreateAtByStartDate(String startDate) {
        if (startDate == null){
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(startDate+"-01T00:00:00", formatter);
        log.info(dateTime.toString());
        return startDate != null ? post.createAt.after(dateTime) : null;
    }
}