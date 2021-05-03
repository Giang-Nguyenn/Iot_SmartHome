#include <Firebase.h>
#include <FirebaseArduino.h>
#include <ESP8266WiFi.h>
#include "DHT.h"
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <string.h>
//set wifi
const char* namee = "OPPO A37fw";//name wifi connect
const char* pass = "12345678.";//password
String value;
String value_light;
float value_tem=23;
float value_hum=68;
//light D0,led-D5
int led1 = 14;
int cambien_light = 16;
//temperature, humidity D1,led-D6
int led2 = 12;
const int DHTPIN = 4;       //Đọc dữ liệu từ DHT11 ở chân 2 trên mạch Arduino
const int DHTTYPE = DHT11;  //Khai báo loại cảm biến, có 2 loại là DHT11 và DHT22 
DHT dht(4, DHT11);
//gas D3
int led3 = 0;
int gas1 = 5;
int gas2=2;
float gas_value=10;
String get_time;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  delay(1000);
  connectWifi();
  timeClient.begin();

  //connect Firebase
  Firebase.begin("internet-of-thing-6c4d7-default-rtdb.firebaseio.com","oygjlIxQMXj4UltIHRgyJpivYJZBzwD4AaEtJZFE");
  pinMode(cambien_light,INPUT);
  pinMode(led1, OUTPUT);
  pinMode(led2, OUTPUT);
  pinMode(led3, OUTPUT);
//  pinMode(gas1,INPUT);
  pinMode(gas2,INPUT);
  dht.begin();

}

void loop() {
    // put your main code here, to run repeatedly:
   get_time=gettime();

  dht1();
  light();
  gas();


  delay(1000); 
}
//kiểm tra chuỗi có bắt đầu bằng một chuỗi khác
boolean startwith(String a,String b){
  for(int i=0;i<b.length();i++){
    if(!(a[i]==b[i])){return false;}
    }
    return true;
  }
//lấy thời gian thực GTM+7
String gettime(){
  timeClient.update();
  timeClient.setTimeOffset(25200);
 return timeClient.getFormattedTime();
  }
  //cảm biến khí gas
void gas(){
    Serial.println(gas_value);
//    Serial.println(analogRead(gas1));
    gas_value = digitalRead(gas2);
    if(startwith(Firebase.getString("user1/fire/fire1/auto"),"true")){
      Serial.println("gas auto");
      if(gas_value==0){
        Serial.println("Có gas");
        digitalWrite(led3, HIGH);
        if(startwith(Firebase.getString("user1/fire/fire1/status"),"0")){
        Firebase.setString("user1/fire/fire1/status","1+"+get_time);
        } 
      }
      else{
        Serial.println("Không có gas");
        digitalWrite(led3, LOW);
        if(startwith(Firebase.getString("user1/fire/fire1/status"),"1")){
        Firebase.setString("user1/fire/fire1/status","0+"+get_time);
        }
      }
    }
    if(startwith(Firebase.getString("user1/fire/fire1/auto"),"false")){
      digitalWrite(led1, LOW);
      Serial.println("gas không auto");
      if(gas_value==0){
        Serial.println("Có gas");
        if(startwith(Firebase.getString("user1/fire/fire1/status"),"0")){
        Firebase.setString("user1/fire/fire1/status","1+"+get_time);
        }
      }
      else{
        Serial.println("Không có gas");
        if(startwith(Firebase.getString("user1/fire/fire1/status"),"1")){
        Firebase.setString("user1/fire/fire1/status","0+"+get_time);
        }
      }
    }
}
  //cảm biến nhiệt độ,độ ẩm
