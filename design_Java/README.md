# 设计模式

## 23种设计模式

### 创建型模式（Creational Patterns）

1. **单例模式（Singleton）** - 确保一个类只有一个实例，并提供一个全局访问点。
2. **工厂方法模式（Factory Method）** - 定义一个用于创建对象的接口，让子类决定实例化哪一个类。
3. **抽象工厂模式（Abstract Factory）** - 创建相关或依赖对象的家族，而不需明确指定具体类。
4. **建造者模式（Builder）** - 构建一个复杂的对象，并允许按步骤构造。
5. **原型模式（Prototype）** - 通过拷贝现有的实例创建新的实例，而不是通过新建。

### 结构型模式（Structural Patterns）

6. **适配器模式（Adapter）** - 允许对象间的接口不兼容问题。
7. **装饰器模式（Decorator）** - 动态地给一个对象添加额外的职责。
8. **代理模式（Proxy）** - 为其他对象提供一个代替或占位符以控制对它的访问。
9. **外观模式（Facade）** - 提供一个统一的高层接口，用于访问子系统中的一群接口。
10. **桥接模式（Bridge）** - 将抽象部分与其实现部分分离，使它们可以独立地变化。
11. **组合模式（Composite）** - 将对象组合成树形结构以表示“部分-整体”的层次结构。
12. **享元模式（Flyweight）** - 通过共享来高效地支持大量细粒度的对象。

### 行为型模式（Behavioral Patterns）

13. **策略模式（Strategy）** - 定义一系列算法，把它们一个个封装起来，并使它们可互换。
14. **模板方法模式（Template Method）** - 在方法中定义算法的框架，延迟到子类中实现。
15. **观察者模式（Observer）** - 对象间的一对多依赖关系，当一个对象改变状态时，所有依赖于它的对象都会得到通知并自动更新。
16. **迭代器模式（Iterator）** - 顺序访问一个聚合对象中的各个元素，不暴露其内部的表示。
17. **责任链模式（Chain of Responsibility）** - 使多个对象都有机会处理请求，从而避免请求的发送者和接收者之间的耦合关系。
18. **命令模式（Command）** - 将请求封装为一个对象，从而使用户可用不同的请求对客户进行参数化。
19. **备忘录模式（Memento）** - 在不破坏封装性的前提下，捕获一个对象的内部状态，并在该对象之外保存这个状态。
20. **状态模式（State）** - 允许一个对象在其内部状态发生改变时改变其行为。
21. **访问者模式（Visitor）** - 对象结构中的元素对象分别作用于一个访问者对象，让这个访问者对象决定如何处理每一个元素。
22. **中介者模式（Mediator）** - 定义一个中介对象来简化原有对象的交互。
23. **解释器模式（Interpreter）** - 定义一个语言的文法，并构建一个解释器，这个解释器可以解释和执行语言中的句子。

## 各个模式的说明

### 迭代器模式（Iterator）

#### 设计模式说明

