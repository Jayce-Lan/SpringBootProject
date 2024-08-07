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

### 集合（Set）

#### 简介

Redis set对外提供的功能与list类似，是一个列表的功能，特殊之处在于set是可以**自动去重**的，但需要存储一个列表数据，又不希望出现重复数据时，set是一个很好的选择，并且set提供了判断某个成员是否在一个set集合内的总要接口，这个也是list说不能提供的。

Redis的set是string的**无序集合**。**它的底层是一个value为null的hash表**，所以添加、删除、查找的**复杂度都是O<sub>(1)</sub>**。

一个算法，随着数据的增加，执行时间的长短会根据复杂度增减；如果是O<sub>(1)</sub>，数据增加，查找数据的时间不变。

#### 常用命令

> sadd key member [member]

将一个或多个member加入集合key中，已经存在的将被忽略。返回添加成功member的个数

```shell
[db3] > sadd s1 v1 v2 v3 v4 v2
(integer) 4
[db3] > sadd s1 v5
(integer) 1
```

> smembers key

获取该集合所有的值

```shell
[db3] > smembers s1
1) "v1"
2) "v2"
3) "v3"
4) "v4"
5) "v5"
```

> sismember key member

判断key中是否存在member，存在返回1；不存在返回0

```shell
[db3] > SISMEMBER s1 v4
(integer) 1

[db3] > SISMEMBER s1 v6
(integer) 0
```

> scard key

返回该集合的元素个数

```shell
[db3] > scard s1
(integer) 5
```

> srem key member [member]

删除集合中的某个指定元素。当元素不存在时，不做删除；返回删除成功个数。与list一样，**值在键在，值空键亡**。

```shell
[db3] > srem s1 v1 v6
(integer) 1

[db3] > srem s1 v2 v3
(integer) 2
```

> spop key [count]

从key集合中**随机删除**count个元素，count不录入时默认为1，返回被删除元素。

```shell
[db3] > sadd s1 v1 v2 v3 v4 v5 v6
(integer) 6
[db3] > spop s1 3
1) "v2"
2) "v3"
3) "v5"
[db3] > spop s1
"v6"
```

> srandmember key [count]

从集合中随机获取count个值，count不指定时默认为1；不会删除集合元素。

```shell
[db3] > srandmember s1 3
1) "v4"
2) "v5"
3) "v6"
[db3] > srandmember s1 3
1) "v1"
2) "v2"
3) "v6"
[db3] > srandmember s1
"v1"
```

> smove source destination member

将source集合的member元素从source移动到destination当中，成功返回1，失败返回0。

```shell
[db3] > smembers s1
1) "v1"
2) "v2"
3) "v3"
4) "v4"
5) "v5"
6) "v6"
[db3] > smembers s2
1) "vv1"
2) "vv2"
3) "vv3"
4) "vv4"
5) "vv5"

[db3] > smove s1 s2 v1
(integer) 1

[db3] > smembers s1
1) "v2"
2) "v3"
3) "v4"
4) "v5"
5) "v6"
[db3] > smembers s2
1) "vv1"
2) "vv2"
3) "vv3"
4) "vv4"
5) "vv5"
6) "v1"

[db3] > smove s1 s2 v1
(integer) 0
```

> sinter key [key]

返回多个集合中的交集元素

```shell
[db3] > sadd s1 v1 v2 v3 v4
(integer) 4

[db3] > sadd s2 v1 v2 v5 v6
(integer) 4

[db3] > sinter s1 s2
1) "v1"
2) "v2"
```

> sunion key [key]

返回多个集合中的并集元素

```shell
[db3] > sunion s1 s2
1) "v1"
2) "v2"
3) "v3"
4) "v4"
5) "v5"
6) "v6"
```

> sdiff key [key]

返回多个集合的差集，只展示首个key中所含的差集元素

```shell
[db3] > sdiff s1 s2
1) "v3"
2) "v4"

[db3] > sdiff s2 s1
1) "v5"
2) "v6"
```

#### 数据结构

set数据结构是`dict`字典，字典是用hash表实现的。

