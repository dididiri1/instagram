
## 프로젝트 소개
스프링부트 SNS프로젝트 인스타그램 사이트를 mocking한 개인 프로젝트 입니다.

이지업의 [[메타코딩] 스프링부트 SNS프로젝트 - 포토그램 만들기](https://easyupclass.e-itwill.com/course/course_view.jsp?id=27&rtype=0&ch=course) 강의를 수강하면서 CSS, HTML를 사용했습니다.

## Stack

- Spring Boot, Security, AOP
- JPA, Spring Data JPA, Querydsl, jpql
- Thymeleaf, Jquery
- MySQL
- AWS EC2, S3, RDS, Route53
- Docker
- Junit5, Mockito
- Spring REST Docs


## Spring REST Docs
- [Docs 바로가기](https://kangmin.me/docs/index.html)
![](https://github.com/dididiri1/instagram/blob/main/src/main/resources/static/images/docs.png?raw=true)

## ERD 
- [ERD CLOUD 바로가기](https://www.erdcloud.com/d/6ahAYA76hYapBqPRr)

![](https://github.com/dididiri1/instagram/blob/main/src/main/resources/static/images/erd.png?raw=true)


## Directory
```
├───java
│   └───sample
│       └───instagram
│           │   InstagramApplication.java
│           │
│           ├───config
│           │   │   AwsS3Config.java
│           │   │   JapAuditingConfig.java
│           │   │   QueryDslConfig.java
│           │   │   SecurityConfig.java
│           │   │   ThymeleafConfig.java
│           │   │   WebMvcConfig.java
│           │   │
│           │   ├───aop
│           │   │       BindingAspect.java
│           │   │
│           │   └───auth
│           │           AuthFailureHandler.java
│           │           AuthSuccessHandler.java
│           │           PrincipalDetails.java
│           │           PrincipalDetailsService.java
│           │
│           ├───controller
│           │   ├───api
│           │   │   ├───comment
│           │   │   │       CommentApiController.java
│           │   │   │
│           │   │   ├───image
│           │   │   │       ImageApiController.java
│           │   │   │
│           │   │   ├───member
│           │   │   │       MemberApiController.java
│           │   │   │
│           │   │   └───subscribe
│           │   │           SubscribeApiController.java
│           │   │
│           │   └───web
│           │       ├───auth
│           │       │       AuthController.java
│           │       │
│           │       ├───image
│           │       │       ImageController.java
│           │       │
│           │       ├───member
│           │       │       MemberController.java
│           │       │
│           │       └───story
│           │               StoryController.java
│           │
│           ├───domain
│           │   │   BaseEntity.java
│           │   │
│           │   ├───comment
│           │   │       Comment.java
│           │   │       CommentQueryRepository.java
│           │   │       CommentRepositoryJpa.java
│           │   │
│           │   ├───image
│           │   │       Image.java
│           │   │       ImageQueryRepository.java
│           │   │       ImageRepository.java
│           │   │       ImageRepositoryJpa.java
│           │   │
│           │   ├───like
│           │   │       Like.java
│           │   │       LikeRepositoryJpa.java
│           │   │
│           │   ├───member
│           │   │       Member.java
│           │   │       MemberQueryRepository.java
│           │   │       MemberRepository.java
│           │   │       MemberRepositoryJpa.java
│           │   │
│           │   └───subscribe
│           │           Subscribe.java
│           │           SubscribeRepositoryJpa.java
│           │
│           ├───dto
│           │   │   ApiResponse.java
│           │   │   Role.java
│           │   │
│           │   ├───auth
│           │   │       AuthResponse.java
│           │   │
│           │   ├───comment
│           │   │   ├───request
│           │   │   │       CommentRequest.java
│           │   │   │
│           │   │   └───response
│           │   │           CommentResponse.java
│           │   │
│           │   ├───image
│           │   │   ├───reponse
│           │   │   │       ImageCreateResponse.java
│           │   │   │       ImagePopularResponse.java
│           │   │   │       ImageResponse.java
│           │   │   │       ImageStoryResponse.java
│           │   │   │
│           │   │   └───request
│           │   │           ImageCreateRequest.java
│           │   │           ImageSearch.java
│           │   │
│           │   ├───member
│           │   │   ├───request
│           │   │   │       MemberCreateRequest.java
│           │   │   │       MemberProfileRequest.java
│           │   │   │       MemberUpdateRequest.java
│           │   │   │       ProfileImageRequest.java
│           │   │   │       ProfileImageResponse.java
│           │   │   │
│           │   │   └───response
│           │   │           MemberCreateResponse.java
│           │   │           MemberProfileResponse.java
│           │   │           MemberResponse.java
│           │   │           MemberSubscribeResponse.java
│           │   │           MemberUpdateResponse.java
│           │   │
│           │   └───subscribe
│           │       └───response
│           │               SubscribeResponse.java
│           │
│           ├───handler
│           │   │   GlobalExceptionHandler.java
│           │   │   
│           │   └───ex
│           │           CustomApiDuplicateKey.java
│           │           CustomApiException.java
│           │           CustomException.java
│           │           CustomValidationApiException.java
│           │           CustomValidationException.java
│           │
│           ├───service
│           │   ├───auth
│           │   │       AuthService.java
│           │   │
│           │   ├───aws
│           │   │       S3UploadService.java
│           │   │
│           │   ├───comment
│           │   │       CommentService.java
│           │   │
│           │   ├───image
│           │   │       ImageService.java
│           │   │
│           │   ├───like
│           │   │       LikeService.java
│           │   │
│           │   ├───member
│           │   │       MemberService.java
│           │   │
│           │   └───subscribe
│           │           SubscribeService.java
│           │
│           └───util
│                   Script.java
│
└───resources
    └───application.yml

```

### Reference

- [[메타코딩] 스프링부트 SNS프로젝트 - 포토그램 만들기](https://easyupclass.e-itwill.com/course/course_view.jsp?id=27&rtype=0&ch=course)