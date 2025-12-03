package minjae.academy.video.repository;

import minjae.academy.video.entity.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video,String> {
    List<Video> findAllByOrderByCreatedAtDesc();

    Optional<Video> findByStoredFilename(String storedFilename);

    List<Video> findByStatus(Video.VideoStatus status);

    // 페이지네이션 지원
    Page<Video> findAllByOrderByCreatedAtDesc(Pageable pageable);

    // 검색 + 페이지네이션
    @Query("SELECT v FROM Video v WHERE " +
            "(:keyword IS NULL OR :keyword = '' OR " +
            "LOWER(v.originalFilename) LIKE LOWER(CONCAT('%', :keyword, '%'))) " +
            "AND (:status IS NULL OR v.status = :status) " +
            "ORDER BY v.createdAt DESC")
    Page<Video> searchVideos(@Param("keyword") String keyword,
                             @Param("status") Video.VideoStatus status,
                             Pageable pageable);
}
