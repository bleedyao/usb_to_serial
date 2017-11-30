# usb_to_serial [![](https://jitpack.io/v/bleedyao/usb_to_serial.svg)](https://jitpack.io/#bleedyao/usb_to_serial)

Android Usb 转串口：一个可扩展（目前不支持扩展芯片）的，在 Android 设备中通过 usb 转串口实现设备之间的数据收发，最达到设备与设备之间的通信。

## 设备支持
FTDI 设备 默认:9600,8,1,None,flow off

## 测试芯片
* FT232R(实测芯片)
* 理论上可以驱动所有 FTDI 类型的芯片，例如：FT230X, FT231X, FT234XD, FT232R 和 FT232H.

## 准备工作
Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

Step 2. Add the dependency
```groovy
	dependencies {
		compile 'com.github.bleedyao:usb_to_serial:lastest'
	}
```

## 代码实现
* 在执行任何操作之前（建议在程序入口的 onCreate 方法中），必须初始化驱动
```java
FtDev.init(context);
```
* 获得 usb 设备的对象
```java
FtDev dev = FtDev.getInstance()
```
* 设置参数(后两个设置，下文会用到)
```java
dev.setBaudRate(115200)
   .setDataBit((byte) 8)
   .setFlowControl((byte) 0)
   .setParity((byte) 0)
   .setFlowControl((byte) 0)
   .setStopBit((byte) 1)
   .setConvertModel(new HexConvertModel())
   .setCharsetName("ISO-8859-1");
```
波特率等 6 个参数上文中有默认值，因此这 6 个参数可以不进行修改。

* 发送数据

  注：请先连接你的 FTDI 系列的硬件设备
```java
dev.sendMessage("你要发送的任何数据的字符串形式");
```


这里用到了上边设置的的两个参数：setConvertModel —— 设置转换模式，setCharsetName —— 设置编码格式

这两参数在设置的时候没有先后顺序。

*    编码格式
    发送的字符串会以此编码格式，转换成二进制数据流的形式发送发送出去。默认是 ISO-8859-1

* 转换模式

    在将字符串转换成数据流之前的转换操作。比如我传的是 16 进制报文："5A0000810178"，我的转换需求是将 16 进制报文转成 ASCII 码，之后再转换成数据流。

    所以我需要把这个转化过程放在 convert 里面，而 restore 则是接收的时候的转换逻辑。注：接收的数据是字符组成的字符串，在 ReadThread 类中第 50 ~ 53 行显示。

    如果你发送的是想 AT 指令这样的字符串（"AT+UID=?"），完全可以不设置转换模式即可。

    如果你和我的转换逻辑不同，你可以自定义 Converter，在定义自己的转换模式

* 接收数据
    此函数库采用观察者模式，当有 usb 设备接收到数据后，会更新所有观察者的数据。为了看到接收的效果，需要自行模拟返回数据。

    * 用要获取数据的 class 类实现 MessageObserver 接口。
    * filter 是过滤器，在此处选择你要接收的数据样式，返回 true 则接收数据。
    * receive 这里是接收到的数据，和数据的有效长度，用于判断广播命令中，是否存在多条数据存在于一条返回数据中。
    * 调用 dev.addObserver 方法，监听被观察者。
## 退出

在 onStop 方法中调用 dev.close()，结束程序

## 实例代码 sample 说明
sample 中的代码是我在之上的基础上进行的封装，其原理和上述内容相同，
​    