Java中`HashSer`的内部实现使用的是`HashMap`，只不过所有的value都指向同一个对象。Redis的set结构也是一样，它的内部也使用hash结构，所有的value都指向同一个内部值。

---

### 哈希（Hash）

#### 简介

Redis hash是一个键值对集合。

Redis hash是一个string类型的`field`和`value`的映射表，hash特别适用于存储对象。类似Java中的`Map<String, Object>`。

![redis hash](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis5-hash.png)

用户ID为查找的key，存储的value包括用户的姓名、年龄、生日等信息，如果用普通的key-value结构存储可能会造成数据冗余。

> hash与string的存储对比

![redis hash and string](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis6-hash.png)

还有一种形式是**序列化对象**，然后以key-value形式存入string中。但是如果对象是经常需要修改某个属性的情况下，需要先方序列化对象，修改后再序列化存储，会造成较大的资源开销。

相比之下，hash存储不会那么冗余，读取也更方便。

#### 常用命令

> hset key field value [field value]

给key对象hash中进行赋值，相当于声明属性，可以声明多个；其中field为属性名，value为属性值，返回添加成功属性个数。

```shell
[db3] > hset user001 name Tony
(integer) 1

[db3] > hset user001 age 20 tel 130xxxx bir 0626
(integer) 3
```

> hget key field

取出hash中的field属性值

```shell
[db3] > hget user001 name
"Tony"
```

> hmset key fild value [field value]

批量设置hash的属性与值，其实与hset类似；但是插入成功时返回OK，而不是具体数量

```shell
[db3] > hmset user002 name Jack age 30 tel 140xxxx bir 1213
"OK"
```

> hexists key field

查看对应key的hash中给定的field是否存在，存在返回1，不存在返回0

```shell
[db3] > hexists user001 name
(integer) 1

[db3] > hexists user001 job
(integer) 0
```

> hkeys key / hvals key

列出对应key的hash的field/value

```shell
[db3] > hkeys user001
1) "name"
2) "age"
3) "tel"
4) "bir"

[db3] > hvals user001
1) "Tony"
2) "20"
3) "130xxxx"
4) "0626"
```

> hincrby key field increment

为对应key的hash中的field属性增加increment个值，1为+1、-1为-1，以此类推；返回增加/减小后的结果

```shell
[db3] > hincrby user001 age 1
(integer) 21

[db3] > hincrby user001 age 3
(integer) 24

[db3] > hincrby user001 age -1
(integer) 23
```

> hsetnx key field value

将hash中的field的值设置为value，当且仅当field不存在。成功返回1，失败返回0。

```shell
[db3] > hsetnx user001 name Jonny
(integer) 0

[db3] > hsetnx user001 lastName Jonny
(integer) 1
```

#### 数据结构

hash类型对应的数据结构是两种：`ziplist` （压缩列表）、`hashtable`（hash表）。当field-value长度较短且个数较少时，使用ziplist，否则使用hashtable。

---

### 有序集合（Zset）

#### 简介

Redis有序集合zset（sorted set）与普通集合set非常相似，是一个**没有重复元素**的字符串集合。

不同之处是有序集合的每个成员都关联了一个**评分（score）**，这个评分被用来按照从最低分到最高分的方式排序集合中的成员。**集合的成员是唯一的，但是评分是可以重复的**。

因为元素是有序的，所以可以根据评分或者次序（position）来获取一个范围的元素。

访问有序集合中的中间元素也是非常快的，因此能够使用有序集合作为一个没有重复成员的只能列表。

#### 常用命令

> zadd key [NX | XX] [GT | LT] [CH] [INCR] score member [score member]

将一个或多个member按照排序分数score加入到有序集合key中，返回插入个数。

```shell
[db3] > zadd z1 1 v1 2 v2 3 v3
(integer) 3
```

> zrange key start stop [BYSOCORE | BYLEX] [REV] [LIMIT offset count] [WITHSCORES]

返回游戏集合key中下标在`[start, stop]`之间的元素，stop为-1时为取全部。

WITHSCORES: 让score和对应的集合内的元素一起返回。

```shell
[db3] > zrange z1 0 1
1) "v1"
2) "v2"
[db3] > zrange z1 0 1 withscores
1) "v1"
2) "1"
3) "v2"
4) "2"
```

