### 1. 앱 주요 기능
  * 포화 상태 로그 조회 - 쓰레기통 뚜껑 언저리에 설치한 초음파 센서 측정 값을 통해 특정 시간대의 포화 상태를 로그 조회
  * 차트 보기 - 특정시간대의 포화 상태를 그래프로 조회 (가로축 : 시간, 세로 축 : 비율(%) 값)
### 2. 폴더별 기능 
   * #### httpconnection 
      * GetRequest - 쓰레기통 포화 상태 로그 조회시 http response를 받아오는 역할을 수행합니다.
   * #### ui
     * #### apicall 
       * GetLog - 포화상태 로그 조회시에 디바이스의 로그들을 ArrayList로 받아오는 역할을 수행합니다.
     * ChartActivity - 사용자가 쓰레기통 A, B 버튼 클릭시 해당 정보에 따라 차트를 그리는 역할을 수행합니다.  
     * LogActivity - 쓰레기통 A, B의 로그들을 사용자 클릭 이벤트에 따라 띄워주는 역할을 수행합니다. 
     * MainActivity - 메인 화면으로 포화 상태 로그 조회, 차트 보기 버튼을 띄우는 역할을 수행합니다.
### 3. 활용 라이브러리 및 특징 
  * 차트 라이브러리 : MPAndroidChart 라이브러리 이용
  * 필수 사항 : build.gradle(:app)에 추가 
    ```
    repositories {
      maven { url 'https://jitpack.io' }
    }
    dependencies {
      //chart
      implementation 'com.github.PhilJay:MPAndroidChart:v3.1.0'
    }
    ```
    * 앱 특징
      * 로그 조회 or 차트 조회 버튼 누르면 조회 초기 상태는 A 쓰레기통의 1970년 1월 1일 ~ 현재까지의 값들(Table의 모든 값들)을 보여줌
      * 날짜 선택은 사용자가 용이하게 할 수 있도록 조회 시작 날짜는 현재 날짜 - 1, 종료 날짜는 현재 날짜로 세팅됨
      * 시간 선택시에도 마찬가지로 현재 시간으로 기본적으로 세팅됨
      * 쓰레기통 A, B의 로그를 각각 조회할 수 있도록 화면을 넘겨줄 때, 인텐트로 url을 넘겨주고 사용자 선택에 따라 /trashCanA or /trashCanB가 URL에 추가됨
      * URL이 계속해서 쌓이는 것을 방지하기 위해 String tempStr에 이전 url 저장, 다시 복귀하는 식으로 구성함 (LogActivity.java 코드)
      * 로그 조회시에는 LogDeviceLambda 함수를 이용하여 배포한 API URL 을 버튼 클릭시 넘겨주어서 http request를 요청하는 방식
      * url 예시 : https://z7oyy1oz62.execute-api.ap-northeast-2.amazonaws.com/trashCan/trashCanA/log?from=2023-12-05%2013:26:00&to=2023-12-12%2013:27:00
### 4. 앱 시연 영상
#### https://youtu.be/UvjXEWUmIRI 
### 5. 앱 화면 구성
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/18da7c95-70b2-47f2-8e69-d14886a51c05"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/f6132264-b535-4200-ab7a-a3c2db9c200d"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/b33901fe-5563-46ac-adb8-8ba2ca2cc8ef"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/03c5b347-4b53-4126-b2e8-6b2407d84080"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/205e884a-4957-467a-b8ea-529afb9e8802"></image>
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/95768024-d2a9-4043-bae4-bbd47cf85188"></image>
