#include <pgmspace.h>
 
#define SECRET
 
const char WIFI_SSID[] = "";      //와이파이 아이디             
const char WIFI_PASSWORD[] = "";  //와이파이 비밀번호
 
#define THINGNAME "" //AWS IoT에 등록된 사물 이름 입력부분
 
int8_t TIME_ZONE = -5; //NYC(USA): -5 UTC
 
const char MQTT_HOST[] = ""; //AWS IoT 엔드포인트 입력 부분(보안 상의 이유로 삭제)
 
//AWS IoT에서 제공하는 CA 인증서 입력 부분
static const char cacert[] PROGMEM = R"EOF(  
-----BEGIN CERTIFICATE-----
-----END CERTIFICATE-----
)EOF";
 
// AWS IoT에서 생성한 클라이언트 인증서 입력 부분(보안 상의 이유로 삭제)
// Copy contents from XXXXXXXX-certificate.pem.crt here ▼ 
static const char client_cert[] PROGMEM = R"KEY(
-----BEGIN CERTIFICATE-----
-----END CERTIFICATE-----
)KEY";
 
// 개인 키 입력 부분(보안 상의 이유로 삭제)
// Copy contents from  XXXXXXXX-private.pem.key here ▼ 
static const char privkey[] PROGMEM = R"KEY(
-----BEGIN RSA PRIVATE KEY-----
-----END RSA PRIVATE KEY-----
)KEY";