![迭代器模式类图](https://gitee.com/Jayce_Lan/some_img/raw/master/design/iterator.png)

> `Iterator` 迭代器

该角色负责定义按顺序逐个遍历元素的接口（API）。它定义了`hasNext`和`next` 两个方法。其中，`hasNext` 方法用语判断是否存在下一个元素，`next`方法则用于获取该元素。

```java
public interface Iterator {
    abstract boolean hasNext();
    abstract Object next();
}
```

> `ConcreteIterator` 具体的迭代器

该角色负责实现 `Iterator`角色所定义的接口。该角色中包含了遍历集合所必须的信息。

`BookShelf` 类的实例保存在 `bookShelf` 字段中，被指向书的下标保存在`index` 字段中。

```java
public class BookShelfIterator implements Iterator {
    private BookShelf bookShelf;
    private Integer index;

    public BookShelfIterator(BookShelf bookShelf) {
        this.bookShelf = bookShelf;
        this.index = 0;
    }

    @Override
    public boolean hasNext() {
        return index < bookShelf.getLength();
    }

    @Override
    public Object next() {
        Book book = bookShelf.getBookAt(this.index);
        this.index++;
        return book;
    }
}
```

> `Aggregate` 集合

该角色负责定义创建`Iterator` 角色的接口。这个接口是一个方法，会创建出“按顺序访问保存在自身内部元素的对象”。

```java
public interface Aggregate {
    /**
     * 需要遍历集合元素时，可以调用该方法来生成一个实现了 Iterator 接口的类的实例
     * @return 生成一个用于遍历集合的迭代器
     */
    abstract Iterator iterator();
}
```

> `ConcreteAggregate` 具体的集合

该角色负责实现 `Aggregate` 角色所定义的接口。它会创建具体的 `Iterator` 角色，即`ConcreteIterator` 角色。它实现了`itreator` 方法。

```java
public class BookShelf implements Aggregate {
    private List<Book> books;
//    private Book[] books; // 如果使用数组而不是List会无法存储大于初始定义长度的对象
    private int last = 0;

    public BookShelf(int maxSize) {
        this.books = new ArrayList<Book>(maxSize);
//        this.books = new Book[maxSize];
    }

    public Book getBookAt(int index) {
        return books.get(index);
//        return books[index];
    }

    public void appendBook(Book book) {
        this.books.add(book);
//        this.books[last] = book;
        last++;
    }

    public int getLength() {
        return last;
    }

    @Override
    public Iterator iterator() {
        return new BookShelfIterator(this);
    }
}
```

#### 扩展思路

> 为什么要引入设计模式

其实直接使用for循环也可以实现遍历，而在集合之外引入`Iterator` 这个角色，旨在于可以将遍历与实现分开来。

```java
while (iterator.hasNext()) {
    log.info("item >>>>> {}", iterator.next());
}
```

这里只用了`Iterator` 的`hasNext`方法和 `next`方法，并没有调用 `BookShelf` 的方法，也就是说，**while循环并不依赖于BookShelf的实现**。

如果往下决定弃用数组/List管理而用 `java.util.Vector` ，不管BookShelf如何变幻，只要它的`iterator` 方法能正确返回Iterator实例，即使不改变while循环，代码仍然可用。

> 抽象类和接口

使用接口与实现关系，是为了解耦合。如果只使用实现类，容易导致类之间有强耦合，所有类都难以被再次利用。为了弱化类之间的耦合，进而使得类更加容易作为组件被再次利用，需要引入抽象类和接口。这也是贯穿设计模式的思想。

---

### 适配器模式（Adapter）

Adapter模式有以下两种

- 类适配器模式（使用继承的适配器）

- 对象适配器模式（使用委托的适配器）

#### 使用继承的适配器

假设我们的插座提供的是100伏特的交流电，而我们的设备需要使用12伏特的直流电，那么使用过程中肯定需要电源适配器

|      | 电源的比喻   | 示例程序                                 |
| ---- | ------- | ------------------------------------ |
| 实际情况 | 交流100伏特 | Banner类（showWithParen、showWithAster） |
| 变换装置 | 适配器     | PrintBanner类                         |
| 需求   | 直流12伏特  | Print接口（printWeak、printStrong）       |

![使用继承的适配器](https://gitee.com/Jayce_Lan/some_img/raw/master/design/adapter-01.png)

对于`Main`类而言，`Banner`类以及它的`showWithParen`/`showWithAster` 方法被完全隐藏。就像电器只需要在直流12伏特的电压下就能正常工作，但它并不知道这12伏特的电压是由适配器将100伏特交流电转换而成的。

`Main` 类并不知道 `PrintBanner` 类是如何实现的，这样就可以在不用对`Main`类进行修改的情况下改变`PrintBanner`类的具体实现。

> Banner

最终需要使用到（调用到）的代码。

```java
public class Banner {
    private static final Logger log = LogManager.getLogger(Banner.class);

    private String printMsg;

    public Banner(String printMsg) {
        this.printMsg = printMsg;
    }

    public void showWithParen() {
        log.info("({})", this.printMsg);
    }

    public void showWithAster() {
        log.info("*{}*", this.printMsg);
    }
}
```

> Print接口

需求接口，即我们实际接触并使用的工具。

```java
public interface Print {
    void printWeak();
    void printStrong();
}
```

> PrintBanner

该类扮演了适配器的角色。继承了Banner；同时实现了Print接口。

```java
public class PrintBanner extends Banner implements Print {
    public PrintBanner(String printMsg) {
        super(printMsg);
    }

    @Override
    public void printWeak() {
        super.showWithParen();
    }

    @Override
    public void printStrong() {
        super.showWithAster();
    }
}
```

#### 使用委托的适配器

![适配器模式](https://gitee.com/Jayce_Lan/some_img/raw/master/design/adapter02.png)

委托其实就是“交给其他人”。在这里，Banner与上面的相同，与上面继承+接口不同的是，这里的Print不是接口而同为实体类。但是Java中无法同时继承两个类，只能单一继承，因此无法将PrintBanner定义为Print和Banner的子类。

PrintBanner类声明的`banner` 对象保存了Banner类的实例，该实例是在PrintBanner的构造函数中生成的。printWeak方法和printStrong方法会通过banner对象调用Banner类的showWithParen和showWithAster方法。

> Print

将Print定义为一个抽象类，这样就会与接口一样，只需要声明方法而不需要实现，交由子类实现它的方法。

```java
public abstract class Print02 {
    public abstract void printWeak();
    public abstract void printStrong();
}
```

> PrintBanner

声明`banner` 实例，并通过实例去实现继承的方法，从而达到适配。

```java
public class PrintBanner02 extends Print02 {
    private Banner banner;

    public PrintBanner02(String pringMsg) {
        this.banner = new Banner(pringMsg);
    }

    @Override
    public void printWeak() {
        banner.showWithParen();
    }

    @Override
    public void printStrong() {
        banner.showWithAster();
    }
}
```

#### 设计模式说明

![适配器模式](https://gitee.com/Jayce_Lan/some_img/raw/master/design/adapter03.png)

> Target（对象）

该角色负责定义所需的方法。即让电脑正常工作所需的直流12伏特电源，在程序实例中对应的是Print接口（使用继承时）和Print类（使用委托时）扮演的角色。

> Client（请求者）

该角色负责使用Target角色所定义的方法进行具体处理。相当于直流12伏特电源驱动的笔记本电脑，在程序实例中对应的是Main类扮演的角色。

> Adaptee（被适配）

此处不是Adapt-er（适配）角色，而是Adapt-ee（被适配）角色，Adaptee是一个持有既定方法的角色。相当于交流100伏特电源，在程序实例中对应的是Banner类。

如果Adaptee角色中的方法与Target角色的方法相同（也就是说电压已经是12伏特直流电压），就不需要接下来的Adapter角色了。

> Adapter（适配）

Adapter模式的主人公，使用Adaptee角色的方法来满足Target角色的需求，这是Adapter模式的目的，也是Adapter角色的作用。相当于将交流100伏特电压转换为直流12伏特电压的适配器，在程序实例中对应的是PrintBanner类。

在类适配器模式中，Adapter角色通过继承来使用Adaptee角色；而在对象适配器模式中，Adapter角色通过委托（创建对象）来使用Adaptee角色。

#### 设计思路

软件的生命周期总是伴随着版本升级，而在版本升级中会出现兼容问题。我们基本不会完全抛弃旧版本，这时我们就需要使用适配器模式进行版本兼容。

例如我们往后只想维护新版本，这时可以让新版本承担Adaptee角色，旧版本扮演Target角色，编写一套Adapter的类，让它使用新版本的类去实现旧版本的方法。

当然，Adaptee角色和Target角色功能完全不同时，适配器模式无法使用。就如同无法使用交流100伏特电压转换出自来水。

---