package minjae.academy.video.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VideoResponseDto<T> {
    private boolean success;
    private String message;
    private T data;

    public static <T> VideoResponseDto<T> success(T data) {
        return VideoResponseDto.<T>builder()
                .success(true)
                .message("Success")
                .data(data)
                .build();
    }

    public static <T> VideoResponseDto<T> success(String message, T data) {
        return VideoResponseDto.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    public static <T> VideoResponseDto<T> error(String message) {
        return VideoResponseDto.<T>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}