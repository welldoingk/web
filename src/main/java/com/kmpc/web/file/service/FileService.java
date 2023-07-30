package com.kmpc.web.file.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.kmpc.web.board.dto.PostDto;
import com.kmpc.web.file.dto.FileDto;
import com.kmpc.web.file.dto.PostFileDto;
import com.kmpc.web.file.entity.PostFile;
import com.kmpc.web.file.repository.FileRepository;
import com.kmpc.web.file.repository.PostFileRepository;
import com.kmpc.web.member.repository.MemberRepository;

import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.InputStream;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileService {

    @Value("${upload.path}")
    private String uploadDir;
    @Value("${upload.path2}")
    private String uploadDir2;

    private final FileRepository fileRepository;

    private final PostFileRepository postFileRepository;

    private final MemberRepository memberRepository;

    @Transactional
    public Map<String, Object> saveFile(PostDto postDto, Long postId) throws Exception {

        String os = System.getProperty("os.name").toLowerCase();

        List<MultipartFile> multipartFile = postDto.getImageFiles();

        // 결과 Map
        Map<String, Object> result = new HashMap<String, Object>();

        // 파일 시퀀스 리스트
        List<Long> fileIds = new ArrayList<Long>();

        try {
            if (multipartFile != null) {
                if (multipartFile.size() > 0 && !multipartFile.get(0).getOriginalFilename().equals("")) {
                    for (MultipartFile file1 : multipartFile) {
                        String originalFileName = file1.getOriginalFilename(); // 오리지날 파일명
                        String extension = originalFileName.substring(originalFileName.lastIndexOf(".")); // 파일 확장자
                        String savedFileName = UUID.randomUUID() + extension; // 저장될 파일 명

                        File targetFile = new File(os.equals("win") ? uploadDir : uploadDir2 + savedFileName);

                        // 초기값으로 fail 설정
                        result.put("result", "FAIL");

                        FileDto fileDto = FileDto.builder()
                                .originFileName(originalFileName)
                                .savedFileName(savedFileName)
                                .uploadDir(os.equals("win") ? uploadDir : uploadDir2)
                                .extension(extension)
                                .size(file1.getSize())
                                .contentType(file1.getContentType())
                                .build();
                        // 파일 insert
                        com.kmpc.web.file.entity.File file = fileDto.toEntity();
                        Long fileId = insertFile(file);
                        log.info("fileId={}", fileId);

                        try {
                            InputStream fileStream = file1.getInputStream();
                            FileUtils.copyInputStreamToFile(fileStream, targetFile); // 파일 저장
                            // 배열에 담기
                            fileIds.add(fileId);
                            result.put("fileIdxs", fileIds.toString());
                            result.put("result", "OK");
                        } catch (Exception e) {
                            // 파일삭제
                            FileUtils.deleteQuietly(targetFile); // 저장된 현재 파일 삭제
                            e.printStackTrace();
                            result.put("result", "FAIL");
                            break;
                        }

                        PostFileDto bostFileDto = PostFileDto.builder()
                                .postId(postId)
                                .build();

                        PostFile postFile = bostFileDto.toEntity(file);
                        insertPostFile(postFile);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /** 파일 저장 db */
    @Transactional
    public Long insertFile(com.kmpc.web.file.entity.File file) {
        return fileRepository.save(file).getId();
    }

    @Transactional
    public Long insertPostFile(PostFile postFile) {
        return postFileRepository.save(postFile).getId();
    }

}