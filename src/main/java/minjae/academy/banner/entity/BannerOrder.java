package minjae.academy.banner.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class BannerOrder {
    @Id
    private String uuid;

    @Column
    private String bannerUuid;

    @Column
    private Integer displayOrder;
}
