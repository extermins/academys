package minjae.academy.config.SpringSecurity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import minjae.academy.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
    private final Member member;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Enum Role → "ROLE_" prefix 부여
        return List.of(new SimpleGrantedAuthority("ROLE_" + member.getRole().name()));
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 기능 쓰지 않으면 true 고정
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 비밀번호 유효기간 사용 안 하면 true
    }

    @Override
    public boolean isEnabled() {
        return true; // 활성화 여부 (추가 컬럼 없으므로 true)
    }

    public Member getMember() {
        return member;
    }
}
