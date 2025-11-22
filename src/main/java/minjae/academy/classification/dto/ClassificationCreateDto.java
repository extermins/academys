package minjae.academy.classification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import minjae.academy.classification.entity.Classification;

@Getter
@Setter
@NoArgsConstructor
public class ClassificationCreateDto {

    private String name;

    private Integer year;

    private Integer depth;

    private String parentUuid;

}
