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
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reponse.ImageStoryResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;
import sample.instagram.service.aws.S3UploaderService;

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
    public ImageResponse createImage(ImageCreateRequest imageCreateRequest) {
        Member member = memberRepository.findOne(imageCreateRequest.getMemberId());
        String imageUrl = getImageUrl(imageCreateRequest);
        Image image = imageCreateRequest.create(imageUrl, member);
        Image imageEntity = imageRepositoryJpa.save(image);

        return ImageResponse.of(imageEntity);
    }

    private String getImageUrl(ImageCreateRequest imageCreateRequest) {
        return s3UploaderService.uploadFileS3(imageCreateRequest.getFile(), FOLDER_NAME);
    }

    public List<ImageStoryResponse> getStory(Long memberId, Pageable pageable) {
        List<Image> images = imageQueryRepository.findStoryWithImageMember(memberId, pageable);

        return images.stream()
                .map(image -> ImageStoryResponse.of(image))
                .collect(Collectors.toList());
    }

}