> zrangebyscore key min max [WITHSCORES] [LIMIT offset count]

返回有序集合key中，所有score在`[min, max]`之间的成员。有序集合成员按score值递增（从小到大）次序排练。

```shell
[db3] > zrangebyscore z1 1 3
1) "v1"
2) "v2"
3) "v3"
```

> zrevrangeby key max min [WITHSCORES] [LIMIT offset count]

同上，但是排练顺序为由大到小。

```shell
[db3] > zrevrangebyscore z1 3 1
1) "v3"
2) "v2"
3) "v1"
```

> zincrby key increment member

为member元素的score加上增量increment，返回增加后的元素的score。

```shell
[db3] > zincrby z1 1 v1
"2"

[db3] > zincrby z1 5 v1
"7"
```

> zrem key member [member ...]

删除key集合下指定元素，返回删除成功元素个数

```shell
[db3] > zrem z1 v1 v2
(integer) 2
```

> zcount key min max

统计该集合下分数区间`[min, max]`区间元素个数

```shell
[db3] > zcount z1 2 3
(integer) 2
```

> zrank key member [WITHSCORE]

返回元素在集合内的排名，从0开始，由小到大。

```shell
[db3] > zrank z1 v1
(integer) 2

[db3] > zrank z1 v1 withscore
1) "2"
2) "7"
```

#### 数据结构

zset（SortedSet）是Redis提供的一个非常特别的数据结构，一方面它等价于Java数据结构`Map<String, Double>`，可以给每一个元素value赋予一个权重score；另一方面它又类似于TreeSet，内部的元素会按照权重score进行排序，可以得到每个元素的名词，还可以通过score的范围来获取元素的列表。

zset底层使用了两个数据结构：

- hash，hash的作用就是关联元素value和权重score，保障元素value的唯一性，可以通过元素value找到对应的score值

- 跳跃表，跳跃表的目的在于给元素value排序，根据score的范围获取元素列表

#### 跳跃表（跳表）

> 简介

有序集合在生活中比较常见，录入根据成绩对学生排名、根据得分对玩家排名等。对于有序集合的底层实现，可以用数组、平衡数、链表等。数组不便于元素的插入、删除；平衡树或红黑树虽然效率高但是结构复杂；链表查询需要遍历所有，效率低。Redis采用的是跳跃表。跳跃表效率堪比红黑树，实现远比红黑树简单。

> 实例

对比有序链表和跳跃表，从表中查询出51

- 有序链表
  
  - `1`=>`11`=>`21`=>`31`=>`41`=>`51`=>`61`=>null
  
  - 要查找值为51的元素，需要从第一个元素开始依次查找、比较才能找到。共需要6次比较。

