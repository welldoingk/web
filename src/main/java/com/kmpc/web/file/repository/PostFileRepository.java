package com.kmpc.web.file.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kmpc.web.file.entity.PostFile;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
}
