# Redis 学习

关于 Redis 知识点的学习。

## NoSQL数据库简介

说明 NoSQL 数据库的优势以及应用场景等。

### 技术发展

#### 解决CPU及内存压力

web2.0 时代，随着手机端可以访问互联网，服务器承载压力成倍增加，因此开始使用分布式来进行服务部署，但随之也引来了一些问题。

举一个例子，用户登陆需要 session 存储用户信息，如果我们不存储 session 信息，那么负载均衡在首次登陆时假如将请求分发给了服务器 1，此时服务器 1 存储了 session 信息；可是当第二次访问时，Nginx 将请求分发给了服务器 2，此时服务器 2 并未存储 session 信息，又需要重新登陆，这显然是不合理的。

基于上述问题，我们有几套解决方案

- 使用客户端 `cookie` 存储

- 存在文件服务器或者数据库中

- `session` 复制

- 使用`NoSQL` 缓存存储

> cookie 存储

使用 cookie 存储的方式由于是存储在客户端当中，这会导致信息有泄露的风险，非常不安全；而且由于请求当中每次都要携带 cookie 信息，会造成网络负担较重而且传输效率低。

> 存在文件服务器或者数据库中

由于每次都要访问一次服务器获取用户的 session 信息，会存在大量的 IO 问题。

> session 复制

在每台服务器中都复制一遍 session 信息，由于数据都是一样的，会造成数据冗余，节点越多浪费就越大。

> 使用 NoSQL 缓存数据库

存储在内存中，相比于存储在数据库服务器中还需要做 IO 处理相比**读取速度更快，数据结构简单**。

![redis1-session.img](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis1-session.png)

#### 解决 IO 压力

打破了传统关系型数据库以业务逻辑为依据的存储模式，而针对不同数据结构类型改为以性能最优先的存储方式。

![redis2-cache.png](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis2-cache.png)

### NoSQL 数据库

#### NoSQL 数据库概述

NoSQL（Not only SQL），即“不仅仅是 SQL”，泛指**非关系型的数据库**。NoSQL 不依赖业务逻辑方式存储，而以简单的`key-value` 模式存储，因此大大的增加了数据库的扩展能力，拥有以下特性：

- 不遵循 SQL 标准

- 不支持 ACID（原子性Atomicity、一致性Consistency、隔离性Isolation、持久性Durability）

- 远超于 SQL 的性能

#### NoSQL适用场景

- 对数据高并发的读写

- 海量数据的读写

- 对数据高可扩展性

#### NoSQL 不适用场景

- 需要事务支持

- 基于 SQL 结构化查询数据，处理复杂的关系，需要即席查询（即席查询（Ad-hoc Query），也称为临时查询或一次性查询，是指用户根据需要临时发起的查询请求，而不是事先定义好的标准查询）

*不着 SQL和用了 SQL也不行的情况，请考虑用 NoSQL*

#### 常用 NoSQL 数据库及其特点

> Redis

- 几乎涵盖了 Memcached 的绝大部分功能

- 数据都在内存中，支持持久化，主要用作备份恢复

- 除了支持简单的 `key-value` 模式，还支持多种数据结构的存储，例如 `list/set/hash/zset` 等

- 一般是作为缓存数据库辅助持久化的数据库

> MongoDB

- 高性能、开源、模式自由（schema free）的文档型数据库（格式类似 JSON）

- 数据都在内存中，如果内存不足，把不常用的数据保存到硬盘

- 虽然是 `key-value` 模式，但是对 value （尤其是 JSON）提供了丰富的查询功能

- 支持二进制数据及大型对象

- 可以根据数据的特点替代 RDBMS，成为独立的数据库，或者配合 RDBMS，存储特定的数据

> 关键术语 Memcached

Memcached是一个高性能的分布式内存缓存系统，广泛用于提高大型动态网站和应用程序的性能。它通过缓存数据和对象来减少读取外部数据源（如数据库或API）的次数，从而加快数据检索速度并降低延迟。

Memcached的主要特点包括：

