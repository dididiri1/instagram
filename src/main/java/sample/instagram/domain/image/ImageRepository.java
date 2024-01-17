package sample.instagram.domain.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@RequiredArgsConstructor
@Repository
public class ImageRepository {

    private final EntityManager em;

    public Image findOne(Long id) {
        return em.find(Image.class, id);
    }

}
