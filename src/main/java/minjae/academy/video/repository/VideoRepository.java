package minjae.academy.video.repository;

import minjae.academy.video.entity.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository<Video,String> {
    List<Video> findAllByOrderByCreatedAtDesc();

    Optional<Video> findByStoredFilename(String storedFilename);

    List<Video> findByStatus(Video.VideoStatus status);
}
