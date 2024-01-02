package sample.instagram.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;


@Service // Ioc
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    // 1. 패스워드는 알아서 체킹하니깐 신경쓸 필요 없다.
    // 2. 리턴이 잘되면 자동으로 세션을 만든다.


    @Override
    public UserDetails loadUserByUsername(String userid) throws UsernameNotFoundException {

        Member memberEntity = memberRepository.findByUsername(userid);

        if(memberEntity == null){
            return null;
        } else {
            return new PrincipalDetails(memberEntity);
        }

    }
}
