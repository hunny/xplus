# Spark 基础 —— 元组（tuple）

## Scala 中的元组同 Python，由()标识：

```
val t = (1, 2, 3)
```

从元组中获取某个字段的值，可以用下标函数，注意是从 _1 开始，或者用 productElement 方法（它是从 0 开始计数的）

```
t._1            # 1
t.productElement(0) # 1
```

使用 productArity 方法获得元组的大小：

```
t.productArity      # 3
```

## 元组与 case class

Scala 创建元组非常简单方便，但通过下标而不是有意义的名称来访问元素会让代码很难理解。我们更希望创建一个简单的记录类型，它可以根据名称而不是下标访问字段。幸运的是，Scala 恰提供这样的语法，可以方便地创建这种记录，这就是 case class。

```
case class Student (name: String, age: Int, weight: Double)
val s1 = Student("zhang", 23, 65.)
# 我们便可以使用名称来访问字段，颇有几分 map 的感觉
```
