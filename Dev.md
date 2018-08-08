如何处理持续回调和事件监听
-------------

js调用java 回调完成后回删除回调引用以回收资源，但是有些时候需要持续监听事件，例如文件下载、按键点击等
这是要的处理方式如下：
1. java端创建一个Interface，里面添加方法，方法添加@JSInterface注解，通过NIMJsBridge获取到动态代理对象，该对象可以用于调用js方法
2. js端通过JSBridge对象的_registerHandler方法注册1创建的方法实现监听