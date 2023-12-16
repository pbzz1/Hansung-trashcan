//로라 통신
#include <LiquidCrystal.h>
#include "SNIPE.h"
#define ATSerial Serial

//16byte hex key
String lora_app_key = "11 22 33 44 55 66 77 88 99 aa bb cc dd ee ff 00";
SNIPE SNIPE(ATSerial);

LiquidCrystal lcd(2,3,4,5,6,7);//LCD pin

void setup()
{
  Serial.begin(9600); //시리얼 통신 시작
  while(!Serial); //시리얼 통신이 준비될 때까지 대기
  ATSerial.begin(115200);
  while (ATSerial.read() >= 0) {}
  while (!ATSerial);

  //lcd
  lcd.begin(16,2);

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

void loop()
{

  char buffer[100];
  String ver = SNIPE.lora_recv();
  delay(300);
  lcd.clear();
  lcd.setCursor(1,0);
  
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
      float value2 = Value2.toFloat();
      Serial.println(Value2);
      
      if(value2 == 100.00){
        lcd.print("A is Full");
        Serial.println("A is Full");
        delay(500);
      }
      else{
        lcd.print("A is not FUll");
        Serial.println("A is not Full");
        delay(500);
      }
      
   }
   else{
      Serial.println("recv fail");
      delay(500);
    }
   delay(1000);
}
