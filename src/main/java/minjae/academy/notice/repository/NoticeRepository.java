package minjae.academy.notice.repository;

import minjae.academy.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice,String> {
    List<Notice> findAll();
    Optional<Notice> findById(String uuid);
}
