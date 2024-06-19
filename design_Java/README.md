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

### 模板方法模式（Template Method）

#### 设计模式实现

模板的原意是指带有镂空文字的塑料板，只要用笔在模板镂空处进行临摹就能写出整齐的文字。

在Template Method模式中，组成模板的方法被定义在父类中。由于都是抽象方法，所以只查看父类是无法知道方法的具体实现的，只能知道父类如何调用这些方法。

实现这些抽象方法的是子类，在之类中实现了抽象方法也决定了具体的处理。只要在不同的子类中实现不同的具体处理，当父类的模板方法被调用时程序行为也会不同。但是无论如何实现，处理流程都按照父类定义进行。

如上述所说**在父类中定义流程处理的框架，在子类中实现具体处理**的模式称为**Template Method模板模式**。

| 类名              | 说明                                        |
| --------------- | ----------------------------------------- |
| AbstractDisplay | 定义了open、print、close抽象方法，并且只实现了display的抽象类 |
| CharDisplay     | 实现了open、print、close方法的类                   |
| StringDisplay   | 实现了open、print、close方法的类                   |
| Main            | 测试程序行为的类                                  |

![模板模式](https://gitee.com/Jayce_Lan/some_img/raw/master/design/template-method.png)

> AbstractDisplay

定义了模板，并且抽象类定义的抽象方法，即使被new了本身，也需要实现。

```java
public abstract class AbstractDisplay {
    public abstract void open();
    public abstract void print();
    public abstract void close();

    public final void display() {
        open();
        for (int i = 0; i < 5; i++) {
            print();
        }
        close();
    }
}
```

> CharDisplay

```java
public class CharDisplay extends AbstractDisplay {
    private Logger log = LogManager.getLogger(CharDisplay.class);

    private char aChar;

    public CharDisplay(char aChar) {
        this.aChar = aChar;
    }

    @Override
    public void open() {
        log.info("== char open ==");
    }

    @Override
    public void print() {
        log.info("char >>>>> {}", this.aChar);
    }

    @Override
    public void close() {
        log.info("== char close ==");
    }
}
```

> StringDisplay

```java
public class StringDisplay extends AbstractDisplay {
    private Logger log = LogManager.getLogger(StringDisplay.class);

    private String string;

    public StringDisplay(String string) {
        this.string = string;

    }

    @Override
    public void open() {
        log.info("== string open ==");
    }

    @Override
    public void print() {
        log.info("string print >>>>> {}", this.string);
    }

    @Override
    public void close() {
        log.info("== string close ==");
    }
}
```

#### 设计模式说明

![模板模式](https://gitee.com/Jayce_Lan/some_img/raw/master/design/template-method02.png)

> AbstractClass 抽象类

AbstractClass角色不仅负责实现模板方法，还负责声明在模板方法中使用到的抽象方法。这些方法由ConcreteClass角色负责实现。在程序实例中对应的是`AbstractDisplay` 类。

> ConcreteClass 具体类

该角色负责具体实现AbstractClass角色中定义的抽象方法。在这里实现的方法会在AbstractClass角色的模板方法中被调用。在程序实例中对应的是`CharDisplay` 和`StringDisplay` 类。

#### 设计思路

模板模式的优点在于父类模板方法中编写了算法（统一方法），因此无需在每个子类中重新编写。

例如，未使用模板模式时，复制粘贴多个Class，编写完后很长时间才发现bug，就必须将修改映射到所有的Class中。如果使用模板模式，只需要修改模板方法即可解决问题。

无论在CharDisplay的实例还是StringDisplay的实例，都先保存在AbstractDisplay类型的变量中，然后再调用`display()`方法。

使用父类类型的变量保存子类实例的优点是，即使没有用`instanceof`等指定之类的种类，程序也能正常工作。

无论在父类类型的变量中保存哪个子类实例，程序都可以正常工作，这种原则称之为**里氏替换原则**（The Liskov Substitution Principle，LSP）。LSP并非局限于模板模式，而是通用的继承原则。

---

### 工厂方法模式（Factory Method）

在Template Method模式中，父类规定了处理流程，子类实现具体处理。如果将该模式用于生成实例，它将演变为Factory Method模式。

Factory有“工厂”的意思，用模板模式来构建生成实例的工厂，这就是工厂模式。

在工厂模式中，父类决定实例的生成方式，但并不决定所要生成的具体的类，具体的处理全部交由子类负责。由此可以将生成实例的框架（framework）和实际负责生成实例的类解耦。

#### 设计模式实现

| 包         | 类名            | 说明                                   |
| --------- | ------------- | ------------------------------------ |
| framework | Product       | 自定义抽象方法use的抽象类                       |
| framework | Factory       | 实现了create方法的抽象类                      |
| idcard    | IDCard        | 实现了use方法的类                           |
| idcard    | IDCardFactory | 实现了createProduct、registerProduct方法的类 |
| ——        | Main          | main方法测试类                            |

![工厂模式](https://gitee.com/Jayce_Lan/some_img/raw/master/design/factory-method.png)

> Product

用来表示“产品”的类。仅声明了抽象方法use。use方法的实现则被交给了Product的子类负责，在这个框架中，定义了产品是“任意的可以use”的东西。

```java
public abstract class Product {
    public abstract void use();
}
```

> Factory

该类其实使用了模板模式，还声明了“生成产品”的`createProduct` 抽象方法和用于“注册产品” 的`registerProduct` 抽象方法。生成和注册具体处理交给了Factory的子类。

在这个框架中，我们定义了工厂是用来调用`create` 方法生成Product实例的。而create方法的实现是先调用createProduct生成产品，接着调用registerProduct注册产品。

具体的实现内容根据Factory Method模式适用的场景而改变。但是，只要是工厂模式，在生成实例时就一定会用到模板模式。

```java
public abstract class Factory {
    public final Product create(String owner) {
        Product product = createProduct(owner);
        registerProduct(product);
        return product;
    }

    protected abstract Product createProduct(String owner);
    protected abstract void registerProduct(Product product);
}
```

> IDCard

```java
public class IDCard extends Product {
    private static final Logger log = LogManager.getLogger(IDCard.class);
    private String owner;

    public IDCard(String owner) {
        log.info("制作{}的ID卡", owner);
        this.owner = owner;
    }

    @Override
    public void use() {
        log.info("使用{}的ID卡", this.owner);
    }

    public String getOwner() {
        return this.owner;
    }
}
```

> IDCardFactory

```java
public class IDCardFactory extends Factory {
    private List<String> owners = new ArrayList<>();

    @Override
    protected Product createProduct(String owner) {
        return new IDCard(owner);
    }

    @Override
    protected void registerProduct(Product product) {
        owners.add(((IDCard)product).getOwner());
    }

    public List<String> getOwners() {
        return owners;
    }
}
```

> Main

```java
public class FactoryMethodMain {
    private static final Logger log = LogManager.getLogger(FactoryMethodMain.class);

    public static void main(String[] args) {
        Factory factory = new IDCardFactory();
        Product card1 = factory.create("小红");
        Product card2 = factory.create("小明");
        Product card3 = factory.create("小刚");
        card1.use();
        card2.use();
        card3.use();
    }
}
```

#### 设计模式说明

![Factory Method](https://gitee.com/Jayce_Lan/some_img/raw/master/design/factory-method02.png)

> Product（产品）

Product角色属于框架这一方，是一个抽象类。它定义了在工厂模式中生成的实例所持有的接口（API），但具体的处理则由子类ConcreteProduct角色决定。对应程序中的`Product` 类。

> Creator（创建者）

Creator角色属于框架这一方，他是负责生成Product角色的抽象类，但具体的处理则由子类ConcreteCreator角色决定。对应程序中的`Factory`类。

Creator角色对于实际负责生成实例的ConcreteCreator角色一无所知，只需要调用Product角色和生成实例的方法（对应上图的`factoryMethod`方法），就可以生成Product实例。在程序中，`createProduct` 方法是用于生成实例的方法。**不用new关键字来生成实例，而是调用生成实例的专用方法来生成实例，则由可以防止父类与其他具体类耦合**。

> ConcreteProduct（具体的产品）

ConcreteProduct角色属于具体加工这一方，它决定了具体的产品。对应程序中的`IDCard` 类。

> ConcreteCreator（具体的创建者）

ConcreteCreator角色属于具体加工这一方，它负责生成具体的产品。对应程序中的`IDCardFactory`类。

#### 设计思路

使用工厂模式，我们拥有了“框架”与“具体加工”两个概念，分别对应`frameword`包和`idcard`包。

假如需要相同的框架创造出其他的“产品”和“工厂”。例如创建需要表现电视机类的`Televison`和表示电视机工厂类的`TelevisonFactory`。这时，我们只需要将这两个类放入`televison` 包中，而不修改`framework`中的任何内容就可以创建产品和工厂。

---

### 单例模式（Singleton）

在程序运行时，通常会生成很多实例。以`java.lang.String` 为例，基本上我们声明一次字符串就会创建一个String实例。

但是有时候我们需要在程序中表示某个东西只存在一个时，就会有单例（只能创建一个实例）的需求。我们可以通过多加注意，保证只调用一次`new Class()` 来实现单例。但是程序修改、维护过程中我们不能每次都通过“稍加注意”来达到单例的目的，此时我们必须满足：

- 确保任何情况下都绝对只有1个实例

- 想在程序上表现出“只存在一个实例”（即，我随时用到这个对象都是它，而不是被new出来的新对象）

像这样确保只生成一个实例的模式被称作**单例（Singleton）模式**。Singleton是指只含有一个元素的集合。

#### 设计模式说明

![Singleton](https://gitee.com/Jayce_Lan/some_img/raw/master/design/singleton.png)

> Singleton

Singleton类只会生成一个实例。Singleton类定义了static字段（类的成员变量）singleton，并将其初始化为Singleton的实例。初始化行为仅在该类被加载时只进行一次。

Singleton类的构造方法是`private` 的，这是为了禁止冲Singleton类外部调用构造函数。如果从Singleton类以外的代码中调用构造函数`new Singleton()`，程序就会编译报错。这也就符合了单例模式要确保在任何情况下只生成一个实例。

```java
public class Singleton {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private static Singleton singleton = new Singleton();

    private Singleton() {
        log.info("生成了一个Singleton实例-{}", this.getClass());
    }

    public static Singleton getInstance() {
        return singleton;
    }
}
```

> Main

```java
private void testSingleton() {
    Singleton obj1 = Singleton.getInstance();
    Singleton obj2 = Singleton.getInstance();
    if (Objects.equals(obj1, obj2)) {
        log.info("obj1与obj2为同一个对象");
    } else {
        log.info("obj1与obj2不为同一个对象");
    }
}
```

在首次调用`Singleton.getInstance()` 方法时，Singleton类会被初始化，也就是这事，static字段的singleton被初始化，生成唯一一个实例。

#### 设计思路

单例模式对实例的数量设置了限制。在一些特定情况下，多个实例之间会互相影响（如初始化变量等），可能会产生意想不到的bug，此时使用单例模式就不需要担心实例之间影响的情况。

#### 防止多线程下单例失效的延伸

Java中解决并发环境下单例模式失效的问题，主要可以通过以下几种方式来实现：

1. **饿汉式（静态初始化）**：
   饿汉式在类加载时就完成了实例化，因此不存在并发问题。这是最简单也是线程安全的实现方式，但可能会提前占用资源。
   
   ```java
   public class Singleton {
       private static final Singleton INSTANCE = new Singleton();
   
       private Singleton() {}
   
       public static Singleton getInstance() {
           return INSTANCE;
       }
   }
   ```

2. **双重检查锁定（Double-Checked Locking, DCL）**：
   通过在getInstance方法中增加同步块，只在实例为null时进行同步创建，减少同步开销。但需要注意volatile关键字来防止指令重排序导致的问题。
   
   ```java
   public class Singleton {
       private volatile static Singleton instance;
   
       private Singleton() {}
   
       public static Singleton getInstance() {
           if (instance == null) {
               synchronized (Singleton.class) {
                   if (instance == null) {
                       instance = new Singleton();
                   }
               }
           }
           return instance;
       }
   }
   ```

3. **静态内部类**：
   利用Java类加载机制的特性，保证线程安全且延迟加载。
   
   ```java
   public class Singleton {
       private static class SingletonHolder {
           private static final Singleton INSTANCE = new Singleton();
       }
   
       private Singleton() {}
   
       public static Singleton getInstance() {
           return SingletonHolder.INSTANCE;
       }
   }
   ```

4. **枚举**：
   Java枚举类型天然线程安全，并且可以防止反射和序列化攻击。
   
   ```java
   public enum Singleton {
       INSTANCE;
   
       // 可以在这里添加业务方法
   }
   ```

5. **使用容器（如Spring IoC容器）管理**：
   在使用Spring框架等IoC容器时，可以让容器来管理单例，从而避免直接处理并发问题。

对于序列化破坏单例的问题，可以在单例类中添加以下方法来控制反序列化过程，确保单例特性不被破坏：

```java
private Object readResolve() {
    return getInstance();
}
```

这段代码应该放在单例类中，标记为`private`，这样在反序列化时会调用这个方法而不是创建新的实例。这适用于实现了`Serializable`接口的单例类。

---

### 原型模式（Prototype）

在Java中，可以使用new关键字指定类名生成类的实例。但是在开发过程中有时候会在“不指定类名的前提下生成实例”，情况如下：

- 对象种类繁多，无法将它们整合到一个类中

- 难以根据类生成实例

- 想解耦框架与生成的实例关系

**Prototype模式**，有原型、模型的意思，在设计模式中是指根据实例原型、实例模型生成新的实例。在Java语言中，可以使用`clone` 创建出实例的副本。原型模式中将要学习`clone` 方法和`Cloneable`接口的使用方法。

#### 设计模式实现

| 包         | 类名           | 说明                                              |
| --------- | ------------ | ----------------------------------------------- |
| framework | Product      | 声明了抽象方法`use`和`createClone`的接口                   |
| framework | Manager      | 调用`createClone`方法复制实例的类                         |
| 无名        | MessageBox   | 将字符串放入方框中并使其显示出来的类。实现了`use`方法和 `createClone`方法  |
| 无名        | UnderlinePen | 给字符串加上上下划线并使其显示出来的类。实现了`use`方法和 `createClone`方法 |
| 无名        | Main         | 测试程序行为的类                                        |

![prototype](https://gitee.com/Jayce_Lan/some_img/raw/master/design/prototype.png)

> Product

`use`方法是用于“使用”的方法，具体如何使用交由子类

`createClone`方法是用于复制实例的方法

```java
public interface Product extends Cloneable {
    void use(String str);
    Product createClone();
}
```

> Manager

`register`方法将会接收到一组map，key为Product的名字、value为Product接口，并且注册到showcase中。

在Manager类中并没有写明具体的类名，仅使用了Product接口名，因为**一旦在类中使用到了别的类名，就意味着该类与其他类紧密耦合**，而此处Product接口成为了连接Manager类与其他具体类的桥梁。

```java
public class Manager {
    private Map<String, Product> showcase = new HashMap<>();

    public void register(String name, Product product) {
        showcase.put(name, product);
    }

    public Product create(String name) {
        Product product = showcase.get(name);
        return product.createClone();
    }
}
```

> MessageBox

```java
public class MessageBox implements Product {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private char decochar;

    public MessageBox(char decochar) {
        this.decochar = decochar;
    }

    @Override
    public void use(String str) {
        int length = str.getBytes(StandardCharsets.UTF_8).length;
        String head = "";
        for (int i = 0; i < length + 4; i++) {
            head += decochar;
        }
        log.info(head);
        log.info(decochar + " " + str + " " + decochar);
        log.info(head);
    }

    @Override
    public Product createClone() {
        Product product = null;
        try {
            product = (Product) clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
        }
        return product;
    }
}
```

> UnderlinePen

```java
public class UnderlinePen implements Product {
    private final Logger log = LogManager.getLogger(this.getClass().getName());
    private char ulchar;

    public UnderlinePen(char ulchar) {
        this.ulchar = ulchar;
    }

    @Override
    public void use(String str) {
        int length = str.getBytes(StandardCharsets.UTF_8).length;
        log.info("\"" + str + "\"");
        StringBuilder foot = new StringBuilder();
        for (int i = 0; i < length; i++) {
            foot.append(ulchar);
        }
        log.info(foot.toString());
    }

    @Override
    public Product createClone() {
        Product product = null;
        try {
            product = (Product) clone();
        } catch (CloneNotSupportedException e) {
            log.error(e.getMessage());
        }
        return product;
    }
}
```

> Main

```java
public static void main(String[] args) {
    // 准备
    Manager manager = new Manager();
    UnderlinePen underlinePen = new UnderlinePen('~');
    MessageBox messageBox = new MessageBox('*');
    MessageBox messageBox2 = new MessageBox('/');
    manager.register("underlinePen", underlinePen);
    manager.register("messageBox", messageBox);
    manager.register("messageBox2", messageBox2);
    // 生成
    Product product1 = manager.create("underlinePen");
    product1.use("Hello, World!");
    Product product2 = manager.create("messageBox");
    product2.use("Hello, World!");
    Product product3 = manager.create("messageBox2");
    product3.use("Hello, World!");
}
```

#### 设计模式说明

![prototype](https://gitee.com/Jayce_Lan/some_img/raw/master/design/prototype02.png)

> Prototype（原型）

Product角色负责定义用于复制现有实例生成新实例的方法。对应的是程序中的`Product` 接口。

> ConcretePrototype（具体的原型）

ConcretePrototype角色负责实现复制现有实例并生成新实例的方法。对应的是程序中的`MessageBox` 和`UnderlinePen`。

> Client（使用者）

Client角色负责使用复制的方法生成新的实例。对应的是程序中的`Manager` 。

---

### 建造者模式（Builder）

通常，在建造具有复杂结构的物体时，需要先建造组成这个物体的各个部分，然后分阶段将它们组装起来。这种用于组装具有复杂结构的实例的模式，就叫做建造者模式。

#### 设计模式实现

| 类名          | 说明               |
| ----------- | ---------------- |
| Builder     | 定义了决定文档结构的方法的抽象类 |
| Director    | 编写一个文档的类         |
| TextBuilder | 使用纯文本编写文档的类      |
| HTMLBuilder | 使用HTML编写文档的类     |
| Main        | 测试程序行为的类         |

![Builder](https://gitee.com/Jayce_Lan/some_img/raw/master/design/builder.png)

> Builder

```java
public interface Builder {
    void makeTitle(String title);
    void makeString(String str);
    void makeItems(String[] items);
    void close();
}
```

> Director

`TextBuilder`和`HTMLBuilder`都是`Builder`的子类（实现类），因此Director只需要Builder的方法即可完成，**Director并不关心实际编写文档的到底是哪一个具体的子类（实现类）**。

正因如此，必须在Builder中声明足够多的方法以实现某一功能，但不包括子类（实现类）的特有方法。

```java
public class Director {
    private Builder builder;

    public Director(Builder builder) {
        this.builder = builder;
    }

    /**
     * 编写文档的实现方法
     */
    public void construct() {
        builder.makeTitle("Greeting");
        builder.makeString("早上与下午");
        builder.makeItems(new String[] {
                "早上好",
                "下午好"
        });
        builder.makeString("晚上");
        builder.makeItems(new String[]{
                "晚上好",
                "goodnight！",
                "goodbye!"
        });
        builder.close();
    }
}
```

> TextBuilder

```java
public class TextBuilder implements Builder {
    private StringBuffer buffer = new StringBuffer();

    @Override
    public void makeTitle(String title) {
        buffer.append("================================\n");
        buffer.append("[" + title + "]\n");
        buffer.append("\n");
    }

    @Override
    public void makeString(String str) {
        buffer.append("- " + str + "\n");
    }

    @Override
    public void makeItems(String[] items) {
        for (String item : items) {
            buffer.append(" -" + item + "\n");
        }
        buffer.append("\n");
    }

    @Override
    public void close() {
        buffer.append("================================\n");
    }

    public String getResult() {
        return this.buffer.toString();
    }
}
```

> Main

```java
private void testTextBuilder() {
    TextBuilder textBuilder = new TextBuilder();
    Director director = new Director(textBuilder);
    director.construct();
    log.info("result >>>>> {}", textBuilder.getResult());
}
```

#### 设计模式说明

![builder](https://gitee.com/Jayce_Lan/some_img/raw/master/design/builder02.png)

> Builder（建造者）

Builder角色负责定义用于生成实例的接口，准备了用于生成实例的方法。

> ConcreteBuilder（具体建造者）

ConcreteBuilder负责实现Builder的接口，这里定义了生成实例时被调用的方法。此外，在ConcreteBuilder角色中还定义了获取最终生成结果的方法`getResult`。对应程序中的`TextBuilder` 和`HTMLBuilder`。

> Director（监工）

Director角色负责使用Builder角色的接口来生成实例，它不依赖ConcreteBuilder角色。为了确保无论ConcreteBuilder是如何被定义的，Director都能正常工作，它**只调用在Builder角色中被定义的方法**。

> Client（使用者）

该角色使用了Builder模式，对应程序中的`Main`。

##### Builder模式时序图

![builder](https://gitee.com/Jayce_Lan/some_img/raw/master/design/builder03.png)

---