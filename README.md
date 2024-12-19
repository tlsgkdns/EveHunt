# EVEHUNT
## 개요
- 프로젝트 소개: 이벤트 열 수 있는 사이트
- 프로젝트 계기: 각종 커뮤니티에서 간단하게 이벤트를 여는 경우가 있다. 이러한 이벤트를 간편하게 열고 참여할 수 있는 사이트가 있으면 좋겠다고 생각을 하여 사이트를 만들었다.
- 프로젝트 기간: 2024.06 ~ 2024.08
- 프로젝트 인원: 1인
- 프론트엔드 링크: [EveHuntVue](https://github.com/tlsgkdns/EveHuntVue)
## 개발 환경
* OS: <img src="https://img.shields.io/badge/window 10-0078D6?style=for-the-badge&logo=window10&logoColor=white">
* IDE: <img src="https://img.shields.io/badge/intellij 2023.3.1-000000?style=for-the-badge&logo=intellijidea&logoColor=white">
* 실행 브라우저: <img src="https://img.shields.io/badge/chrome-4285f4?style=for-the-badge&logo=googlechrome&logoColor=white">
## ERD
<img src="https://github.com/user-attachments/assets/70e86498-4747-4a86-8c65-d8a25bd8a1a3">

## 기술 스택
- 프론트엔드: <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=black"><img src="https://img.shields.io/badge/Css-1572B6?style=for-the-badge&logo=css&logoColor=white">
- 백엔드: <img src="https://img.shields.io/badge/SpringBoot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=black"><img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">
- 데이터베이스: <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white"><img src="https://img.shields.io/badge/H2-2439A1?style=for-the-badge&logoColor=white">
- 성능 테스트: <img src="https://img.shields.io/badge/nGrinder-ECD53F?style=for-the-badge&logo=ngrinder&logoColor=white">
- ORM: <img src="https://img.shields.io/badge/JPA-F37C20?style=for-the-badge&logoColor=white">
### 주요 사용 이유
- <img src="https://img.shields.io/badge/vue.js-4FC08D?style=for-the-badge&logo=vue.js&logoColor=black">: Vue.js가 러닝커브가 낮았기 때문에 이것을 선택하였다.
- <img src="https://img.shields.io/badge/kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white">: Java보다 data 클래스, let 등의 편의 기능을 제공하기에 사용하기 더 편리해서 사용하였다.
- <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white">: 무료로 제공되고, 빠르고 안정적이기에 사용하였다.
- <img src="https://img.shields.io/badge/H2-2439A1?style=for-the-badge&logoColor=white">: 스트 코드의 데이터베이스를 위해 간단하게 사용할 수 있어서 사용히였다.
- <img src="https://img.shields.io/badge/Redis-FF4438?style=for-the-badge&logo=redis&logoColor=white">: 동시성 문제를 해결하기 위한 분산락을 도입하였다. 초기엔 비싼 리소스였기에 Named Lock을 사용했다. 하지만, 테스트 코드 작성 때, H2 데이터베이스 사용으로 인해 Named Lock 사용이 어려울 것이라 느껴져서, Redis로 전환했다.
## 기능
- Spring Security를 사용한 회원 기능: 손쉬운 회원가입/로그인 기능의 구현이 가능했고, JWT를 이용해 보안성을 향상했다.
- QueryDSL을 활용한 검색 기능: 제목, 개최자, 태그, 종료일 등 여러 기준으로의 키워드와 비슷한 기준으로의 정렬 순서를 정해서 검색하여, 여러 이벤트에 접근성을 향상 시켰다.
- 이벤트 개최 기능: 이벤트 개최시, 제목, 상세, 태그, 종료일, 사진 등을 설정할 수 있다. Scheduler를 사용해서 종료일이 지날 시 자동적으로 이벤트가 종료하도록 설정하여 개최자를 편의성을 높였다.
- 이벤트 참여 기능: 분산 락을 도입해 정확한 참여자 카운팅을 지원해, 일관성 있는 이벤트 참여를 제공한다.
- 신고 기능: 권한 기능을 통해 Admin만이 접근할 수 있는 페이지 도입. 신고가 들어오면, 이 페이지를 통해 신고를 처리해서 이벤트 개최 정지 기능 지원해 양질의 이벤트를 제공한다. Scheduler로 자동적으로 정지를 해제해 admin의 편의성을 높였다.
- 당첨자 추첨 기능:  랜덤 추첨 기능을 지해 주최자의 편의성을 고려하였고 당첨 결과는 이메일로 발송 가능하다. 당첨자마다의 메시지 수정 기능으로 서드 파티 도움 없이 편리하게 유저의 고유성 부여할 수 있다.
- 인기 이벤트 / 태그 캐싱: 자주 사용된 태그와 참여된 이벤트를 Home 화면에 보여주고, 캐싱 기능을 통한 빠른 접근으로 서버의 부담감을 줄였다.
## 트러블 슈팅
### 페이징 성능 향상
- 성능 테스트 도중, 단순히 이벤트를 페이징하는 메소드가 7.4 TPS란 낮은 성능을 보였다.
<img src="https://github.com/user-attachments/assets/f94505d6-4c13-44aa-a1cd-9d293e914f38">
- 쿼리 최적화가 필요하단 결론을 내린 후, 쿼리 최적화를 공부하고, 필요한 칼럼만 가져오는 최적화를 적용하였다.
- 빈 데이터베이스로 바꾸고, VUser 148, 데이터 4000개로 1페이지 테스트 결과는 다음과 같다.
<br><img src="https://github.com/user-attachments/assets/0248cad2-813d-4c96-86db-9e60507d7692">
<br><img src="https://github.com/user-attachments/assets/7a2bb7f5-0ec5-44bf-a106-b7f4c413cc96">
- 최적화 적용 전 2806.3 TPS, 적용 후 3285.8 TPS로 약 13.8%의 성능 향상을 보였고, 그래프는 더욱 안정적인 향상을 보였다.
<br><img src="https://github.com/user-attachments/assets/d89756a5-46a2-417b-b8c5-abc96cab4616">
- 기존 데이터베이스로 다시 바꾸고, 테스트한 결과 99.6 TPS로 일대일 비교는 어렵지만, 훨씬 높은 성능을 보였기에 이 해결책을 택했다.

### 메일 서비스 오류
통합 테스트 도중, 회원가입과 이벤트 결과 처리 등에서 갑작스런 성능 저하로 인해 페이지가 늦게 리다이렉트 되는 것을 확인하였다. 
확인 결과, 메일 서비스가 오래 걸려서 다음 행동을 취할 수 없다란 결론을 내렸다.
이에 메일 서비스를 비동기로 수정해서 사용자에게 아직 메일을 보내지 않더라도 서비스를 이용할 수 있도록 수정했다.

<img src="https://github.com/user-attachments/assets/21a5f5f1-d0e0-43f1-8b79-bee108a5b137">

하지만 다른 문제가 발생하였는데, 10명 이상의 인원에게 메일을 보낼때 오류가 발생했다. 
추론 결과, 너무 많은 스레드가 구글 메일에 접근해서 발생한 오류로 생각했다. 그렇기에 나는 다음과 같은 시도를 하였다.
1. 비동기 파라미터 수정
- 스레드 풀 수를 줄이고 태스크 대기 큐을 늘려서, 메일 서비스에 접근하는 스레드 수를 줄였다.
- 10명 남짓한 인원의 한계가 100명으로 늘어났지만 목표치인 1000명에 다다르지 못했다.
2. Lock 사용
- 메일을 보내는 메소드에 Lock 사용을 시도했다.
- Lock이 timeout 되는 등의 여러 오류가 발생해, 이 안을 포기했다.
3. Scheduler 활용
- 한 번에 메일을 보내서 문제가 발생한 것이라면, 메일을 나눠서 보내는 것이 어떨까란 생각으로 전환햇다.
- 데이터베이스에 Mail 테이블을 만들어서, 메일을 보내는 대신에, 이 테이블에 저장하고, Scheduler가 정기적으로 아직 보내지 않은 Mail들을 부분적으로 가져와서, 보내는 파라다임으로 수정하였다.
<img src="https://github.com/user-attachments/assets/db210d48-e72e-44e5-9722-5d4fb646d21a">
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