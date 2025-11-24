package minjae.academy.notice.dto;

import lombok.Data;
import minjae.academy.notice.entity.Notice;

@Data
public class NoticeCreateDto {

    private String title;
    private String content;

    public Notice toEntity(){
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }
}
