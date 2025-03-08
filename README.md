# Demo Web Chess Rating and Mobile Calculator Automation
## Video Demo & Reports
### 1. Chess rating:
- Video: [Demo Chest Rating](https://drive.google.com/file/d/1FHY3ecAVgYyG1qeHDgRdVYC6LIug7S7p/view?usp=sharing)

- Report: [Chess Data spread sheet](https://docs.google.com/spreadsheets/d/1hzFfM3qhEX5RC5ZLDk9Fb4S8ijfx1iLEErdhnWOJPaQ/edit?gid=1526660525#gid=1526660525)
### 2. Calculator mobile app:
- Video : [Demo Calculator](https://drive.google.com/file/d/1X_FQE1B8rnH5AWZCYaSaXDa-ZMZqG6_B/view?usp=sharing)
## Setup & Run Instructions
Requirements (Latest Version):
- ✅ **Java 21+** 
- ✅ **Maven**
- ✅ **Appium Server** 
- ✅ **Android SDK** & **ADB** 
- ✅ **Google Chrome** 
- ✅ **Node.js & npm**
### 1. Clone the Repository
```
git clone https://github.com/PhiHoc/gsm-interview-demo.git
cd gsm-interview-demo
mvn clean install "-Dmaven.test.skip=true"
```
### 2. Run the Tests
#### 2.1. ChessRatingTest: **Extract data from chess.com** and updates the data in **Google Sheets**.
```
mvn clean verify "-Dit.test=ChessRatingTest"
```
#### 2.2. CalculatorTest:
The Appium tests in this project are designed to run on the following Android device:

| Property       | Value                 |
|---------------|-----------------------|
| **Device Name** | 2201117SG |
| **Model**       | Xiaomi Redmi Note 11 |
| **Android Version** | 13 |
| **UDID**        | BMGINFA6IZ6XW4SS |
| **Automation Framework** | UIAutomator2 |
| **App Package** | `com.miui.calculator` |
| **App Activity** | `com.miui.calculator.cal.CalculatorActivity` |

If you are using a **different Android device**, update the **UDID** and **device name** directly to the code `CalculatorTest.java`.

Ensure your Android device is connected
```
adb devices
```
Run the test
```
mvn clean verify "-Dit.test=CalculatorTest"  
```

