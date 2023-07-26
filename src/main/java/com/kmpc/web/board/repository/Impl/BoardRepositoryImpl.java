package com.kmpc.web.board.repository.Impl;

import java.util.List;

import com.kmpc.web.board.dto.BoardDto;
import com.kmpc.web.board.dto.QBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.kmpc.web.board.repository.CustomBoardRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import static com.kmpc.web.board.entity.QBoard.board;
import static com.kmpc.web.member.entity.QMember.member;


@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements CustomBoardRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<BoardDto> selectBoardList(String searchVal, Pageable pageable) {
        List<BoardDto> content = getBoardMemberDtos(searchVal, pageable);
        Long count = getCount(searchVal);
        return new PageImpl<>(content, pageable, count);
    }

    private Long getCount(String searchVal){
        Long count = jpaQueryFactory
                .select(board.count())
                .from(board)
                //.leftJoin(board.member, member)   //검색조건 최적화
                .fetchOne();
        return count;
    }

    private List<BoardDto> getBoardMemberDtos(String searchVal, Pageable pageable){
        List<BoardDto> content = jpaQueryFactory
                .select(new QBoardDto(
                         board.id
                        ,board.title
                        ,board.content
                        ,board.regDate
                        ,board.uptDate
                        ,board.viewCount
                        ,member.memberName))
                .from(board)
                .leftJoin(board.member, member)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
        return content;
    }
}