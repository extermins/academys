package minjae.academy.banner.service;

import lombok.RequiredArgsConstructor;
import minjae.academy.banner.dto.BannerOrderDto;
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

    public List<Banner> allBanners(){
        return bannerJpaRepository.findAll();
    }

    @Transactional
    public void updateDisplayOrders(List<BannerOrderDto> orders) {
        for (BannerOrderDto orderDto : orders) {
            Banner banner = bannerJpaRepository.findById(orderDto.getUuid())
                    .orElseThrow(() -> new IllegalArgumentException("배너를 찾을 수 없습니다: " + orderDto.getUuid()));
            banner.setDisplayOrder(orderDto.getDisplayOrder());
        }
    }

}
