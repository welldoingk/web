package com.kmpc.web.util;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.kmpc.web.board.entity.Board;
import com.kmpc.web.board.repository.BoardRepository;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.userDBInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final CodeRepository codeRepository;
        private final MemberRepository memberRepository;
        private final BoardRepository boardRepository;
        private final PasswordEncoder passwordEncoder;

        public void userDBInit() {

            List<Code> codeList = codeRepository.findAll();
            if (codeList.size() == 0) {
                Code code = Code.builder()
                        .classCode("MT")
                        .codeNo("1")
                        .codeName("북한산")
                        .build();

                codeRepository.save(code);

                code = Code.builder()
                        .classCode("MT")
                        .codeNo("2")
                        .codeName("설악산")
                        .build();

                codeRepository.save(code);

                code = Code.builder()
                        .classCode("MT")
                        .codeNo("3")
                        .codeName("백두산")
                        .build();

                codeRepository.save(code);
            }

            List<Board> boardList = boardRepository.findAll();
            if (boardList.size() == 0) {
                Board board = Board.builder()
                        .boardName("공지")
                        .boardType("1")
                        .build();

                boardRepository.save(board);

                board = Board.builder()
                        .boardName("자유")
                        .boardType("2")
                        .build();

                boardRepository.save(board);

                board = Board.builder()
                        .boardName("사진")
                        .boardType("3")
                        .build();

                boardRepository.save(board);
            }

            List<Member> memberList = memberRepository.findAll();
            if (memberList.size() == 0) {
                Member member = Member.builder()
                        .memberId("1")
                        .password(passwordEncoder.encode("1"))
                        .memberName("테스트")
                        .nickname("테스트")
                        .build();
                // member 저장
                memberRepository.save(member);
            }
        }
    }
}