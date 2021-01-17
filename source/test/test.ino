#include <Firebase.h>
#include <FirebaseArduino.h>
//#include <FirebaseESP8266.h>
#include <ESP8266WiFi.h>
#include "DHT.h"
#include <NTPClient.h>
#include <WiFiUdp.h>
#include <string.h>
//FirebaseData firebaseData;
//FirebaseData fbdo;
//const char* ssid = "1 2 3 4";
//const char* password = "12345678@@";
const char* namee = "1 2 3 4";//name wifi connect
const char* pass = "12345678@@";//password
String value;
String value_light;
float value_tem;
float value_hum;
//light D0,led-D5
int led1 = 14;
int cambien_light = 16;
//temperature, humidity D1,led-D6
const int DHTPIN = 4;       //Đọc dữ liệu từ DHT11 ở chân 2 trên mạch Arduino
const int DHTTYPE = DHT11;  //Khai báo loại cảm biến, có 2 loại là DHT11 và DHT22 
DHT dht(4, DHT11);
//get time
String get_time;
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP);


void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  delay(1000);
  connectWifi();
  timeClient.begin();

  
  Firebase.begin("internet-of-thing-6c4d7-default-rtdb.firebaseio.com","oygjlIxQMXj4UltIHRgyJpivYJZBzwD4AaEtJZFE");
//  Firebase.pushString("tem_hum/a","123");
  pinMode(cambien_light,INPUT);
  pinMode(led1, OUTPUT);
  dht.begin();

}

void loop() {
    // put your main code here, to run repeatedly:
   get_time=gettime();

  dht1();
//  light();


  delay(2000); 
}
boolean startwith(String a,String b){
  for(int i=0;i<b.length();i++){
    if(!(a[i]==b[i])){return false;}
    }
    return true;
  }
String gettime(){
  timeClient.update();
  timeClient.setTimeOffset(25200);
//  unsigned long epochTime = timeClient.getEpochTime();
//  struct tm *ptm = gmtime((time_t *)&epochTime);
//  int currentYear = ptm->tm_year+1900;
//  int currentMonth = ptm->tm_mon+1;
//  int monthDay = ptm->tm_mday;
//  String currentDate = String(currentYear) + "-" + String(currentMonth) + "-" + String(monthDay);
 return timeClient.getFormattedTime();
// +currentDate
  }
void dht1(){
  float h = dht.readHumidity();    
  float t = dht.readTemperature(); 
  if(startwith(Firebase.getString("tem_hum/auto"),"true")){
    Serial.print("auto");
    }
   else if(startwith(Firebase.getString("tem_hum/auto"),"false")){
    Serial.print("không auto");
    }
   else{
   Serial.print("hẹn giờ");
   }

  if(value_tem!=t){
    Firebase.pushFloat("tem_hum/data/tem",t);
    value_tem=t;
    }
  if(value_hum!=h){
    Firebase.pushFloat("tem_hum/data/hum",h);
    value_hum=h;
    }
  

  Serial.print("Nhiet do: ");
  Serial.println(t);               
  Serial.print("Do am: ");
  Serial.println(h);  
              
  Serial.println();               
  }
void light(){
  value=String(digitalRead(cambien_light));
    if(startwith(Firebase.getString("light/auto"),"true")){
        if(value_light!=value){
               Serial.print("Giá trị sáng đã thay đổi");
               Firebase.setString("light/status",value+"+"+get_time);
               String a=Firebase.getString("light/status");
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
    else if(startwith(Firebase.getString("light/auto"),"time")){
      Serial.println("Hẹn giờ");
      String t=Firebase.getString("light/time/everyday");
      if(startwith(Firebase.getString("light/status"),"1")){
        Serial.print("Đang bật đèn");
        if(((get_time[0]-48)*10*3600+(get_time[1]-48)*3600+(get_time[3]-48)*600+(get_time[4]-48)*60+(get_time[6]-48)*10+(get_time[7]-48))>=
        ((t[9]-48)*10*3600+(t[10]-48)*3600+(t[12]-48)*600+(t[13]-48)*60+(t[15]-48)*10+(t[16]-48))){
          digitalWrite(led1, LOW);
          Firebase.setString("light/status","0+"+get_time);
          }
        
        }
      else{
        Serial.print("Đang tắt đèn");
        if(((get_time[0]-48)*10*3600+(get_time[1]-48)*3600+(get_time[3]-48)*600+(get_time[4]-48)*60+(get_time[6]-48)*10+(get_time[7]-48))>=
        ((t[0]-48)*10*3600+(t[1]-48)*3600+(t[3]-48)*600+(t[4]-48)*60+(t[6]-48)*10+(t[7]-48))){
          digitalWrite(led1, HIGH);
          Firebase.setString("light/status","1+"+get_time);
          }
        }
      
     }
    else{
      Serial.println("Không tự động");
      String a=Firebase.getString("light/status");
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
void connectWifi(){
  Serial.print("Kết nối wifi");
  WiFi.begin(namee,pass);
  while(WiFi.status() !=WL_CONNECTED){
    delay(500);
    Serial.print(".");
    }
    Serial.print("Kết nối thành công");
  }
