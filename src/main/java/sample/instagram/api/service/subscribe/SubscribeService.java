package sample.instagram.api.service.subscribe;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;
import sample.instagram.domain.member.MemberRepositoryJpa;
import sample.instagram.domain.subscribe.Subscribe;
import sample.instagram.domain.subscribe.SubscribeRepositoryJpa;
import sample.instagram.handler.ex.CustomApiException;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class SubscribeService {

    private final SubscribeRepositoryJpa subscribeRepositoryJpa;

    private final MemberRepository memberRepository;


    @Transactional
    public void createSubscribe(Long fromMemberId, Long toMemberId) {
        try {
            Member fromMember = memberRepository.findOne(fromMemberId);
            Member toMember = memberRepository.findOne(toMemberId);

            Subscribe subscribe = Subscribe.create(fromMember, toMember);
            subscribeRepositoryJpa.save(subscribe);

        } catch (Exception e) {
            throw new CustomApiException("이미 구독을 하였습니다.");
        }
    }

    @Transactional
    public void deleteSubscribe(Long fromUserId, Long toUserId) {
        subscribeRepositoryJpa.deleteByFromMemberIdAndToMemberId(fromUserId, toUserId);

    }
}
