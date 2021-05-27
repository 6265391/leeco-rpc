## LeeCo-RPC
<p align="center">
<a href="https://github.com/KFCFans/PowerJob/blob/master/LICENSE"><img src="https://img.shields.io/github/license/KFCFans/PowerJob" alt="LICENSE"></a>
</p>
#### 基于netty和marshalling(Jboss) 实现自定义协议栈网络通信

 1. 启动服务端 启动客户端
 2. 客户端会首次向服务段发起握手 携带节点ID等有效身份认证信息
 3. 服务端验证握手 并返回握手应答
 4. 链路建立
 5. 客户端会像服务端每50秒发送心跳
 6. 服务端收到心跳请求后 响应心跳请求
 7. 服务端退出 客户端感知后关闭链路
 - 注: 通信采用全双工协议  心跳采用ping-pong机制  当客户端连续N次ping都没有得到pong响应  客户端则主动关闭链路  间隔周期T后发起重连 直至成功
 
 
 
 
