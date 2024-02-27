package jpabook.jpastudy.api;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jpabook.jpastudy.domain.Member;
import jpabook.jpastudy.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

	private final MemberService memberService;

	/**
	 * 회원 조회 v1
	 *
	 * @return List<Member> 엔티티로 리턴하기
	 */
	@GetMapping("/api/v1/members")
	public List<Member> membersV1() {
		return memberService.findMembers();
	}

	@GetMapping("/api/v2/members")
	public Result membersV2() {
		List<Member> findMembers = memberService.findMembers();

		List<MemberDto> collect = findMembers.stream().map(m -> new MemberDto(m.getName()))
			.collect(Collectors.toList());

		return new Result<>(collect);
	}

	@Data
	@AllArgsConstructor
	static class Result<T> {
		private T data;
	}

	@Data
	@AllArgsConstructor
	static class MemberDto {
		private String name;
	}

	/**
	 * 회원 등록 v1
	 *
	 * @param member Member 엔티티로 입력 받기
	 */
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	/**
	 * 회원 등록 v2
	 *
	 * @param request CreateMemberRequest DTO로 입력 받기
	 */
	@PostMapping("/api/v2/members")
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

		Member member = new Member();
		member.setName(request.getName());

		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}

	/**
	 * 회원 수정 v2
	 */
	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable Long id, @RequestBody @Valid UpdateMemberRequest request) {

		memberService.update(id, request.getName());
		Member findMember = memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(), findMember.getName());
	}

	@Data
	static class UpdateMemberRequest {
		private String name;
	}

	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse {
		private Long id;
		private String name;
	}

	@Data
	static class CreateMemberRequest {
		@NotEmpty
		private String name;
	}

	@Data
	static class CreateMemberResponse {
		private Long id;

		public CreateMemberResponse(Long id) {
			this.id = id;
		}
	}
}
