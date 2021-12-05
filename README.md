# 🍝 나만의 은밀한 라따뚜이

![ezgif-3-410d82ddc14b](https://user-images.githubusercontent.com/56161518/144391518-cc98f28a-187c-4db6-ad09-c6b5e6057ee3.gif)  


## 목차

1. 팀 소개
2. 기획 의도
3. 주요기능
4. 기술 스택
5. 프로젝트 구조
6. 관련 링크

## 팀 소개

### KDJJ.work()

| <a href="https://github.com/svclaw2000"><img src="https://avatars.githubusercontent.com/u/46339857?v=4" width=100/><br><center>K018\_박규훤</center></a> | <a href="https://github.com/dongkey1198"><img src="https://avatars.githubusercontent.com/u/51209390?v=4" width=100/><br><center>K002\_김동현</center></a> | <a href="https://github.com/azzyjk"><img src="https://avatars.githubusercontent.com/u/56161518?v=4" width=100/><br><center>K055\_정준원</center></a> | <a href="https://github.com/Jeong-heon2"><img src="https://avatars.githubusercontent.com/u/55446114?v=4" width=100/><br><center>K059\_최정헌</center></a> |
| :------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------: |

## 기획 의도

요리할 때마다 자신이 만들 요리의 레시피를 항상 검색해야 합니다.    
요리를 할때도 초보자라면 시간을 놓치는 일이 잦습니다.  
이 앱은 레시피들을 관리하고 시간을 알려주는 기능을 통해 요리를 도와줍니다.

## 기슬 스택

![Untitled](https://user-images.githubusercontent.com/56161518/144422866-9c9a323c-d1a3-4ee4-9723-c387baed4889.png)

## 프로젝트 구조
### Multi Module & Clean Architecture
```
├── app
│   ├── app
│   └── di 
│       ├── data 
│       └── domain
│
├── data
│   ├── datasource(Interface)
│   ├── di
│   └── repository(Impl)
│
├── domain
│   ├── common
│   ├── di
│   ├── model
│   │   ├── exception
│   │   └── request
│   ├── repository(Interface)
│   └── usecase
│
├── presentation
│   ├── common
│   ├── di
│   ├── model
│   ├── services
│   ├── view
│   │   ├── adapter
│   │   ├── bindingadapter
│   │   ├── dialog
│   │   ├── home
│   │   │   ├── my
│   │   │   ├── others
│   │   │   └── search
│   │   ├── recipedetail
│   │   ├── recipeeditor
│   │   ├── recipesummary
│   │   └── splash
│   └── viewmodel
│   │   ├── home
│   │   │   ├── my
│   │   │   ├── others
│   │   │   └── search
│   │   ├── recipedetail
│   │   ├── recipeeditor
│   │   └── recipesummary
│
├── local
│   ├── dao
│   ├── dataSource(Impl)
│   ├── database
│   ├── di
│   └── dto
│
└── remote
    ├── common
    ├── datasource(Impl)
    ├── di
    ├── dto
    └── service
```

## 주요 기능

**나만의 레시피를 은밀하게 저장**

> 직접 레시피를 어플에 저장하여 나만의 레시피 모음zip을 만들 수 있습니다.

| 레시피 저장                                                                                                       |
|----------------------------------------------------------------------------------------------------------------|
| ![number1](https://user-images.githubusercontent.com/56161518/144392480-da5e2a42-0721-47f2-b7b3-b51b6629288f.gif) |

**은밀한 내 레시피 공유하기**

> 레시피를 다른 사람들에게 공유할 수 있습니다.

| 레시피 공유                                                                                                             |
|----------------------------------------------------------------------------------------------------------------------|
| ![number2 (1)](https://user-images.githubusercontent.com/56161518/144409029-484856b1-f355-425f-88d7-4006e8addaf4.gif) |

**원하는 레시피는 바로 검색**

> 검색 기능을 통해 원하는 레시피를 찾을 수 있습니다.

| 레시피 검색                                                                                                        |
|-------------------------------------------------------------------------------------------------------------------|
| ![number4 (1)](https://user-images.githubusercontent.com/56161518/144749983-527c3b3d-7a55-42af-aabd-ecd1852e9d39.gif) |

**레시피 슬쩍 훔쳐오기**

> 다른 사용자들이 공유한 레시피를 다운받아 나만의 레시피에 추가할 수 있습니다.

| 레시피 훔쳐오기                                                                                                        |
|-------------------------------------------------------------------------------------------------------------------|
| ![number6](https://user-images.githubusercontent.com/56161518/144393142-82097fa9-e415-4c3b-a191-7e321909a9b7.gif) |

**다른 사람들의 레시피 힐끔보기**

> 다른 사용자가 공유한 레시피들을 확인해 볼 수 있습니다.

| 레시피 힐끔보기                                                                                                          | 
|-------------------------------------------------------------------------------------------------------------------|
| ![number7](https://user-images.githubusercontent.com/56161518/144393149-2a481b6f-fcd4-4b43-917b-f7d1cb570b9e.gif) | !

**타이머 기능**

> 레시피에 조리 단계별로 타이머를 등록하고 레시피를 볼 때 사용할 수 있습니다.

| 레시피 타이머                                                                                                         |
|-----------------------------------------------------------------------------------------------------------------|
| ![number3](https://user-images.githubusercontent.com/56161518/144393153-26a44a3a-a389-4f96-b538-1a9be7ad1bd0.gif) |

## 프로젝트 관리

### Github 이슈를 통한 작업 관리

![스크린샷 2021-12-02 오후 11 53 46](https://user-images.githubusercontent.com/56161518/144445879-717ee670-0531-4fdf-b704-40e66b768581.png)    
![스크린샷 2021-12-02 오후 11 57 10](https://user-images.githubusercontent.com/56161518/144446408-f38ab240-2363-4054-8083-c19cbaf488b5.png)

### Slack Github bot을 이용한 리뷰 및 PR 상태 확인

![스크린샷 2021-12-02 오후 10 12 33](https://user-images.githubusercontent.com/56161518/144445491-1a0fc55c-20ee-4f7f-96ef-0c764e3483e1.png)

### Template를 이용한 PR 메세지 관리

![스크린샷 2021-12-03 오전 12 00 15](https://user-images.githubusercontent.com/56161518/144447025-ccbad611-e5e8-4492-a592-b5cf38cef02a.png)

### Github Action을 통한 build 실패 및 테스트 코드 실패 방지

![스크린샷 2021-12-03 오전 12 04 37](https://user-images.githubusercontent.com/56161518/144447728-98dbea0f-7df0-44f4-a6a6-7dd110937040.png)

## 관련 링크

### [프로토타입 / C4 모델](https://www.figma.com/file/cHfiiAwilyKdbPcO7KgWmu/%EB%82%98%EB%A7%8C%EC%9D%98-%EC%9D%80%EB%B0%80%ED%95%9C-%EB%9D%BC%EB%94%B0%EB%9A%9C%EC%9D%B4%E2%9D%A4?node-id=64%3A9879)

### [그라운드 룰](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Ground-Rules)

### [컨벤션](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Convention)

### [앱 배포 로그](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/%EC%95%B1-%EB%B0%B0%ED%8F%AC-%EB%A1%9C%EA%B7%B8)




