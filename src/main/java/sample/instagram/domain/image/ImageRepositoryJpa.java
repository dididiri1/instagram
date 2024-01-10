package sample.instagram.domain.image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepositoryJpa extends JpaRepository<Image, Long> {

}
