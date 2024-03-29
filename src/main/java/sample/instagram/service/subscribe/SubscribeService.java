package sample.instagram.service.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.dto.subscribe.response.SubscribeResponse;
import sample.instagram.handler.ex.CustomApiException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepositoryJpa subscribeRepositoryJpa;

    private final MemberRepository memberRepository;

    @Transactional
    public SubscribeResponse createSubscribe(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberRepository.findOne(fromMemberId);
        Member toMember = memberRepository.findOne(toMemberId);
        Subscribe subscribe = Subscribe.create(fromMember, toMember);
        Subscribe saveSubscribe = validateDuplicateCreateSubscribeAndToEntity(subscribe);

        return SubscribeResponse.of(saveSubscribe);
    }

    private Subscribe validateDuplicateCreateSubscribeAndToEntity(Subscribe subscribe) {
        try {
            return subscribeRepositoryJpa.save(subscribe);
        } catch (Exception e) {
            throw new CustomApiException("이미 구독을 하였습니다.");
        }
    }

    @Transactional
    public void deleteSubscribe(Long fromUserId, Long toUserId) {
        subscribeRepositoryJpa.deleteByFromMemberIdAndToMemberId(fromUserId, toUserId);
    }

}
