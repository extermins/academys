package minjae.academy.banner.service;

import lombok.RequiredArgsConstructor;
import minjae.academy.banner.dto.BannerOrderDto;
import minjae.academy.banner.dto.BannerUpdateDto;
import minjae.academy.banner.entity.Banner;
import minjae.academy.banner.repository.BannerJpaRepository;
import minjae.academy.file.service.BannerFileService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BannerService {
    private final BannerJpaRepository bannerJpaRepository;

    public void addBanner(Banner banner){
        bannerJpaRepository.save(banner);
    }
    // 전체배너
    public List<Banner> allBanners(){
        return bannerJpaRepository.findAll();
    }

    //배너 하나만
    public Banner getOneBanner(String uuid) {
        return bannerJpaRepository.findById(uuid)
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다: " + uuid));
    }


    @Transactional
    public void updateDisplayOrders(List<BannerOrderDto> orders) {
        for (BannerOrderDto orderDto : orders) {
            Banner banner = bannerJpaRepository.findById(orderDto.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다: " + orderDto.getUuid()));
            banner.setDisplayOrder(orderDto.getDisplayOrder());
        }
    }


    @Transactional
    public void updateBanner(BannerUpdateDto bannerUpdateDto) {
        Banner banner = bannerJpaRepository.findByUuid(bannerUpdateDto.getUuid())
                .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다."));

        // 엔티티 업데이트
        banner.setLinkUrl(bannerUpdateDto.getLinkUrl());
        banner.setDescription(bannerUpdateDto.getDescription());
        banner.setStartDate(bannerUpdateDto.getStartDate());
        banner.setEndDate(bannerUpdateDto.getEndDate());
        banner.setEnabled(bannerUpdateDto.isEnabled());

        // 이미지 URL이 변경된 경우에만 업데이트
        if(bannerUpdateDto.getImageUrl() != null) {
            banner.setImageUrl(bannerUpdateDto.getImageUrl());
        }

        // JPA의 변경 감지로 자동 업데이트
    }
}
