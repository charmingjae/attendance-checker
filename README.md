# Automatic attendance system using [Beacon](https://ko.wikipedia.org/wiki/%EB%B9%84%EC%BD%98)
> 2021 - 1학기  

<br/>

## 🤝 Team : " 볶음밥은 인하각 "
👨‍🚀 (C)[차민재](github.com/charmingjae)  
👨‍🚀 [김응빈](github.com/eungbin)  
👨‍🚀 [박규민](github.com/mareepark)  



<br/>

## 👨🏻‍💻 Overview  
2021년 현재 교내에서 사용 중인 출석 시스템은 비효율적이다.  
첫 학생부터 마지막 순서의 학생까지 출석을 확인하는 과정은 교수님께 크게 의존되어 있고 시간이 상당히 많이 걸린다.  
따라서 의존성, 효율성에서 나타나는 문제점을 해결하고자 본 프로젝트를 계획하였다.

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