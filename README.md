# 🍝 나만의 은밀한 라따뚜이

> refer. 만개의 레시피, 쿠킹마마, 쿠팡

## 🍰 일해라 규동준정! - KDJJ.work()

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

## 🍖 주요 기능

**❤ 나만의 레시피를 은밀하게 저장**

> 직접 레시피를 어플에 저장하여 나만의 레시피 모음zip을 만들 수 있다.

**🧡 은밀한 내 레시피 공유하기**

> 레시피를 다른 사람들에게 공유할 수 있습니다.

**💛 레시피 슬쩍 훔쳐오기**

> 다른 사용자들이 공유한 레시피를 다운받아 나만의 레시피에 추가할 수 있습니다.

**💚 다른 사람들 레시피 힐끔보기**

> 다른 사용자들이 공유한 레시피들을 확인해볼 수 있습니다.

**💙 띠링~ 다음 단계 진행해주세요~(타이머 기능)**

> 레시피에 조리 단계별로 타이머를 등록하고 레시피를 볼 때 사용할 수 있습니다.

## 🌮 관련 링크

### [💜 프로토타입 / C4 모델](https://www.figma.com/file/cHfiiAwilyKdbPcO7KgWmu/%EB%82%98%EB%A7%8C%EC%9D%98-%EC%9D%80%EB%B0%80%ED%95%9C-%EB%9D%BC%EB%94%B0%EB%9A%9C%EC%9D%B4%E2%9D%A4?node-id=64%3A9879)

### [🤎 백로그](https://docs.google.com/spreadsheets/d/1vEL1eakho71AsXfejBZ_s8rf3ol1djSEHNhL8YVzziU/edit?usp=sharing)

### [🖤 그라운드 룰](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Ground-Rules)

### [🤍 컨벤션](https://github.com/boostcampwm-2021/Android08-Ratatouille/wiki/Convention)

## 프로젝트 배포 로그

### V1.1

- 레시피 추가 페이지 동작 구현

  - 드래그와 스와이프로 단계 순서 변경 및 제거
  - 레시피 등록하기 전 유효성 검사
  - 레시피 저장 구현
  - 저장 후 팝업 알림 구현

- 내 레시피 페이지 UI

- 힐끔보기 페이지 UI

## 프로젝트 구조
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