1. **简单性**：Memcached的设计非常简洁，专注于提供快速的缓存服务。

2. **分布式**：它支持分布式缓存，可以跨多个服务器运行，以支持大规模的缓存需求。

3. **内存存储**：Memcached将数据存储在内存中，这使得访问速度非常快。

4. **键值存储**：Memcached使用简单的键值对（key-value）模型来存储数据。

5. **多线程**：它支持多线程，可以同时处理多个请求，提高并发性能。

6. **自动过期**：Memcached允许设置缓存数据的过期时间，过期后数据会自动从缓存中删除。

7. **LRU（最近最少使用）算法**：当缓存达到容量上限时，Memcached会使用LRU算法自动移除最长时间未被访问的数据。

8. **网络协议**：Memcached使用自己的网络协议来处理客户端请求，这简化了缓存操作。

9. **跨平台**：它可以在多种操作系统上运行，包括Linux、Unix和Windows。

10. **广泛的客户端支持**：几乎所有流行的编程语言都有Memcached的客户端库，使得集成和使用变得容易。

Memcached的应用场景

- **网站缓存**：用于缓存网页内容、会话信息等，减少数据库访问次数。
- **API缓存**：缓存API调用结果，提高响应速度。
- **数据库缓存**：作为数据库的前端缓存，减少数据库负载。
- **对象缓存**：缓存应用程序的对象，如用户会话、购物车等。

使用Memcached时需要注意的问题：

- **数据丢失**：由于Memcached主要运行在内存中，如果服务器故障，缓存的数据可能会丢失。
- **数据一致性**：需要确保缓存数据与后端数据源的一致性。
- **缓存穿透**：需要采取措施防止缓存穿透问题，即大量请求未缓存的数据。
- **缓存雪崩**：避免大量缓存数据同时过期，导致缓存雪崩。

Memcached是一个强大的工具，可以帮助提高应用程序的性能和可扩展性，但也需要合理配置和管理以发挥最佳效果。

### 行式存储数据库

#### 行式数据库

类似于 MySQL 等主流数据库的存储形式，以行式数据存储数据

| id  | name | city | age |
| --- | ---- | ---- | --- |
| 1   | 张三   | 深圳   | 20  |
| 2   | 李四   | 广州   | 30  |
| 3   | 王五   | 南宁   | 35  |

> 行式数据库存储模式

进行 OLAP 分析型处理慢（例如查平均值等操作）

进行 OLTP 事务型处理快（例如查询 id 为 3 的人员信息）

```sql
1,张三,深圳,20
2,李四,广州,30
3,王五,南宁,35


select * from user where id = 1 # 快
select avg(age) from user # 慢
```

> 列式数据库

进行 OLAP 分析型处理快（例如查平均值等操作）

进行 OLTP 事务型处理慢（例如查询 id 为 3 的人员信息）

```sql
1,2,3
张三,李四,王五
深圳,广州,南宁
20,30,35

select * from user where id = 1 # 慢
select avg(age) from user # 快
```

---

## Redis特点

- Redis是一个**开源的key-value存储系统**

- 和`Memcached`类似，它支持存储的value类型相对更多，包括`string`（字符串）、`list`（链表）、`set`（集合）、`zset`（sorted set 有序集合）和`hash`（哈希类型）

- 这些数据类型都支持`push/pop`、`add/remove`及取交集、并集和差集及丰富的操作，而且这些操作都是**原子性**的

- 在此基础上，Redis支持各种不同方式的排序

- 与Memcached一样，为了保证效率，数据都是**缓存在内存中**

- 区别是Redis会**周期性的把更新的数据写入磁盘**或者把修改操作写入追加的记录文件

- 在此基础上，Redis实现了`master-slave`（主从）同步

### Redis使用场景

> 配合关系型数据库做高速缓存

- 高频次、热门访问的数据，降低数据库IO

- 分布式架构，做session共享

> 多样的数据结构存储持久化数据

- 最新N个数据  <===  通过`list`实现按自然时间排序的数据

