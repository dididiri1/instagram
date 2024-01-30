package sample.instagram.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageQueryRepository;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.image.reponse.ImageCreateResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.aws.S3UploaderService;
import sample.instagram.dto.image.reponse.ImagePopularResponse;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepositoryJpa imageRepositoryJpa;

    private final ImageQueryRepository imageQueryRepository;

    private final MemberRepository memberRepository;

    private final S3UploaderService s3UploaderService;
    private static final String FOLDER_NAME = "story";

    @Transactional
    public ImageCreateResponse createImage(ImageCreateRequest imageCreateRequest) {
        Member member = memberRepository.findOne(imageCreateRequest.getMemberId());
        String imageUrl = getImageUrl(imageCreateRequest);
        Image image = imageCreateRequest.toEntity(imageUrl, member);
        Image imageEntity = imageRepositoryJpa.save(image);

        return ImageCreateResponse.of(imageEntity);
    }

    private String getImageUrl(ImageCreateRequest imageCreateRequest) {
        return s3UploaderService.uploadFileS3(imageCreateRequest.getFile(), FOLDER_NAME);
    }

    public List<ImageStoryResponse> getMyStory(Long memberId, Pageable pageable) {
        List<Image> images = imageQueryRepository.findMySubscriptionStory(memberId, pageable);

        return images.stream()
                .map(image -> ImageStoryResponse.of(image, memberId))
                .collect(Collectors.toList());
    }

    public List<ImageStoryResponse> getStory(Long memberId, Pageable pageable) {
        List<Image> images = imageQueryRepository.findAllStory(memberId, pageable);

        return images.stream()
                .map(image -> ImageStoryResponse.of(image, memberId))
                .collect(Collectors.toList());
    }

    public List<ImagePopularResponse> getPopularImages() {
        List<Image> images = imageQueryRepository.findAllWithMemberLikes();

        return images.stream()
                .map(image -> ImagePopularResponse.of(image))
                .sorted(Comparator.comparing(ImagePopularResponse::getLikeCount).reversed())
                .collect(Collectors.toList());
    }

}
