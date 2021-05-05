# Automatic checking access system using [Beacon](https://ko.wikipedia.org/wiki/%EB%B9%84%EC%BD%98)
> 2021 - 1학기  

<br/>

## 🤝 Team : " 볶음밥은 인하각 "  
👨‍🚀 (C)[차민재](https://github.com/charmingjae)  
👨‍🚀 [김응빈](https://github.com/eungbin)  
👨‍🚀 [박규민](https://github.com/mareepark)  



<br/>

## 👨🏻‍💻 Overview  
COVID-19 사태로 인해 정부에서는 음식점과 같은 사업장에 방문 시 QR코드를 이용하여 방문자의 기록을 보관한다.
QR코드 방식은 QR코드를 발급하는 회사로부터 1회성 코드를 발급받아 체크하는데 QR코드를 받는데까지의 과정이 오래 걸린다.
따라서 이러한 비효율적인 부분을 개선하기 위해 비콘을 활용하여 사업장 방문 시 자동적으로 출입 여부를 체크하도록 시스템을 구현하고자 한다.

<br/>

## 🔧 Tech

OS(Operating System) :
```java
Android ( SDK Version : 28 )
```

Database : 
> ⚠️ Need [google-services.json](https://firebase.google.com/docs/android/setup?hl=ko)
```
Firebase
```

Beacon :
```
Minew beacon E7
```

<br/>


## 🏃‍♂️ Getting Started
> 본 프로젝트는 H/W(Beacon)과 Android 기기 간 통신을 이용한 프로젝트로 구동 시 Beacon과 Android OS를 기반으로 한 App을 구동할 수 있는 기기가 필요합니다.  

1. Clone
~~~bash
git clone https://github.com/charmingjae/beacon_attendance
~~~  

2. Turn on the device Bluetooth  

3. Run

<br/> 

## 📦 Setting

> Dependencies  

~~~java
dependencies {
    implementation 'androidx.appcompat:appcompat:1.2.0'
    implementation 'com.google.android.material:material:1.3.0'
    implementation 'androidx.constraintlayout:constraintlayout:2.0.4'
    implementation 'com.google.firebase:firebase-database:19.7.0'
    implementation 'com.google.firebase:firebase-auth:20.0.4'
    implementation 'com.android.support:support-v4:28.0.0'
    implementation 'com.android.support:appcompat-v7:28.0.0'
    testImplementation 'junit:junit:4.+'
    androidTestImplementation 'androidx.test.ext:junit:1.1.2'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.3.0'
    implementation fileTree(include: ['*.jar'], dir: 'libs')
    implementation files('libs/minewBeaconAdmin.jar')
    implementation 'com.android.support:recyclerview-v7:28.0.0'
    compile 'com.android.support:recyclerview-v7:28.0.0'
}
~~~

<br/>  

> Android config

~~~java
compileSdkVersion 28
buildToolsVersion "30.0.3"
minSdkVersion 18
targetSdkVersion 26
~~~

<br/>  

## 📖 Comment  

> Need HardWare 'Beacon' to check rssi between beacon and device

본 프로젝트는 모바일 디바이스와 Beacon간의 신호 세기를 바탕으로 구현 된 어플리케이션입니다. 따라서 원활한 어플리케이션의 구동과 사용을 위해 [비콘](https://ko.wikipedia.org/wiki/%EB%B9%84%EC%BD%98)이 필요합니다.  
본 프로젝트에서 사용 된 비콘의 구매 정보는 [여기](http://m.nowwin.co.kr/product/%EB%B9%84%EC%BD%98-ibeacon-%EB%B8%94%EB%A3%A8%ED%88%AC%EC%8A%A4-beacon-i9-%EB%B9%84%ED%8F%B0-beafon/74/)에서 확인하실 수 있습니다.