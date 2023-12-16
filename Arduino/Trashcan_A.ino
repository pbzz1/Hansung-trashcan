//초음파, RFID + SoftwareSerial + LoRa

#include <SoftwareSerial.h>

//로라 통신
#include "SNIPE.h"
#define ATSerial Serial

#define TXpin 4
#define RXpin 5

//16byte hex key
String lora_app_key = "11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff 00";
SNIPE SNIPE(ATSerial);

//RFID 라이브러리
#include <SPI.h>
#include <MFRC522.h>

//초음파 pin
#define trigPin 6
#define echoPin 7

#define RST_PIN 9 //RFID 리더의 RST 핀
#define SS_PIN 10 //RFID 리더의 SS(SSDA) 핀
/*
 * RST          9            
 * SDA(SS)      10 
 * MOSI         11
 * MISO         12
 * SCK          13 
 */

SoftwareSerial mySerial(RXpin,TXpin);

MFRC522 mfrc522(SS_PIN, RST_PIN);

String rfidValue="";

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);//시리얼 통신 시작
  mySerial.begin(2400); 
  while (!Serial);    // Do nothing if no serial port is opened (added for Arduinos based on ATMEGA32U4)
  ATSerial.begin(115200);
  while (ATSerial.read() >= 0) {}
  while (!ATSerial);
  //초음파 센서
  pinMode(trigPin, OUTPUT);
  pinMode(echoPin, INPUT);

  SPI.begin();        // Init SPI bus
  mfrc522.PCD_Init(SS_PIN, RST_PIN); // Init each MFRC522 card

   /* SNIPE LoRa Initialization */
  if (!SNIPE.lora_init())
  {
    Serial.println("SNIPE LoRa Initialization Fail!");
    while (1);
  }
  /* SNIPE LoRa Set AppKey */
  if (!SNIPE.lora_setAppKey(lora_app_key))
  {
    Serial.println("SNIPE LoRa app key value has not been changed");
  }
  /* SNIPE LoRa Set Frequency */
  if (!SNIPE.lora_setFreq(LORA_CH_1))
  {
    Serial.println("SNIPE LoRa Frequency value has not been changed");
  }
  /* SNIPE LoRa Set Spreading Factor */
  if (!SNIPE.lora_setSf(LORA_SF_7))
  {
    Serial.println("SNIPE LoRa Sf value has not been changed");
  }
  /* SNIPE LoRa Set Rx Timeout 
   * If you select LORA_SF_12, 
   * RX Timout use a value greater than 5000  
  */
  if (!SNIPE.lora_setRxtout(5000))
  {
    Serial.println("SNIPE LoRa Rx Timout value has not been changed");
  }
  Serial.println("SNIPE LoRa DHT22 Recv");

}

long microsecondsToCentimeters(long microseconds)
{
  return microseconds / 29 / 2;
}

void loop() {
  // put your main code here, to run repeatedly:
  long duration, cm;
  
  //초음파 센서 cm 구하기
  digitalWrite(trigPin, LOW);
  delayMicroseconds(2);
  digitalWrite(trigPin, HIGH);
  delayMicroseconds(10);
  digitalWrite(trigPin, LOW);
  duration = pulseIn(echoPin, HIGH);

  cm = microsecondsToCentimeters(duration);

  float trashp = 100 - (cm-4)*2.777777777777778;
  byte trashp_data = trashp;
  Serial.println(trashp);

  //시리얼 통신으로 값 전송
  mySerial.write(trashp);

  //먼저 값 보내주기
  char buffer[100];
  //보낼 값
  String v = String(trashp);
  const char *value_u = v.c_str();
  String send_value;

  // 새로운 카드가 감지되면
  if (mfrc522.PICC_IsNewCardPresent()) {
  // 카드의 UID를 읽기
  if (mfrc522.PICC_ReadCardSerial()) {
     Serial.print(F("'uid': "));
     // UID를 시리얼 모니터에 출력
     dump_byte_array(mfrc522.uid.uidByte, mfrc522.uid.size);
     Serial.println();
        
     // 카드 읽기 작업 후 정리
     mfrc522.PICC_HaltA();
     mfrc522.PCD_StopCrypto1();

     // UID를 문자열로 변환하여 출력
     rfidValue = byteArrayToString(mfrc522.uid.uidByte, mfrc522.uid.size);
     //Serial.println(rfidValue);

     const char *value_r = rfidValue.c_str();
     //buffer에 보낼 값 넣어주기   
     sprintf(buffer, "A:%s:%s",value_u,value_r);
  }

}
else{
  sprintf(buffer, "A:%s:""",value_u);
}

send_value = buffer;
  if (SNIPE.lora_send(send_value))
  { 
    Serial.print("send:");
    Serial.println(send_value);
    Serial.println("send success");
   }
   else
   {
     Serial.println("send fail");
     delay(500);  
    }
  
    String ver = SNIPE.lora_recv(); //로라로 수신
   //로라 통신
   //만약 데이터가 오류 없이 도착하면
   if (ver != "AT_RX_TIMEOUT" && ver != "AT_RX_ERROR")
   {
      memset(buffer, 0x0, sizeof(buffer));
      ver.toCharArray(buffer, sizeof(buffer));
      int seperatorIndex1 = ver.indexOf(':');

      String Value1 = ver.substring(0, seperatorIndex1);
      String Value2 = ver.substring(seperatorIndex1 + 1);
      
      Serial.println("recv success");
      Serial.print("MESSAGE:");
      Serial.println(ver);
      Serial.println(SNIPE.lora_getRssi());
      Serial.println(SNIPE.lora_getSnr());
      Serial.print("certified:");
      Serial.println(Value1);
      Serial.print("ratio:");
      Value2 = Value2.toFloat();
      Serial.println(Value2);
    }
   else{
      Serial.println("recv fail");
      delay(500);
    }

delay(500);

}

// 바이트 배열을 16진수로 출력하는 함수
void dump_byte_array(byte *buffer, byte bufferSize) {
  for (byte i = 0; i < bufferSize; i++) {
    Serial.print(buffer[i] < 0x10 ? " 0" : " ");
    Serial.print(buffer[i], HEX);
  }
}

// 바이트 배열을 문자열로 변환하는 함수
String byteArrayToString(byte *buffer, byte bufferSize) {
  String result = "";
  for (byte i = 0; i < bufferSize; i++) {
    if (buffer[i] < 0x10) {
      result += "0";
    }
    result += String(buffer[i], HEX);
    if (i < bufferSize - 1) {
      result += " ";
    }
  }
  return result;
}

void resetRFIDValue() {
  // 변수 초기화
  rfidValue = "";
  Serial.println("RFID 값이 리셋되었습니다.");
}
