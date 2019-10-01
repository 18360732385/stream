# stream

**利用sha1withrsa算法进行报文加解密、报文日志的打印，学习过滤器、拦截器、切片特性**

SecretKey：yml文件配置类属性，并在filter中使用

SecretFilter：报文加解密、全局流水号

LogFilter：日志过滤器

aspect：打印日志（弃用）

RequestWrapper和ResponseWrapper：重写的请求响应体

自定义异常、全局异常处理

自定义响应体、自定义响应码enum