void dht1(){
  float h = dht.readHumidity();    
  float t = dht.readTemperature(); 
  if(startwith(Firebase.getString("user1/tem_hum/tem_hum1/auto"),"true")){
    if(t>26){
      Firebase.setString("user1/tem_hum/tem_hum1/status","1+"+get_time);
      digitalWrite(led2, HIGH);
    }
    else{
      Firebase.setString("user1/tem_hum/tem_hum1/status","0+"+get_time);
      digitalWrite(led2, LOW);
    }
    Serial.print("auto");
    }
   else if(startwith(Firebase.getString("user1/tem_hum/tem_hum1/auto"),"false")){
    Serial.print("không auto");
       if(startwith(Firebase.getString("user1/tem_hum/tem_hum1/status"),"1")){
          digitalWrite(led2, HIGH);
       }
       else{
          digitalWrite(led2, LOW);
       }
    }
   else{
   Serial.print("hẹn giờ");
   }
      Firebase.pushString("user1/tem_hum/tem_hum1/data/tem",String(t)+"+"+get_time);
      Firebase.pushString("user1/tem_hum/tem_hum1/data/hum",String(h)+"+"+get_time);
//  if(value_tem!=t){
//    Firebase.pushString("user1/tem_hum/tem_hum1/data/tem",String(t)+"+"+get_time);
//    value_tem=t;
//    }
//  if(value_hum!=h){
//    Firebase.pushString("user1/tem_hum/tem_hum1/data/hum",String(h)+"+"+get_time);
//    value_hum=h;
//    }
  

  Serial.print("Nhiet do: ");
  Serial.println(t);               
  Serial.print("Do am: ");
  Serial.println(h);  
              
  Serial.println();               
  }
  //cảm biến ánh sáng
void light(){
  value=String(digitalRead(cambien_light));
  if(startwith(Firebase.getString("user1/light/light1/auto"),"truee")){
    value_light="reset_value_light";
    Firebase.setString("user1/light/light1/auto","true");
    }
    if(startwith(Firebase.getString("user1/light/light1/auto"),"true")){
        if(value_light!=value){
               Serial.print("Giá trị sáng đã thay đổi");
               Firebase.setString("user1/light/light1/status",value+"+"+get_time);
               String a=Firebase.getString("user1/light/light1/status");
               Serial.print("Giá trị là :");
               if(startwith(a,"0")){
                 Serial.println("sáng");
                 digitalWrite(led1, LOW);
               }
              else{
              Serial.println("tối");
              digitalWrite(led1, HIGH);
              }
         }
    }
    else if(startwith(Firebase.getString("user1/light/light1/auto"),"time")){
      Serial.println("Hẹn giờ");
      String t=Firebase.getString("user1/light/light1/time/everyday");
      if(startwith(Firebase.getString("user1/light/light1/status"),"1")){
        Serial.print("Đang bật đèn");
        if(((get_time[0]-48)*10*3600+(get_time[1]-48)*3600+(get_time[3]-48)*600+(get_time[4]-48)*60+(get_time[6]-48)*10+(get_time[7]-48))>=
        ((t[9]-48)*10*3600+(t[10]-48)*3600+(t[12]-48)*600+(t[13]-48)*60+(t[15]-48)*10+(t[16]-48))){
          digitalWrite(led1, LOW);
          Firebase.setString("user1/light/light1/status","0+"+get_time);
          }
        
        }
      else{
        Serial.print("Đang tắt đèn");
        if(((get_time[0]-48)*10*3600+(get_time[1]-48)*3600+(get_time[3]-48)*600+(get_time[4]-48)*60+(get_time[6]-48)*10+(get_time[7]-48))>=
        ((t[0]-48)*10*3600+(t[1]-48)*3600+(t[3]-48)*600+(t[4]-48)*60+(t[6]-48)*10+(t[7]-48))){
          digitalWrite(led1, HIGH);
          Firebase.setString("user1/light/light1/status","1+"+get_time);
          }
        }
      
     }
    else{
      Serial.println("Không tự động");
      String a=Firebase.getString("user1/light/light1/status");
               Serial.print("Giá trị là :");
               if(startwith(a,"0")){
                 Serial.println("sáng");
                 digitalWrite(led1, LOW);
               }
              else{
              Serial.println("tối");
              digitalWrite(led1, HIGH);
              }
    }
    value_light=value;
  }
//hi
//connect wifi
void connectWifi(){
  Serial.print("Kết nối wifi");
  WiFi.begin(namee,pass);
  while(WiFi.status() !=WL_CONNECTED){
    delay(500);
    Serial.print(".");
    }
    Serial.print("Kết nối thành công");
  }
