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




