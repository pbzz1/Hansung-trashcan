# Hansung-trashcan
### 프로젝트 주제 : [환경 미화원분들을 위한 쓰레기통 모니터링 시스템]
### 시연 영상 링크 https://youtu.be/oYp2sEuCdz0
#### 각 폴더별 README 파일에 기술 설명이 기재되어 있습니다.
### 1. 프로젝트 개요
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/81f01824-16c0-4dc4-808d-6763a7152926" weight="600" height="400"></image>
### 2. 시스템 구조도
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/24537809-079b-4d76-815e-33d37b517aff" weight="600" height="400"></image>
### 3. 핵심 기능
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/c34f3be8-de72-4a3f-931e-1afc5aff480f" weight="600" height="400"></image>
### + 앱을 통해 특정기간대의 쓰레기통 로그를 조회 가능하며 차트 보기 버튼을 통해 포화 상태를 시간대별로 한눈에 파악 가능함
### 4. 외형 및 HW 전체 구성
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/f715f307-a39a-42eb-9c02-2b5c904e3350" weight="600" height="400"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/9b3c5223-efdb-4fe5-9ee6-b4b73b0c9eab" weight="600" height="400"></image>
### 5. S/W 구성
* ### AWS IoT : 중앙 아두이노 사물 등록 및 디바이스 인증, MQTT 테스트에 활용, IoT 규칙을 설정하여 람다 함수에 Trigger
* ### AWS Lambda : DB에 저장, MQTT 주제 게시, 인증된 RFID 태그인지 검사, 이메일 전송 등 각종 AWS 서비스와 연동하는 함수 배포
* ### AWS SNS : 주제 및 구독 생성을 통해 쓰레기통 비율이 50, 70, 100%일 때 이메일을 전송
* ### AWS DynamoDB : 미화원들의 RFID 태그값들, 쓰레기통 A의 ratio 값들을 각각 Cleaner, trashCanA 테이블에 저장
* ### AWS API Gateway : 클라이언트(앱)에서 자유롭게 포화 상태를 조회할 수 있도록 LogDeviceLambda 함수를 GET 메서드로 통합 요청하는 API 생성 및 배포
* ### Android Studio : 사용자(미화원)가 포화 상태를 조회하고 차트를 볼 수 있는 인터페이스 역할
  * #### 아래 사진은 앱 화면입니다.
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/18da7c95-70b2-47f2-8e69-d14886a51c05"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/b33901fe-5563-46ac-adb8-8ba2ca2cc8ef"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/95768024-d2a9-4043-bae4-bbd47cf85188"></image>
     


     
