* ### StoreDBLambda 
  * AWS IoT rule으로부터 수신된 쓰레기통 A의 비율 정보를 AWS DynamoDB TrashCanA table에 저장,
  * 중앙 아두이노에게 "Publish_To_B" 라는 주제로 쓰레기통 A의 비율 정보 게시 (MQTT 프로토콜),
  * IoT rule으로부터 수신된 RFID uid 태그 값을 가지고 Cleaner table 탐색 및 일치 여부 확인하여
  * 중앙 아두이노에게 "checkRFID"라는 주제로 true or false 값 전송 (MQTT 프로토콜)
* ### distance_app_test
  * 중앙 아두이노 -> Message Broker -> Device Shadow -> IoT Rule에 수신된 A 쓰레기통의 비율이 50 or 70 or 100%일때
  * 환경 미화원분에게 이메일로 알림을 전송하는 함수 (AWS SNS 서비스 이용)
* ### LoggingDeviceLamda (앱 로그조회 시 활용) 
  * AWS DynamoDB "TrashCanA" 테이블에서 deviceId, from, to 에 해당하는 로그들을 가져와서 띄워주는 함수
  * 또한 AWS API Gateway 서비스와 연동하여 GET 메서드의 통합 요청을 LogDeviceLambda로 설정함 (사용자가 앱을 통해 버튼을 누르면 해당 api url이 넘어가는 구조)
  * 프록시 통합은 비프록시 통합 으로 설정하여 사용자가 보내는 http request를 Lambda 함수가 인식할 수 있는 형태로 받음. (템플릿에서 설정)
  * #### 템플릿 설정은 다음과 같다.
    ```
    {
     "device": "$input.params('device')",
     "from": "$input.params('from')",
     "to":  "$input.params('to')"
    }
    ```
  * 위와 같이 템플릿에서 람다 함수가 받는 인수들을 따로 지정함으로써(비프록시 통합) 쓸데없는 http 헤더, 메소드 등 없이 람다가 원하는 값만 받아올 수 있음.
  * api URL : https://z7oyy1oz62.execute-api.ap-northeast-2.amazonaws.com/trashCan/trashCanA/log?from=2023-12-05%2013:26:00&to=2023-12-12%2013:27:00
    * 위 url은 2023/12/5 13시 26분부터 2023/12/12 13시 27분까지의 디바이스 id 가 trashCanA인 로그들을 모두 가져온것 : 클릭하면 확인가능함. (람다 함수 삭제로 동작X)
  * 앱이나 웹에서 API를 이용하기 위해서는 CORS 활성화가 필요함
    * CORS 활성화(Cross-Origin Resource Sharing)는 다른 서버의 리소스를 직접적으로 이용하고자 하는 경우 활성화 해주어야 AWS DB의 내용들을 가져올 수 있음
    * CORS 활성화 버튼만으로 간단하게 설정 가능함
  * 앱에서 활용시에는 사용자가 조회 날짜와 시간을 선택하여 버튼을 누르면 위 API url을 넘겨주도록 설정함.

    
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/fb3e4f0f-eee9-44aa-990b-2f1f0a372af5"></image>
### API Gateway에서 GET 메소드 생성 및 람다 함수 연결한 과정
<image src="https://github.com/pbzz1/Hansung-trashcan/assets/123307856/8a628993-6a96-4c84-a0f9-cccd11250905"></image>
### 리소스 및 템플릿 설정, CORS 활성화 마치고 API 배포한 모습 

