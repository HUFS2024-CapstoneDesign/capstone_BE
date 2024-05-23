package com.example.capstone.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.example.capstone.Converter.MemberConverter;
import com.example.capstone.common.BaseResponse;
import com.example.capstone.dto.request.MemberRequestDto.*;
import com.example.capstone.dto.response.MemberResponseDto.*;
import com.example.capstone.exception.GlobalErrorCode;
import com.example.capstone.kakao.KakaoLoginParams;
import com.example.capstone.service.MemberCommandService;
import com.example.capstone.service.MemberQueryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/members")
@Tag(name = "😎 Member", description = "사용자 관련 API")
public class MemberController {

  private final MemberCommandService memberCommandService;
  private final MemberQueryService memberQueryService;

  @Operation(summary = "회원가입 API", description = "회원가입을 진행합니다")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "성공"),
  })
  @PostMapping("/signup")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<SignUpMemberResponse> signUpMember(@RequestBody SignUpMemberRequest request) {
    return BaseResponse.onSuccess(
        GlobalErrorCode.CREATED,
        MemberConverter.toSignUpMemberResponse(memberCommandService.signUpMember(request)));
  }

  @Operation(summary = "로그인 API", description = "이메일, 비밀번호를 사용한 로그인을 진행합니다")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "성공"),
  })
  @PostMapping("/login")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<TokenResponse> loginMember(@RequestBody LoginMemberRequest request) {
    return BaseResponse.onSuccess(GlobalErrorCode.CREATED, memberCommandService.login(request));
  }

  @Operation(summary = "카카오 API", description = "카카오로그인을 합니다.")
  @ApiResponses({
    @ApiResponse(responseCode = "201", description = "성공"),
  })
  @PostMapping("/kakao")
  @ResponseStatus(HttpStatus.CREATED)
  public BaseResponse<TokenResponse> loginKakao(@RequestBody KakaoLoginParams params) {
    return BaseResponse.onSuccess(
        MemberConverter.toKakaoLogin(memberCommandService.loginKakao(params)));
  }

  @Operation(summary = "reissue API", description = "토큰을 재발급합니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @PostMapping("/reissue")
  public BaseResponse<TokenResponse> reissue(@RequestBody ReissueRequest request) {
    return BaseResponse.onSuccess(memberCommandService.reissue(request));
  }

  @Operation(summary = "아이디 찾기 API", description = "닉네임을 통해 아이디를 찾습니다.")
  @ApiResponse(responseCode = "200", description = "성공")
  @GetMapping("/find-id")
  public BaseResponse<FindEmailByNickNameResponse> reissue(@RequestParam String nickName) {
    return BaseResponse.onSuccess(
        MemberConverter.toFindEmailByNickNameResponse(
            memberQueryService.findMemberByNickName(nickName).get()));
  }

  @Operation(summary = "비밀번호 찾기 API", description = "이메일을 통해 아이디를 찾습니다.")
  @ApiResponse(responseCode = "201", description = "성공")
  @PostMapping("/find-password")
  public BaseResponse<Boolean> findPasswordByEmail(@RequestBody FindPasswordByEmailRequest request)
      throws Exception {
    return BaseResponse.onSuccess(memberCommandService.sendEmail(request));
  }

  @Operation(summary = "비밀번호 찾기 API", description = "이메일을 통해 아이디를 찾습니다.")
  @ApiResponse(responseCode = "201", description = "성공")
  @PostMapping("/check-code")
  public BaseResponse<CheckCodeResponse> checkCode(@RequestBody VerifyCodeRequest request)
      throws Exception {
    return BaseResponse.onSuccess(
        MemberConverter.toCheckCodeResponse(memberCommandService.isVerifiedNumber(request)));
  }
}
