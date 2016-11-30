#安卓大作业--BillingBilling


##基本功能

* 简单记录日常的收入、支出账单
* 查看已记录的所有账单，且可选择想要查看模块的账单
* 查看余额、昨日收入、昨日支出
* 通过折线图查看近七日支出趋势图
* 自定义模块（如书籍、食物、衣服等）
* 查看支出、收入模块
* 用户注册、登录、退出
* 保存登录信息，自动登录


##使用说明

    1、安装后，注册账号，
    2、添加支出、收入模块
    3、添加一个个账单
    
    不支持本地存储，数据全保持在服务器上，目前服务器搭建在实训分配的服务器上，所以最好是用学校的网使用本app

##客户端

### 开发环境
   
  * IDE：Android Studio 2.1.3 
  
  * language:Java
  
### 打包配置
  
  * compileSdkVersion: 24
  
  * targetSdkVersion:24
  
  * minSdkVersion:18
 
### 开发设计到的知识点

  * Activity生命周期、多个Activity之间的跳转与通信
    
  * 线程之间的通信（handler）
  
  * Http通信（HttpURLConnection）
  
  * Fragment、FragmentTabHost布局
  
  * ListView、ArrayAdapter、SimpleAdapter
  
  * JSON数据解析（org.json）
  
  * Canvas、Paint绘图
  
  * 本地数据存储（SharedPreferences）
    
   
##服务端

###开发环境

* IDE:PyCharm

* Languae:Python2.7

* Database:Mysql

###搭建配置(用的实训服务器)

* nginx + uwsgi + gevent + flask

* Linux

* Mysql

###开发、搭建设计到的知识点

* 基于flask的开发

* python与mysql的连接

* 数据库设计

* Mysql常用命令

* nginx、uwsgi、gevent、flask的安装与配置

* linux bash命令（netstat、ps、ln、find、locate、rm、cp、tar、unzip等）

## 待完善部分

* 本地离线存储
* 云端同步
* 支持中文存储
* 更多统计信息（周统计、月统计）
* 语音输入
* 优化数据库
* 优化通信格式
* 为适配更多机型的分配率
