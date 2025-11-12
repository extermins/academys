package minjae.academy.banner.repository;

import minjae.academy.banner.entity.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BannerJpaRepository extends JpaRepository<Banner, String> {
    List<Banner> findAll();
}
