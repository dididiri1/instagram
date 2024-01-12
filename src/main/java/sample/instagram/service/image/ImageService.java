package sample.instagram.service.image;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sample.instagram.service.aws.S3UploaderService;
import sample.instagram.domain.image.Image;
import sample.instagram.domain.image.ImageRepositoryJpa;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.dto.image.reponse.ImageResponse;
import sample.instagram.dto.image.reqeust.ImageCreateRequest;

@RequiredArgsConstructor
@Service
public class ImageService {

    private final ImageRepositoryJpa imageRepositoryJpa;

    private final MemberRepository memberRepository;

    private final S3UploaderService s3UploaderService;
    private static final String FOLDER_NAME = "story";

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
}
