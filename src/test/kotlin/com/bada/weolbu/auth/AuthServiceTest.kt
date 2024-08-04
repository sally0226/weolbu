package com.bada.weolbu.auth

import com.bada.weolbu.auth.model.SignupRequestDTO
import com.bada.weolbu.common.exception.DuplicateUserException
import com.bada.weolbu.common.exception.InvalidPasswordException
import com.bada.weolbu.common.exception.InvalidRefreshTokenException
import com.bada.weolbu.common.exception.UserNotFoundException
import com.bada.weolbu.entity.*
import io.jsonwebtoken.Claims
import io.jsonwebtoken.impl.DefaultClaims
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.should
import io.kotest.matchers.string.haveMinLength
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import io.mockk.*
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class AuthServiceTest : DescribeSpec() {
    private var authManager = mockk<AuthenticationManager>()

    private var userRepository = mockk<UserRepository>()

    private var refreshTokenRepository = mockk<RefreshTokenRepository>()

    private var jwtProvider = mockk<JwtProvider>()

    private var authService: AuthService = AuthService(authManager, userRepository, refreshTokenRepository, jwtProvider)

    init {
        describe("signup") {
            context("유효한 데이터가 주어지면") {
                it("회원가입이 성공적으로 완료된다") {
                    val signupRequest = SignupRequestDTO(
                        name = "Test User",
                        password = "ValidPassword123",
                        email = "test@example.com",
                        role = UserRole.INSTRUCTOR,
                        phoneNumber = "010-1234-5678"
                    )

                    every { userRepository.save(any<User>()) } returns signupRequest.toUser()

                    authService.signup(signupRequest)

                    verify(exactly = 1) { userRepository.save(any<User>()) }
                }
            }

            context("유효하지 않은 비밀번호가 주어지면") {
                it("InvalidPasswordException 예외를 던진다") {
                    val signupRequest = SignupRequestDTO(
                        name = "Test User",
                        password = "invalid",
                        email = "test@example.com",
                        role = UserRole.INSTRUCTOR,
                        phoneNumber = "010-1234-5678"
                    )
                    shouldThrow<InvalidPasswordException> {
                        authService.signup(signupRequest)
                    }
                }
            }

            context("중복된 이메일이 주어지면") {
                it("DuplicateUserException 예외를 던진다") {
                    val signupRequest = SignupRequestDTO(
                        name = "Test User",
                        password = "ValidPassword123",
                        email = "test@example.com",
                        role = UserRole.INSTRUCTOR,
                        phoneNumber = "010-1234-5678"
                    )

                    every { userRepository.save(any<User>()) } throws DuplicateUserException("test@example.com")

                    shouldThrow<DuplicateUserException> {
                        authService.signup(signupRequest)
                    }
                }
            }
        }

        describe("signIn") {
            context("유효한 이메일과 비밀번호가 주어지면") {
                it("로그인이 성공적으로 완료된다") {
                    val email = "test@example.com"
                    val password = "ValidPassword123"
                    val user = User(
                        email = email,
                        name = "Test User",
                        phoneNumber = "010-1234-5678",
                        role = UserRole.INSTRUCTOR,
                        password = ""
                    )

                    every { authManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns UsernamePasswordAuthenticationToken(
                        email,
                        password
                    )
                    every { userRepository.findByEmail(email) } returns user
                    every { jwtProvider.generate(any(), any()) } returns "token"
                    every { refreshTokenRepository.findByUser(any()) } returns null
                    every { refreshTokenRepository.save(any()) } returns mockk()

                    val result = authService.signIn(email, password)

                    result.accessToken should haveMinLength(1)
                    result.refreshToken should haveMinLength(1)
                }
            }

            context("유효하지 않은 이메일이 주어지면") {
                it("UserNotFoundException 예외를 던진다") {
                    val email = "invalid@example.com"
                    val password = "ValidPassword123"

                    every { authManager.authenticate(any<UsernamePasswordAuthenticationToken>()) } returns UsernamePasswordAuthenticationToken(
                        email,
                        password
                    )
                    every { userRepository.findByEmail(email) } returns null

                    shouldThrow<UserNotFoundException> {
                        authService.signIn(email, password)
                    }
                }
            }
        }

        describe("refresh") {
            context("유효한 리프레시 토큰이 주어지면") {
                it("새로운 엑세스 토큰을 반환한다") {
                    val refreshToken = "validRefreshToken"
                    val user = User(
                        email = "test@example.com",
                        name = "Test User",
                        phoneNumber = "010-1234-5678",
                        role = UserRole.INSTRUCTOR,
                        password = ""
                    )

                    val claims: Claims = DefaultClaims(mapOf("email" to "test@example.com"))
                    every { jwtProvider.validate(refreshToken, TokenType.RefreshToken) } returns true
                    every { jwtProvider.getClaims(refreshToken) } returns claims
                    every { refreshTokenRepository.findByRefreshToken(refreshToken) } returns RefreshToken(
                        user = user,
                        refreshToken = refreshToken
                    )
                    every { jwtProvider.generate(any(), any()) } returns "newAccessToken"

                    val result = authService.refresh(refreshToken)

                    result.accessToken should haveMinLength(1)
                    result.refreshToken should haveMinLength(1)
                }
            }

            context("유효하지 않은 리프레시 토큰이 주어지면") {
                it("InvalidRefreshTokenException 예외를 던진다") {
                    val refreshToken = "invalidRefreshToken"

                    every { jwtProvider.validate(refreshToken, TokenType.RefreshToken) } returns false

                    shouldThrow<InvalidRefreshTokenException> {
                        authService.refresh(refreshToken)
                    }
                }
            }
        }
    }
}