- 跳跃表
  
  - 跳跃表查找方式
  
  - ![redis zset](https://gitee.com/Jayce_Lan/some_img/raw/master/RedisLearnImg/redis7-zset.png)
  
  - 从第2层开始，1节点比51节点小，向后比较
  
  - 21节点比51节点小，继续向后比较，后面就是null了，所以从21节点向下到第1层
  
  - 在第1层，41节点比51节点小，继续向后，61节点比51节点大，所以从41向下
  
  - 在第0层，51节点为要查找的点，节点被找到，共查询4次。

由此可见，跳跃表比有序链表效率要高。

---

## 配置文件

### 配置文件所在位置

根据系统的不同，Redis配置文件`redis.conf`会存储在不同位置。

> Linux和Windows

Redis的配置文件通常位于Redis的安装目录下，文件名为`redis.conf`。具体位置可能因操作系统和安装方式的不同而有所变化，但以下是几个常见的默认位置：

- **Ubuntu等Linux发行版**：如果使用`apt`或其他包管理器安装，配置文件通常位于`/etc/redis/redis.conf`。
- **手动安装的Linux系统**：配置文件可能位于Redis安装目录的`conf`子目录下，或直接在安装根目录下，例如`/usr/local/redis/conf/redis.conf`。
- **Windows系统**：配置文件名为`redis.windows.conf`，位置依据Redis的安装路径而定，如果使用zip包解压安装，它通常位于解压目录内。

如果不确定配置文件的具体位置，可以尝试以下几个方法找到它：

1. **查看Redis服务的启动脚本或服务定义文件**（如`/etc/systemd/system/redis.service`或`/etc/init.d/redis`），这些文件中可能会指明配置文件的路径。
2. **使用命令行查找**：在Linux系统中，可以尝试使用`find`命令全局查找，或者如果Redis服务正在运行，可以通过`ps aux | grep redis-server`查看启动命令行参数中是否包含了配置文件的路径。
3. **直接询问Redis服务器**：如果可以访问Redis服务器，可以使用`CONFIG GET dir`命令获取Redis的数据目录，配置文件通常位于此目录或其附近。

请根据你的实际情况检查上述路径或使用相应的方法定位配置文件。

> brew安装的Redis

如果是macOS，会使用`homebrew`进行Redis的安装，此时执行以下命令可以查询Redis配置文件、安装路径等信息

```shell
macbook / % brew info redis
==> redis: stable 7.2.4 (bottled), HEAD
Persistent key-value database, with built-in net interface
https://redis.io/
/opt/homebrew/Cellar/redis/7.2.4 (14 files, 2.4MB) *
  Poured from bottle using the formulae.brew.sh API on 2024-03-28 at 08:47:56
From: https://github.com/Homebrew/homebrew-core/blob/HEAD/Formula/r/redis.rb
License: BSD-3-Clause
==> Dependencies
Required: openssl@3 ✔
==> Options
--HEAD
        Install HEAD version
==> Caveats
To restart redis after an upgrade:
  brew services restart redis
Or, if you don't want/need a background service you can just run:
  /opt/homebrew/opt/redis/bin/redis-server /opt/homebrew/etc/redis.conf
```

### Units单位

配置大小单位，开头定义了一些基本的度量单位，只支持`bytes` ，不支持`bit`，大小写不敏感。

```shell
# Note on units: when memory size is needed, it is possible to specify
# it in the usual form of 1k 5GB 4M and so forth:
#
# 1k => 1000 bytes
# 1kb => 1024 bytes
# 1m => 1000000 bytes
# 1mb => 1024*1024 bytes
# 1g => 1000000000 bytes
# 1gb => 1024*1024*1024 bytes
#
# units are case insensitive so 1GB 1Gb 1gB are all the same.
```

### INCLUDES包含内容

可以使用`include`关键字引入外部配置文件，使得Redis配置包含这些引入配置的内容。

```shell
################################## INCLUDES ###################################

# Include one or more other config files here.  This is useful if you
# have a standard template that goes to all Redis servers but also need
# to customize a few per-server settings.  Include files can include
# other files, so use this wisely.
#
# Note that option "include" won't be rewritten by command "CONFIG REWRITE"
# from admin or Redis Sentinel. Since Redis always uses the last processed
# line as value of a configuration directive, you'd better put includes
# at the beginning of this file to avoid overwriting config change at runtime.
#
# If instead you are interested in using includes to override configuration
# options, it is better to use include as the last line.
#
# include /path/to/local.conf
# include /path/to/other.conf
```

### NETWORK网络相关配置

```shell
################################## NETWORK #####################################

# By default, if no "bind" configuration directive is specified, Redis listens
# for connections from all available network interfaces on the host machine.
# It is possible to listen to just one or multiple selected interfaces using
# the "bind" configuration directive, followed by one or more IP addresses.
# Each address can be prefixed by "-", which means that redis will not fail to
# start if the address is not available. Being not available only refers to
# addresses that does not correspond to any network interfece. Addresses that
# are already in use will always fail, and unsupported protocols will always BE
# silently skipped.
#
# Examples:
#
# bind 192.168.1.100 10.0.0.1     # listens on two specific IPv4 addresses
# bind 127.0.0.1 ::1              # listens on loopback IPv4 and IPv6
# bind * -::*                     # like the default, all available interfaces
#
# ~~~ WARNING ~~~ If the computer running Redis is directly exposed to the
# internet, binding to all the interfaces is dangerous and will expose the
# instance to everybody on the internet. So by default we uncomment the
# following bind directive, that will force Redis to listen only on the
# IPv4 and IPv6 (if available) loopback interface addresses (this means Redis
# will only be able to accept client connections from the same host that it is
# running on).
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT OUT THE FOLLOWING LINE.
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 127.0.0.1 ::1

# Protected mode is a layer of security protection, in order to avoid that
# Redis instances left open on the internet are accessed and exploited.
#
# When protected mode is on and if:
#
# 1) The server is not binding explicitly to a set of addresses using the
#    "bind" directive.
# 2) No password is configured.
#
# The server only accepts connections from clients connecting from the
# IPv4 and IPv6 loopback addresses 127.0.0.1 and ::1, and from Unix domain
# sockets.
#
# By default protected mode is enabled. You should disable it only if
# you are sure you want clients from other hosts to connect to Redis
# even if no authentication is configured, nor a specific set of interfaces
# are explicitly listed using the "bind" directive.
protected-mode yes

# Accept connections on the specified port, default is 6379 (IANA #815344).
# If port 0 is specified Redis will not listen on a TCP socket.
port 6379

# TCP listen() backlog.
#
# In high requests-per-second environments you need a high backlog in order
# to avoid slow clients connection issues. Note that the Linux kernel
# will silently truncate it to the value of /proc/sys/net/core/somaxconn so
# make sure to raise both the value of somaxconn and tcp_max_syn_backlog
# in order to get the desired effect.
tcp-backlog 511

# Unix socket.
#
# Specify the path for the Unix socket that will be used to listen for
# incoming connections. There is no default, so Redis will not listen
# on a unix socket when not specified.
#
# unixsocket /run/redis.sock
# unixsocketperm 700

# Close the connection after a client is idle for N seconds (0 to disable)
timeout 0

# TCP keepalive.
#
# If non-zero, use SO_KEEPALIVE to send TCP ACKs to clients in absence
# of communication. This is useful for two reasons:
#
# 1) Detect dead peers.
# 2) Force network equipment in the middle to consider the connection to be
#    alive.
#
# On Linux, the specified value (in seconds) is the period used to send ACKs.
# Note that to close the connection the double of the time is needed.
# On other kernels the period depends on the kernel configuration.
#
# A reasonable value for this option is 300 seconds, which is the new
# Redis default starting with Redis 3.2.1.
tcp-keepalive 300
```

> bind

- 默认情况下bind=127.0.01只能接受本机的访问请求

- 不写的情况下无限制接受任何ip地址访问

- 生产环境需要配置成服务器地址；服务器是需要远程访问的，所以需要将其注释（注释掉bind 127.0.0.1）

> protected-mode

如果开启了`protected-mode`，那么在没有设定bind ip且没有密码的情况下，Redis只允许接受本机的响应（`protected-mode yes`）。需要支持远程访问需要设置为`protected-mode no`

> tcp-backlog

设置tcp的backlog，backlog其实是一个连接队列

```shell
backlog队列总和 = 未完成三次握手队列 + 已完成三次握手队列
```

在高并发环境下，需要一个高backlog值来避免客户端连接问题。

注意，Linux内核会将这个值减小到`/proc/sys/net/core/somaxconn`的值（128），所以需要确认增大`/proc/sys/net/core/somaxconn`和`/proc/sys/net/ipv4/tcp_max_syn_backlog`（128）两个值来达到想要的效果。

> timeout

客户端闲置 N 秒后关闭连接（0 表示禁用）。以秒作为监听单位，没有默认值，在Redis未指定该值时不会做监听。

> tcp-keepalive

如果非零，则使用 `SO_KEEPALIVE` 向客户机发送 `TCP ACK`，以避免通信中断。向客户端发送 TCP ACK。这样做有两个好处:

1) 检测死机的对等设备。
2) 迫使中间的网络设备认为连接是存活的

在 Linux 上，指定值（秒）是用于发送 ACK 的时间。请注意，关闭连接需要双倍的时间。在其他内核上，周期取决于内核配置。该选项的一个合理值是 300 秒，这是新的Redis 默认值。

### GENERAL通用