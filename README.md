# Gson Enclosing Type Adapter Factory Plugin

**gson-enclosing-plugin**是一款基于**gson**提取嵌套json对象的插件。


## 如何安装?

运行cmd命令行程序，依次执行如下命令，将打包到maven本地仓库。

``` bash
cd gson-enclosing-plugin
mvn install
```


## Usage

- 直接使用内置轻量的Gson对象。
```java
Gson gson=Enclosing.with(User.class).on("user").slight()
```

- 将EnclosingTypeAdapterFactory注册到GsonBuilder上。
```java
Gson gson=Enclosing.with(User.class).on("user").to(aGsonBuilder)
```

- 获取EnclosingTypeAdapterFactory对象。
```java
Gson gson=Enclosing.with(User.class).on("user").bare()
```


## Examples
[EnclosingExample](src/test/java/examples/EnclosingExample.java)