- 排行榜、TopN <=== 利用`zset`有序集合

- 时效性的数据，比如手机验证码  <=== Expire过期

- 计数器、秒杀 <=== 原子性，自增方法`INCR`/`DECR`

- 去除大量数据中的重复数据 <=== 使用`set`集合

- 构建队列 <=== 利用`list`集合

- 发布订阅消息模式 <=== `pub/sub`模式

### Redis相关知识

- 默认16个数据库，类似数组下标从0开始，初始**默认使用0号数据库**

- 使用命令 `select dbid` 进行切换数据库，如：`select 3`

- 统一密码管理，所有库同一个密码

- `dbsize`查看当前数据库key 的数量

- `flushdb`清空当前库

- `flushall`清空全部库

> Redis是单线程+多路IO重复技术

多路复用是指使用一个线程来检查多个文件描述符（Socket）的就绪状态。比如调用`select`和`poll`函数，传入多个文件描述符，如果有一个文件描述符就绪，则返回；否则阻塞直到超时。得到就绪状态后进行真正的操作可以在同一个线程里执行，也可以启动线程执行（比如使用线程池）。

---

## 常用五大数据类型

- 字符串string

- 列表list

- 集合set

- 哈希hash

- 有序集合zset

### Redis键（key）

> 常用命令

```shell
# 查看当前库所有key
keys *
# 其实*表达代表多个字符串，我们可以使用 keys k*或者keys *1进行模糊查询
keys k*
# 判断某个key是否存在
exists keyName
# 查看key存储字段类型
type keyName
# 删除指定key的数据
del keyName
# 根据value选择非阻塞删除，仅将key从keyspace元数据中删除，真正删除会在后续异步操作
unlink keyName
# 给key设置过期时间，此处为10秒
expire keyName 10
# 查看还有多少秒过期，-1表示永不过期，-2表示已经过期(或不存在)
ttl keyName
# 切换数据库
select dbIndex
# 清空当前选择的数据库
flushdb
# 清空所有数据库
flushll
```

### 字符串（String）

#### 简介

string是Redis最基本的类型，可以理解为Memcached一模一样的类型，一个key对应一个value。

string类型是**二进制安全的**。意味着Redis的string可以包含任何数据，比如jpg图片、序列化的对象。

string类型是Redis最基本的数据类型，一个Redis字符串value诸多可以是`512M`。

#### 常用命令

> set <key> <value> 

添加键值对

```shell
set key value [EX seconds|PX milliseconds|KEEPTTL] [NX|XX]
127.0.0.1:6379[3]> set k1 v100
OK
127.0.0.1:6379[3]> set k1 v200 NX
(nil)
```

`EX` key的超时秒数（默认）

`PX`key的超时毫秒数，与EX互斥

`NX` 当数据库中key不存在时，可以将`key-value`添加数据库

`XX` 当数据库中key存在时，可以将`key-value`添加数据库，与NX参数互斥（默认）

> get <key> 

查询对应键的值

```shell
get key
127.0.0.1:6379[3]> get k1
"v1100"
```

> append <key> <value>

将给定的value追加到原值末尾，返回值为最后值的长度

```shell
append key value
127.0.0.1:6379[3]> get k1
"v1100"
127.0.0.1:6379[3]> append k1 v900
(integer) 9
127.0.0.1:6379[3]> get k1
"v1100v900"
```

> strlen <key> 

获取键对应值的长度，不存在时返回0

```shell
strlen key
127.0.0.1:6379[3]> strlen k1
(integer) 9
```

> setnx <key> <value>

当key不存在时设置key值

该方法其实与`set key value nx` 一致。当插入成功时返回1，失败返回0

```shell
setnx key value
127.0.0.1:6379[3]> setnx k3 jack
(integer) 1
127.0.0.1:6379[3]> setnx k1 jack
(integer) 0
```

> incr <key>

将key中存储的数字值增1；只能对数字值操作，如果为空，新增值并且为1。**这个操作是遵循原子性的**，即，操作一旦开始就一直运行到结束，中间不会有任何`context switch`（切换到另一个线程）。

