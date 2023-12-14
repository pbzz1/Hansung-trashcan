# StoreDBLambda
#### AWS IoT rule으로부터 수신된 쓰레기통 A의 비율 정보를 AWS DynamoDB TrashCanA table에 저장,
#### 중앙 아두이노에게 "Publish_To_B" 라는 주제로 쓰레기통 A의 비율 정보 게시 (MQTT 프로토콜),
#### IoT rule으로부터 수신된 RFID uid 태그 값을 가지고 Cleaner table 탐색 및 일치 여부 확인하여
#### 중앙 아두이노에게 "checkRFID"라는 주제로 true or false 값 전송 (MQTT 프로토콜)
