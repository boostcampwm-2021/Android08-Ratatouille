# 🍝 나만의 은밀한 라따뚜이

![ezgif-3-410d82ddc14b](https://user-images.githubusercontent.com/56161518/144391518-cc98f28a-187c-4db6-ad09-c6b5e6057ee3.gif)  
안녕!⭐️ 나는 요리를 도와주는 라따뚜이라고해!  
뭐어어~~? 어제 만든 요리가 맛있었는데 기억나지 않는다고?!?!?  
뭐?!?!? 요리를 만들다가 시간을 놓쳐서 태웠다고??!?!  
괜찮아 내가 도와줄게!⭐️  
너가 저장하고 싶은 요리를 너만 볼 수 있는 ⭐시⭐크⭐릿⭐ 한 공간에 저장하고 도움을 받아봐!

## 목차

1. 🍰 팀 소개
2. 🥩 기획 의도
3. 🍖 주요기능
4. 👨‍💻 기술 스택
5. 🍮 프로젝트 구조
6. 🌮 관련 링크

## 🍰 팀 소개

얼른 코딩하지 못해..?  
일해라 규동준정! - KDJJ.work()

| <a href="https://github.com/svclaw2000"><img src="https://avatars.githubusercontent.com/u/46339857?v=4" width=100/><br><center>K018\_박규훤</center></a> | <a href="https://github.com/dongkey1198"><img src="https://avatars.githubusercontent.com/u/51209390?v=4" width=100/><br><center>K002\_김동현</center></a> | <a href="https://github.com/azzyjk"><img src="https://avatars.githubusercontent.com/u/56161518?v=4" width=100/><br><center>K055\_정준원</center></a> | <a href="https://github.com/Jeong-heon2"><img src="https://avatars.githubusercontent.com/u/55446114?v=4" width=100/><br><center>K059\_최정헌</center></a> |
| :------------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------: | :--------------------------------------------------------------------------------------------------------------------------------------------------: | :-------------------------------------------------------------------------------------------------------------------------------------------------------: |

## 🥩 기획 의도

👨라따뚜이: 혼자 요리할때마다 레시피 찾아보고 하는거 너무 귀찮지않아?  
👧스테파니: 응 맞아...요리할때마다 너무 힘들어

👨라따뚜이: 너두 나처럼 나만의 요리방법을 모아 나만의 레시피 리스트를 만들어봐!!!  
👧스테파니: 응? 그건 어떻게 하는거야???

👨라따뚜이: 너 **_나만의 은밀한 라따뚜이_** 못 들어봤어??  
👧스테파니: 어멋!! 그건 뭐야?

👨라따뚜이: 메모장에 있는 레시피를 깔끔하게 정리하고!! 레시피와 타이머를 동시에 보며 요리하면 너두 5성급 호텔 주방장처럼 요리 할수 있어!!!  
👧스테파니: 세상에!!!

👨라따뚜이: 그뿐만이 아니야!! 다른 사람들이 공유한 레시피도 확인할수 있다구!!  
👧스테파니: 맙소사!!!!

👧스테파니: 나도 나만의 요리 지침서를 만들거야!!  
👨라따뚜이: 하하하하하

## 👨‍💻 기슬 스택

![Untitled](https://user-images.githubusercontent.com/56161518/144422866-9c9a323c-d1a3-4ee4-9723-c387baed4889.png)

## 🍮 프로젝트 구조
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

## 🍖 주요 기능

**❤ 나만의 레시피를 은밀하게 저장**

> 이 요리는 레시피를 기억하고 나중에도 먹어야해...!  
> 내 마음속에 저장⭐️

| 레시피 저장 💾                                                                                                         |
|-------------------------------------------------------------------------------------------------------------------|
| ![number1](https://user-images.githubusercontent.com/56161518/144392480-da5e2a42-0721-47f2-b7b3-b51b6629288f.gif) |

**🧡 은밀한 내 레시피 공유하기 U///U**

> 앗 이 레시피로 만든 음식 너무 맛있는데...?  
> 나혼자 알기 너무 아까워!!!  
> 이 레시피는 많은 사람들이 알아야 해!

| 레시피 공유 👥                                                                                                             |
|-----------------------------------------------------------------------------------------------------------------------|
| ![number2 (1)](https://user-images.githubusercontent.com/56161518/144409029-484856b1-f355-425f-88d7-4006e8addaf4.gif) |

**💚 다른 사람들의 레시피 힐끔보기**

> 라따뚜이에는 항상 맛있는 레시피들이 올라온단 말이지...  
> 오늘은 어떤 레시피들이 있는지 슬쩍 봐볼까..?

| 레시피 힐끔보기 🧐                                                                                                         | 레시피 검색 🔍                                                                                                       |
|-------------------------------------------------------------------------------------------------------------------|-------------------------------------------------------------------------------------------------------------------|
| ![number7](https://user-images.githubusercontent.com/56161518/144393149-2a481b6f-fcd4-4b43-917b-f7d1cb570b9e.gif) | ![number4](https://user-images.githubusercontent.com/56161518/144393134-3b24467a-be29-4ff5-8cb0-f53150997716.gif) |

**💛 레시피 훔쳐오기**

> 뭐..? 저 딸기 빙수🍧가 그렇게 맛있다는 소문이 있던데...  
> 레시피를 훔쳐와서 내 입맛에 맞게 커스텀 한 후 만들어 봐야겠어..!

| 레시피 훔쳐오기 🥷                                                                                                       |
|-------------------------------------------------------------------------------------------------------------------|
| ![number6](https://user-images.githubusercontent.com/56161518/144393142-82097fa9-e415-4c3b-a191-7e321909a9b7.gif) |

**💙 띠링~ 다음 단계 진행해주세요~(타이머 기능)**

> 요리를 공식이야! 시간과 계량을 철저히 해야돼!    
> 라따뚜이를 이용해서 시간을 1초도 틀리지 않고 이용해야겠어!

| 레시피 타이머 ⏱                                                                                                         |
|-------------------------------------------------------------------------------------------------------------------|
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

## 🌮 관련 링크

### [💜 프로토타입 / C4 모델](https://www.figma.com/file/cHfiiAwilyKdbPcO7KgWmu/%EB%82%98%EB%A7%8C%EC%9D%98-%EC%9D%80%EB%B0%80%ED%95%9C-%EB%9D%BC%EB%94%B0%EB%9A%9C%EC%9D%B4%E2%9D%A4?node-id=64%3A9879)

### [🖤 그라운드 룰](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Ground-Rules)

### [🤍 컨벤션](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Convention)

### [🤎 앱 배포 로그](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/%EC%95%B1-%EB%B0%B0%ED%8F%AC-%EB%A1%9C%EA%B7%B8)




