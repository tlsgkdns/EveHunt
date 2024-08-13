# EVEHUNT
## 개요
- 프로젝트 소개: 이벤트 열 수 있는 사이트
- 프로젝트 계기: 각종 커뮤니티에서 간단하게 이벤트를 여는 경우가 있다. 이러한 이벤트를 간편하게 열고 참여할 수 있는 사이트가 있으면 좋겠다고 생각을 하여 사이트를 만들었다.
- 프로젝트 기간: 2024.06.17 ~ 2024.08.02
- 프로젝트 인원: 1인
- 프론트엔드 링크: [EveHuntVue](https://github.com/tlsgkdns/EveHuntVue)
## 개발 환경
* OS: <img src="https://img.shields.io/badge/window 10-0078D6?style=for-the-badge&logo=window10&logoColor=white">
* IDE: <img src="https://img.shields.io/badge/intellij 2023.3.1-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
* 실행 브라우저: <img src="https://img.shields.io/badge/chrome-4285f4?style=for-the-badge&logo=googlechrome&logoColor=white">
## 기술 스택
- 프론트엔드: <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=black"><img src="https://img.shields.io/badge/Css-1572B6?style=for-the-badge&logo=css&logoColor=white">
- 백엔드: <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=black"><img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
- 데이터베이스: <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/H2-2439A1?style=for-the-badge&logoColor=white">
- 성능 테스트: <img src="https://img.shields.io/badge/nGrinder-ECD53F?style=for-the-badge&logo=ngrinder&logoColor=white">
- ORM: <img src="https://img.shields.io/badge/JPA-F37C20?style=for-the-badge&logoColor=white">
### 주요 사용 이유
- <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=black">: CSR 방식의 프론트엔드를 새롭게 배워야하는데, 자주 사용되는 React와 Vue.js 등이 후보가 되었다. 이 때, Vue.js가 러닝커브가 낮았기 때문에 이것을 선택하였다.
- <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">: Java와 Kotlin 둘다 사용 가능하지만, Kotlin이 Java보다 data 클래스, let 등의 편의 기능을 제공하기에 사용하기 더 편리해서 사용하였다.
- <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">: DBMS 중 자주 사용되고, 무료로 제공되기에 사용했다.
- <img src="https://img.shields.io/badge/H2-2439A1?style=for-the-badge&logoColor=white">: 테스트 코드의 데이터베이스를 위해서 사용
- <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">: 동시성 문제를 해결하기 위한 분산락을 도입하였다. 초기엔 MySQL의 Named Lock 사용으로 충분할 것이라 예상했고, 증거로 Cache도 Redis가 아닌 로컬 캐쉬인 Caffeine을 사용했다. 하지만, 테스트 코드 작성 때, H2 데이터베이스 사용으로 인해 Named Lock 사용이 어려울 것이라 느껴져서, Redis로 전환했다.
## 기능
- Spring Security를 사용한 회원 기능: 이메일을 통해 회원가입/로그인 가능, 프로필 사진도 설정할 수 있다.
- QueryDSL을 활용한 검색 기능: 제목, 개최자, 태그, 종료일 등 여러 기준으로의 키워드와 비슷한 기준으로의 정렬 순서를 정해서 검색할 수 있다.
- 이벤트 개최 기능: 이벤트 개최시, 제목, 상세, 태그, 종료일, 사진 등을 설정할 수 있다. Scheduler를 사용해서 종료일이 지날 시 이벤트가 종료하도록 설정하였다.
- 이벤트 참여 기능: 참여시, 추가 질문을 통해 당첨 기준을 정할 수 있다. 분산 락을 도입해 정확한 참여자 카운팅을 지원한다.
- 신고 기능: 권한 기능을 통해 Admin만이 접근할 수 있는 페이지 도입, 신고가 들어오면, 이 페이지를 통해 신고를 처리해서 이벤트 개최 정지 기능 지원. Scheduler로 정지 해제 가능
- 당첨자 추첨 기능: 당첨자에게는 이메일로 메시지를 보내며, 보낼 메시지는 수정할 수 있다. 추첨 인원을 입력해서, 랜덤 추첨 기능을 지원한다.
- 인기 이벤트 / 태그 캐싱: 자주 사용된 태그와 참여된 이벤트를 Home 화면에 보여주고, 캐싱 기능을 통해 빠른 접근이 가능하도록 했다.
## 트러블 슈팅
### 페이징 오류
- 성능 테스트 도중, 단순히 이벤트를 페이징하는 메소드가 7.4 TPS란 낮은 성능을 보였다.
<img src="https://github.com/user-attachments/assets/f94505d6-4c13-44aa-a1cd-9d293e914f38">

- @StopWatch 어노테이션을 AOP를 활용해 만들어 확인해본 결과, 이벤트 QueryDSL Repository 에서 단순 페이징에만 3초가 걸리는 문제를 발견했다.
- 먼저 떠오른 해결책은 캐싱(Caching)이였다. 페이징에 약간 복잡한 where절로 인해 성능이 낮아진게 아닌지 의문이 들었다. 캐싱을 적용하고, 확인을 한 결과 1668.2 TPS로 성능이 많이 개선되었지만, 캐싱은 근본책이 아니라고 여겨져서, 다른 해결책을 강구하였다.

<img src="https://github.com/user-attachments/assets/ced411ba-5929-4b2b-875f-a964ceac5494">

- 다음 해결책은 쿼리 최적화이다. 기술 면접 준비를 하며, 쿼리 최적화를 공부한 경험을 바탕으로 적용해 볼만한 사항을 고민하였고, 단순히 QueryDSL에서 selectFrom(event)를 사용하는 게 아니라, Projection을 사용해 필요한 정보만을 사용하도록 수정했다.
- 처음엔 개선에 다소 회의적이였으나, 99.6 TPS로 훨씬 높은 성능을 보였기에 이 안을 채택하였다.

<img src="https://github.com/user-attachments/assets/d89756a5-46a2-417b-b8c5-abc96cab4616">

### 메일 서비스 오류
통합 테스트 도중, 회원가입과 이벤트 결과 처리 등에서 갑작스런 성능 저하로 인해 페이지가 늦게 리다이렉트 되는 것을 확인하였다. 
nGrinder를 통한 성능 테스트 결과, 에러 발생으로 인해 테스트 불가한 것을 발견하였는데, 메일 서비스가 오래 걸려서 다음 행동을 취할 수 없다란 결론을 내렸다.
이에, 메일 서비스를 비동기로 수정해서, 사용자에게 아직 메일을 보내지 않더라도, 서비스를 이용할 수 있도록 수정했다.

<img src="https://github.com/user-attachments/assets/21a5f5f1-d0e0-43f1-8b79-bee108a5b137">

하지만 다른 문제가 발생하였는데, 이 프로젝트는 최대 1000명이 이벤트에 참여할 수 있도록 처리했는데,
인원 수 전원에게 이벤트 결과 메시지를 발송할 때, 1000명은 커녕 10명 남짓한 인원에게 메일을 보낼때 Authentication Failed 오류가 발생했다.
여기에, 테스트 코드를 수정을 하다보니, 
추론 결과, 구글 메일은 한번에 많은 메일을 보낼 수가 없어서 발생한 오류인 것으로 생각했다. 그렇기에 나는 다음과 같은 시도를 하였다.
1. 비동기 파라미터 수정
- 스레드 풀 수를 줄이고, 태스크 대기 큐을 늘려서, 메일 서비스에 접근하는 스레드 수를 줄였다.
- 10명 남짓한 인원의 한계가 100명으로 늘어났지만, 아무리 파라미터를 조정해도, 이 이상의 향상은 기대하기 어려웠다.
2. Lock 사용
- 메일을 보내는 메소드에 Lock 사용을 시도했다.
- 하지만, 다른 오류가 발생했고, Lock이 timeout 되는 경우도 생겨서, 바로 포기했다.
3. Scheduler 활용
- 한 번에 메일을 보내서 문제가 발생한 것이라면, 메일을 나눠서 보내는 것이 어떨까란 생각으로 전환햇다.
- 데이터베이스에 Mail 테이블을 만들어서, 메일을 보내는 대신에, 이 테이블에 저장하고, Scheduler가 정기적으로 아직 보내지 않은 Mail들을 부분적으로 가져와서, 보내는 파라다임으로 수정하였다.
<img src="https://github.com/user-attachments/assets/c6f85420-422a-436d-b6b7-280f17551477">
<img src="https://github.com/user-attachments/assets/2f54d5f3-a3ca-4187-baa5-c35f2d7ba0a1">
- 오류 없이 안정적으로 메일이 보내지는 것을 확인해서, 이 방법을 택하였다.

## 이 프로젝트를 통해 배운 것들
- Vue3를 배워서 사용해보았다.
- nGrinder 사용법을 익혔다.
- 메일을 보내는 법과, 비동기적으로 처리하는 법을 알게 되었다.
- Scheduler에 대해 배우고, 활용했다.
- QueryDSL을 더 잘 알게 되었다.
- 쿼리 최적화가 중요하다는 것을 깨달았다.
- 캐싱이 생각이상으로 빠르다는 장점을 알게 되었고, 캐싱 관리가 중요하다는 단점을 알게되었다.