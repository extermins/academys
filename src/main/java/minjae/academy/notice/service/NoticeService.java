package minjae.academy.notice.service;

import lombok.RequiredArgsConstructor;
import minjae.academy.notice.entity.Notice;
import minjae.academy.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public void addNotice(Notice notice) {
        noticeRepository.save(notice);
    }

    public List<Notice> findAll() {
        return noticeRepository.findAll();
    }

    public Optional<Notice> findById(String uuid) {
        return noticeRepository.findById(uuid);
    }

    @Transactional
    public void updateNotice(String uuid, Notice notice) {
        Notice notice1 = noticeRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        notice1.setTitle(notice.getTitle());
        notice1.setContent(notice.getContent());
        noticeRepository.save(notice1);
    }

    @Transactional
    public void deleteNotice(String uuid) {
        noticeRepository.deleteById(uuid);
    }
}
