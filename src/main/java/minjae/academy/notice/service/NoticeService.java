package minjae.academy.notice.service;

import lombok.RequiredArgsConstructor;
import minjae.academy.notice.entity.Notice;
import minjae.academy.notice.repository.NoticeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    public void addNotice(Notice notice){
        noticeRepository.save(notice);
    }

    public List<Notice> findAll(){
        return noticeRepository.findAll();
    }

    public Optional<Notice> findById(String uuid){
        return noticeRepository.findById(uuid);
    }
}
