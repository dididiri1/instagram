package sample.instagram.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepositoryJpa extends JpaRepository<Member, Long> {

    Member findByUsername(String username);
}
