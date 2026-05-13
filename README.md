# 해봄
### 스스로 하는 힘을 만드는 습관 관리 앱
<img width="800" alt="그래픽 이미지" src="https://github.com/user-attachments/assets/9de7016a-b3b6-4e0d-a13c-f5ec0bb7cd18" />
<br>
<a href="https://play.google.com/store/apps/details?id=com.swyp.firsttodo">
  <img src="https://github.com/user-attachments/assets/909af0b9-2256-4441-9c3d-2f740b7767cd" width="150" alt="Get it on Google Play" />
</a>

> 개발 기간

- 26년 3월 한 달 간 개발해 MVP 완성, 4/12 출시
- 현재 플레이스토어 운영 중

> 팀 구성

- **Android:** 1명
- **Product Design:** 3명
- **Product Manager:** 2명
- **Server:** 3명
---

# <img width="32" alt="앱아이콘" src="https://github.com/user-attachments/assets/165184ca-063b-4c08-b463-df0024f3d0b2" /> 서비스 소개 및 시연 영상
https://github.com/user-attachments/assets/47b2d086-a584-4eef-a664-fb0c914c6c32

---

# <img width="32" alt="앱아이콘" src="https://github.com/user-attachments/assets/165184ca-063b-4c08-b463-df0024f3d0b2" /> 주요기능
<img width="800" alt="스위프앱4기_7팀_발표최종-이미지-18" src="https://github.com/user-attachments/assets/5b445aad-b1fd-4e79-9998-77cf7e1c74ee" />

---

# 🏛️ Architecture & Design Pattern
본 프로젝트는 **Clean Architecture**와 **UDF 기반 MVVM**을 준수합니다.

### 1. Presentation Layer (UDF)
- **State 중심 UI:** `UiState`를 통해 화면의 모든 상태를 단일 객체로 관리합니다.
- **Side Effect 처리:** `Channel`을 활용해 토스트, 네비게이션 등 일회성 이벤트를 보장합니다.
- **Async Wrapper:** 비동기 데이터의 상태(Init, Loading, Success, Failure)를 타입 안정적으로 관리합니다.

### 2. Data Layer (Robust Error Handling)
- **SafeApiCall:** HTTP 상태 코드와 서버 커스텀 에러 코드를 분리하여 예외 상황을 세밀하게 제어합니다.
- **Result Pattern:** Repository 수준에서 `Result<T>`를 반환하여 데이터 흐름의 명확성을 높였습니다.

---

# 🛠️ Tech Stack
| 카테고리 | 기술/라이브러리 |
| --- | --- |
| Language | Kotlin |
| UI | Jetpack Compose (Material3) |
| Architecture | Clean Architecture |
| Module | Single Module |
| Pattern | MVVM |
| DI | Hilt |
| Async | Coroutines & Flow |
| Network | Retrofit2 + OkHttp |
| Serialization | Kotlinx Serialization |
| Local DB | DataStore (Preferences) |
| Navigation | Compose Navigation |

---

# 📃 Convention & Docs
- [Code Convention](https://endurable-turkey-601.notion.site/Code-Convention-310b6f38dd6581478eaffdb3d1079816?source=copy_link)
- [Git Convention](https://endurable-turkey-601.notion.site/Git-Convention-310b6f38dd6581d29e60f7a14651bf15?source=copy_link)
- [Presentation Rule](https://endurable-turkey-601.notion.site/Presentation-Rule-312b6f38dd65808a878ddea36ecfd798?source=copy_link)
- [Api Rule](https://endurable-turkey-601.notion.site/313b6f38dd65805ca3e5d5b9b9320795?source=copy_link)
- [Troubleshooting](https://endurable-turkey-601.notion.site/351b6f38dd658039bdb1c068b74b77b7?v=351b6f38dd6581ec8461000c7f47b786&source=copy_link)
