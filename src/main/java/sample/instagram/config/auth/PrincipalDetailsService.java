package sample.instagram.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import sample.instagram.domain.member.Member;
import sample.instagram.domain.member.MemberRepository;


@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("username = " + username);

        Member memberEntity = memberRepository.findByUsername(username);

        System.out.println("memberEntity = " + memberEntity);

        if(memberEntity == null){
            return null;
        } else {
            return new PrincipalDetails(memberEntity);
        }

    }
}
