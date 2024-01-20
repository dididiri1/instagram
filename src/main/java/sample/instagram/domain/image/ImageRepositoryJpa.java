package sample.instagram.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepositoryJpa extends JpaRepository<Image, Long> {

    List<Image> findAllByOrderByIdDesc();
}