```shell
incr key
127.0.0.1:6379[3]> incr k2
(error) ERR value is not an integer or out of range
127.0.0.1:6379[3]> incr k4
(integer) 1
127.0.0.1:6379[3]> incr k4
(integer) 2
127.0.0.1:6379[3]> incr k4
(integer) 3
```

> decr <key>

将key中存储的数字值-1；只能对数字值操作，如果为空，新增值并且为-1

```shell
decr key
127.0.0.1:6379[3]> decr k2
(error) ERR value is not an integer or out of range
127.0.0.1:6379[3]> decr k5
(integer) -1
127.0.0.1:6379[3]> decr k5
(integer) -2
127.0.0.1:6379[3]> decr k4
(integer) 2
127.0.0.1:6379[3]> decr k4
(integer) 1
```

> incrby / decrby <key> <num>

将key中存储的数字值按照自己定义的num进行增减，当key不存在时，incrby命令会创建一个值为num的键值对；decrby命令会创建一个值为-num的键值对。

```shell
127.0.0.1:6379[3]> incrby k4 3
(integer) 4
127.0.0.1:6379[3]> incrby k4 3
(integer) 7
127.0.0.1:6379[3]> decrby k4 2
(integer) 5
127.0.0.1:6379[3]> decrby k4 2
(integer) 3
127.0.0.1:6379[3]> incrby k6 8
(integer) 8
127.0.0.1:6379[3]> decrby k7 8
(integer) -8
```

> mset <key> <value> [<key> <value>]

同时设置多个key-value对

```shell
127.0.0.1:6379[3]> mset mk1 mk111 mk2 mk222 mk3 mk333
OK
```

> mget <key> [<key>]

同时获取多个key的值，并且按照get的顺序进行输出

```shell
127.0.0.1:6379[3]> mget mk1 mk3 mk2
1) "mk111"
2) "mk333"
3) "mk222"
```

> msetnx <key> <value> [<key> <value>]

同时设置一个或多个key-value对，当且仅当所有给定的key都不存在时。**该操作遵循原子性，一个失败则全部失败**。成功返回1，失败返回0。

```shell
127.0.0.1:6379[3]> msetnx mk4 mk444 mk5 mk555
(integer) 1
127.0.0.1:6379[3]> msetnx mk6 mk444 mk7 mk777 mk5 mk555
(integer) 0
```

> getrange <key> <startIndex> <endIndex> 

获得值的范围，类似Java中的`substring`，取值范围`[startIndex, endIndex]`。

```shell
[db3] > get mk4
"mk444"
[db3] > getrange mk4 1 3
"k44"
[db3] > GETRANGE mk4 0 100
"mk444"
[db3] > GETRANGE mk4 30 100
""
```

> setrange <key> <startInex> <value>

用value覆写key所存储的字符串值，从startIndex开始。

```shell
[db3] > get mk4
"mk444"
[db3] > setrange mk4 3 abc
(integer) 6
[db3] > get mk4
"mk4abc"
```

> setxt <key> <timeout> <value>

设置键值的同时设置过期时间，单位秒

```shell
[db3] > setex mk6 10 v6
"OK"
```

> getset <key> <value>

以新换旧，设置新值同时获得旧的值

```shell
[db3] > getset mk5 v5
"mk555"
[db3] > get mk5
"v5"
```

#### 数据结构

string的数据结构为简单动态字符串（Simple Dynamic String，即SDS）。是可以修改的字符串，内部结构实现上类似于Java的ArrayList，采用预分配冗余空间的方式来减少内存的频繁分配。

![redis string](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis3-string.png)

如图所示，内部为当前字符串实际分配的空间`capacity`一般要高于实际字符串长度`length`。当字符串长度小于1M是，扩容都是加倍现有的空间，如果超过1M，扩容时一次只会扩1M的空间。字符串最大长度为512M。

---

### 列表（List）

#### 简介

单键多值。

