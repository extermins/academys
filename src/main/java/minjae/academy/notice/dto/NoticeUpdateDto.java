package minjae.academy.notice.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import minjae.academy.notice.entity.Notice;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoticeUpdateDto {

    private String title;

    private String content;

    public Notice toEntity(){
        return Notice.builder()
                .title(this.title)
                .content(this.content)
                .build();
    }

}
