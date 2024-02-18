package jpabook.jpastudy.service;

import jpabook.jpastudy.domain.Member;
import jpabook.jpastudy.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class MemberServiceTest {

	@Autowired
	MemberService memberService;

	@Autowired
	MemberRepository memberRepository;

	@DisplayName("회원가입")
	@Test
	void join() {

		// given
		Member member = new Member();
		member.setName("park");

		// when
		Long savedId = memberService.join(member);

		// then
		assertThat(member).isEqualTo(memberRepository.findOne(savedId));
	}

	@DisplayName("중복 회원 예외")
	@Test
	void validateDuplicateMember() {

		// given
		Member meber1 = new Member();
		meber1.setName("park");

		Member member2 = new Member();
		member2.setName("park");

		// when // then
		memberService.join(meber1);
		assertThatThrownBy(() -> memberService.join(member2))
			.isInstanceOf(IllegalStateException.class)
			.hasMessage("이미 존재하는 회원입니다.");
	}
}