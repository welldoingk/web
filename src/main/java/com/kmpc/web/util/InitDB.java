package com.kmpc.web.util;

import com.kmpc.web.board.entity.Board;
import com.kmpc.web.board.entity.Post;
import com.kmpc.web.board.entity.PostImage;
import com.kmpc.web.board.repository.BoardRepository;
import com.kmpc.web.board.repository.PostImageRepository;
import com.kmpc.web.board.repository.PostRepository;
import com.kmpc.web.common.entity.Code;
import com.kmpc.web.common.repository.CodeRepository;
import com.kmpc.web.member.entity.Member;
import com.kmpc.web.member.repository.MemberRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

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
        private final PostRepository postRepository;
        private final PostImageRepository postImageRepository;
        private final PasswordEncoder passwordEncoder;

        public void userDBInit() {

            List<Code> codeList = codeRepository.findAll();
            if (codeList.isEmpty()) {
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

                code = Code.builder()
                        .classCode("Notice")
                        .codeNo("1")
                        .codeName("Head")
                        .build();

                codeRepository.save(code);

                code = Code.builder()
                        .classCode("Notice")
                        .codeNo("2")
                        .codeName("산행")
                        .build();

                codeRepository.save(code);

                code = Code.builder()
                        .classCode("Notice")
                        .codeNo("3")
                        .codeName("중요")
                        .build();

                codeRepository.save(code);

                code = Code.builder()
                        .classCode("Notice")
                        .codeNo("4")
                        .codeName("일반")
                        .build();

                codeRepository.save(code);
            }

            List<Board> boardList = boardRepository.findAll();
            if (boardList.isEmpty()) {
                Board board = Board.builder()
                        .boardId(1L)
                        .boardName("공지")
                        .build();

                boardRepository.save(board);

                board = Board.builder()
                        .boardId(2L)
                        .boardName("자유")
                        .build();

                boardRepository.save(board);

                board = Board.builder()
                        .boardId(3L)
                        .boardName("사진")
                        .build();

                boardRepository.save(board);

                board = Board.builder()
                        .boardId(4L)
                        .boardName("이벤트")
                        .build();

                boardRepository.save(board);
            }

            List<Member> memberList = memberRepository.findAll();
            if (memberList.isEmpty()) {
                Member member = Member.builder()
                        .memberId("1")
                        .password(passwordEncoder.encode("1"))
                        .memberName("테스트")
                        .nickname("테스트")
                        .build();
                // member 저장
                memberRepository.save(member);
            }

            List<Post> postList = postRepository.findAll();
            if (postList.isEmpty()) {
                Post post = Post.builder()
                        .member(memberRepository.findByMemberId("1").get())
                        .title("북한산")
                        .content("")
                        .boardId(3L)
                        .build();
                PostImage postImage = PostImage.builder()
                        .post(post)
                        .imageUrl("https://kmpc-img-bucket.s3.ap-northeast-2.amazonaws.com/post/C06_1938.jpg")
                        .storeFilename("C06_1938.jpg")
                        .build();
                // member 저장
                postRepository.save(post);
                postImageRepository.save(postImage);

                post = Post.builder()
                        .member(memberRepository.findByMemberId("1").get())
                        .title("공지 테스트")
                        .content("")
                        .boardId(1L)
                        .build();
                postImage = PostImage.builder()
                        .post(post)
                        .imageUrl("https://kmpc-img-bucket.s3.ap-northeast-2.amazonaws.com/post/C06_1938.jpg")
                        .storeFilename("C06_1938.jpg")
                        .build();
                // member 저장
                postRepository.save(post);
                postImageRepository.save(postImage);

                post = Post.builder()
                        .member(memberRepository.findByMemberId("1").get())
                        .title("자유 테스트")
                        .content("")
                        .boardId(2L)
                        .build();
                postImage = PostImage.builder()
                        .post(post)
                        .imageUrl("https://kmpc-img-bucket.s3.ap-northeast-2.amazonaws.com/post/C06_1938.jpg")
                        .storeFilename("C06_1938.jpg")
                        .build();
                // member 저장
                postRepository.save(post);
                postImageRepository.save(postImage);
            }
        }
    }
}