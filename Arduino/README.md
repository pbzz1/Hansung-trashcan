### 1. 사용한 아두이노 부품
#### (1) 쓰레기통 A
- 아두이노 2개
- 브레드보드 2개
- LoRa 쉴드 1개
- 초음파 센서 1개
- 서보 모터 1개
- LCD 1개
- 가변 저항 1개
- RFID 리더기 1개 및 태그 총 4개(3개는 DynamoDB에 저장되어 인증된 것, 1개는 인증되지 않은 것)

#### (2) 쓰레기통 B
- 아두이노 1개
- 브레드보드 1개
- LoRa 쉴드 1개
- LCD 1개
- 가변저항 1개

#### (3) 중앙 아두이노
- ESP8266 D1 R1 우노보드(wifi) 1개
- LoRa 쉴드 1개

### 2. 아두이노 주요 기능
: 쓰레기통 A에서 쓰레기량을 측정해 쓰레기통에서도 볼 수 있게 LCD에 그 값을 띄우고 쓰레기량이 100%가 되면 서보모터를 움직여 쓰레기통 뚜껑이 닫히도록 하였다. 또, 로라 통신을 사용하여 쓰레기통 B에서 쓰레기통 A의 쓰레기량 값을 받아 LCD에 띄우는 작업을 통해 쓰레기통끼리의 통신도 구현하였다. 그 다음 AWS IoT와도 연결하여 쓰레기통 A에 AWS IoT DynamoDB에 저장되어 인증된 환경미화원 분의 카드가 태그되면, true, 아니면 false 값이 쓰레기통 A로 전달되도록 구현하였다.

### 3. 폴더별 기능
- Trashcan_A: 쓰레기통 A의 센서, 시리얼 통신, 로라 통신 코드
- Trashcan_B: 쓰레기통 B의 센서, 로라 통신 코드
- Center: 중앙 와이파이 모듈의 로라 통신, AWS IoT와의 통신 코드
 