Redis列表是简单的字符串列表，按照插入顺序排序，可以添加一个元素到列表的头部（左侧）或者尾部（右侧）。

它的底层实际是个**双向链表**，对两段的操作性能很高，通过索引下标的操作，中间的节点性能可能会较差（查询效率低）。

![redis list](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis4-list.png)

#### 常用命令

> lpush/rpush key element [element]

从左边/右边插入一个或多个值，返回列表的长度

```shell
[db3] > lpush l1 a
(integer) 1
[db3] > rpush l1 b c d
(integer) 4
[db3] > lpush l1 e f
(integer) 6
```

最终列表结构

| 0   | 1   | 2   | 3   | 4   | 5   |
| --- | --- | --- | --- | --- | --- |
| f   | e   | a   | b   | c   | d   |

> lpop/rpop key [count]

从左边/右边删除count个值，不指定count则默认为1；返回被删除的值。**值在键在，值光键亡**。

```shell
[db3] > lpop l1 
"f"
[db3] > rpop l1 2
1) "d"
2) "c"
[db3] > lrange l1 0 -1
1) "e"
2) "a"
3) "b"
```

> rpoplpush key1 key2

从key1列表右边删除一个值，并将被删除的值插入到key2列表左边

```shell
[db3] > rpoplpush l1 l2
"v3"
```

> lrange key start end

按照索引下标获得元素（从左到右），end为-1时，表示取所有的值

```shell
[db3] > lrange l1 0 -1
1) "f"
2) "e"
3) "a"
4) "b"
5) "c"
6) "d"
[db3] > lrange l1 0 3
1) "f"
2) "e"
3) "a"
4) "b"
```

> lindex key index

根据索引下标index获取key的元素（从左到右）

```shell
[db3] > lindex l1 1
"v2"
```

> llen key

获取list长度

```shell
[db3] > llen l1
(integer) 2
```

> linsert key before|after value newValue

在value 前|后 面插入newValue，方法返回插入后的数组长度

```shell
[db3] > LINSERT l1 before v2 v2.5
(integer) 4
[db3] > lrange l1 0 -1
1) "v1"
2) "v2.5"
3) "v2"
4) "v3"
[db3] > LINSERT l1 after v2 v2.6
(integer) 5
[db3] > lrange l1 0 -1
1) "v1"
2) "v2.5"
3) "v2"
4) "v2.6"
5) "v3"
```

> lrem key count element

从左边起，删除count个element，返回删除个数。如果没有匹配则返回0

```shell
[db3] > lrange l1 0 -1
1) "v1"
2) "v2"
3) "v2"
4) "v2"
5) "v2.5"
6) "v2.6"
7) "v3"
[db3] > lrem l1 2 v2
(integer) 2
[db3] > lrange l1 0 -1
1) "v1"
2) "v2"
3) "v2.5"
4) "v2.6"
5) "v3"
```

> lset key index element

将key对应的数组下标index替换为element

```shell
[db3] > lrange l1 0 -1
1) "v1"
2) "v2"
3) "v2.5"
4) "v2.6"
5) "v3"
[db3] > lset l1 2 v2.4
"OK"
[db3] > lrange l1 0 -1
1) "v1"
2) "v2"
3) "v2.4"
4) "v2.6"
5) "v3"
```

#### 数据结构

list的数据结构为快速链表`quickList`

首先在列表元素较少的情况下会使用一块连续的内存存储，这个结构是`ziplist`，即压缩列表。

它将所有的元素紧挨着一起存储，分配的是一块连续的内存。

当数据量比较多时会改成`quicklist`。

因为普通的链表的附加指针空间太大，会比较浪费空间。比如列表里存储的只是int类型的数据，结构上还需要两个额外的指针prev和next。

`ziplist` <==>`ziplist`<==>`ziplist`

Redis将链表和`ziplist`结合起来组成了`quicklist`。也就是将多个ziplist使用双向指针穿起来使用，这样既满足了快速的插入删除性能，又不会出现太大的空间冗余。

---