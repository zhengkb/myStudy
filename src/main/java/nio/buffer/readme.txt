2020/02/27 22:43
在使用nio读取和写入数据时最关键的时channel以及buffer
buffer作为数据存储的临时块（数据存储）
channel作为一种通道连接channel以及buffer可以将buffer中的数据读取到channel中并通过channel写入文件或者输出到控制台当中

2020/03/02 23:11
构造消息处理
服务端：
初始化，
调用Selector.open() 实例化获取一个selector
调用ServerSocketChannel.open(),实例化获取一个serverSocketChannel
设置获取的serversocketChannel为非阻塞
调用自身的register将其注册到selector中
处理连接信息，
阻塞返回
调用selector.selectedKeys获取当前所有事件
处理事件-》区分事件类型，对于连接事件（isAcceptable()）调用serverSocketChannel.accept()接入连接，并获取SocketChannel，将该channel设置为非阻塞并注册到selector中（register中有个参数用于设置buffer容量
                        ，在读取数据时可以调用attachement拿到对应的buffer）。
                        对于读取事件（isReadable())直接调用channel获取socketchannel通过该socketchannel读取对应管道的数据
使用迭代器移除事件，避免还重复调用（易出现报错）
广播，通过selector拿到当前所有的连接（.keys()），判断其类型，如果是socketchannel且与当前channel不一致则将数据写入到管道中
连接断开：关闭对应的socketchannel，取消对应的selectingkey

客户端:
初始化
调用Selector.open() 实例化获取一个selector
直接调用socketchannel.open(地址)获取连接
设置非阻塞
注册
发送数据，
调用实例化的socketchannel的write方法进行数据传输
接收数据，
注册的selector.select()查看当前是否有事件发生
读取读事件(isreadable())
获取输入数据，输出

https://blog.csdn.net/sunboylife/article/details/89527461

2020/03/03 21:57
单reactor多线程
(1)reactor对象通过select监控客户端请求，收到事件后通过dispatch进行分发
(2)如果是建立连接请求，则Acceptor通过accept处理连接请求，随后创建一个handler对象处理完成连接后的事件
(3)如果不是连接请求，则由reactor分发调用对应的handler进行处理
(4)handler只负责响应事件，不做具体的业务处理，通过read读取的数据调用worker线程池的某个线程进行处理
(5)worker会分配对应线程进行处理，并将处理结果返回给handler
(6)handler接收处理结果调用send将记过返回给client
方案优缺点：
优点：充分利用多核cpu的处理能力
缺点：多线程数据共享和访问比较复杂，reactor本身只有一个线程在处理连接请求，在高并发场景还是会出现瓶颈

主从reactor多线程
(1)Reactor主线程MainReactor对象通过select监听连接事件，收到连接事件后通过Acceptor处理连接事件
(2)当Acceptor处理完连接之后，Mainreactor将连接分配给SubReactor
(3)subReactor将连接加入到连接队列进行监听并创建handler进行各种事件的处理
(4)当有事件发生时，subReactor会调用对应的handler进行处理
(5)handler通过read读取数据，分配给对应的worker线程进行业务处理，并返回结果
(6)handler接收到返回结果之后，将结果发送给client
(7)reactor主线程可以有多个subReactor



