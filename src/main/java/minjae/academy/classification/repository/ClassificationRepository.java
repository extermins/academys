package minjae.academy.classification.repository;

import minjae.academy.classification.entity.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassificationRepository extends JpaRepository<Classification,String> {

}
