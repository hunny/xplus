# angular自定义指令

## Directive

先从定义一个简单的指令开始。 定义一个指令本质上是在HTML中通过元素、属性、类或注释来添加功能。
AngularJS的内置指令都是以ng开头，如果想自定义指令，建议自定义一个前缀代表自己的命名空间。
这里我们先使用my作为前缀:

```javascript
var myApp = angular.module('myApp', [])
  .directive('myDirective', function() {
  return {
    restrict: 'A',
    replace: true,
    template: '<p>Kavlez</p>'
  };
})
```

如此一来，我们可以这样使用，注意命名是camel-case:

```html
<my-directive />
<!-- <my-directive><p>Kavlez</p></my-directive> -->
```

* directive()接受两个参数
  - name：字符串，指令的名字
  - factory_function：函数，指令的行为
应用启动时，以name作为该应用的标识注册factory_function返回的对象。
在factory_function中，我们可以设置一些选项来改变指令的行为。

下面说明定义指令时用到的选项

## restrict （string）

该属性用于定义指令以什么形式被使用，这是一个可选参数，本文开头定义的指令用的也是A，其实该选项默认为A。 
也就是元素（E）、属性（A）、类（C）、注释（M）
比如上面定义的myDirective，可以以任何形式调用。

E（元素）

<my-directive></my-directive>
A（属性，默认值）

<div my-directive="expression"></div>
C（类名）

<div class="my-directive:expression;"></div>
M（注释）

<--directive:my-directive expression-->
 
priority (Number)

也就是优先级，默认为0。
在同一元素上声明了多个指令时，根据优先级决定哪个先被调用。 
如果priority相同，则按声明顺序调用。
另外，no-repeat是所有内置指令中优先级最高的。

terminal (Boolean)

终端? 而且还是Boolean? 
被名字吓到了，其实terminal的意思是是否停止当前元素上比该指令优先级低的指令。 但是相同的优先级还是会执行。
比如，我们在my-directive的基础上再加一个指令:

.directive('momDirective',function($rootScope){
    return{
        priority:3,
        terminal:true
    };
})
调用发现my-directive不会生效:

<div mom-directive my-directive="content" ></div>
 

template (String/Function)

至少得输出点什么吧? 但template也是可选的。
String类型时，template可以是一段HTML。
Function类型时，template是一个接受两个参数的函数，分别为：

tElement
tAttrs
函数返回一段字符串作为模板。


templateUrl (String/Function)

这个就和上面的template很像了，只不过这次是通过URL请求一个模板。 String类型时，templateURL自然是一个URL。 Function类型时返回一段字符串作为模板URL。

replace (Boolean/String)

默认值为false，以文章开头定义的指令为例，假设我们这样调用了指令

<my-directive></my-directive>  
replace为true时，输出:

<p>Kavlez</p>
replace为false时，输出:

<my-directive><p>Kavlez</p></my-directive>      
 
transclude (Boolean)

该选项默认为false，翻译过来叫'嵌入'，感觉还是有些生涩。
template和scope已经可以做很多事情了，但有一点不足。
比如在原有元素的基础上添加内容，transclude的例子如下:

```html
<body ng-app="myApp">
    <textarea ng-model="content"></textarea>
    <div my-directive title="Kavlez">
        <hr>
        {{content}}
    </div>
</body>
<script type="text/javascript">
var myApp = angular.module('myApp', [])
.directive('myDirective', function() {
    return {
        restrict: 'EA',
        scope: {
            title: '@',
            content: '='
        },
        transclude: true,
        template: '<h2 class="header">{{ title }}</h2>\
        <span class="content" ng-transclude></span>'
    };
});
</script>
```
发现div下的hr并没有被移除，就是这样的效果。
注意不要忘了在模板中声明ng-transclude。

scope (Boolean/Object)

默认为false，true时会从父作用域继承并创建一个自己的作用域。
而ng-controller的作用也是从父作用域继承并创建一个新的作用域。
比如这样，离开了自己的作用域就被打回原形了:

```html
<div ng-init="content='from root'">
    {{content}}
    <div ng-controller="AncestorController">
        {{content}}     
        <div ng-controller="ChildController">
            {{content}}     
        </div>
        {{content}} 
    </div>
    {{content}} 
</div>
```

```javascript
.controller('ChildController', function($scope) {
    $scope.content = 'from child';
})
.controller('AncestorController', function($scope) {
    $scope.content = 'from ancestor';
})
```
但不要误解，指令嵌套并不一定会改变它的作用域。
既然true时会从父作用域继承并创建一个自己的作用域，那么我们来试试改为false会是什么样子:

```
<div ng-init="myProperty='test'">
    {{ myProperty }}
    <div my-directive ng-init="myProperty = 'by my-directive'">
        {{ myProperty }}
    </div>
    {{ myProperty }}
</div>

.directive('myDirective', function($rootScope) {
    return {
        scope:false
    };
})
```
显然，结果是三行'by my-directive'。
非true即false? naive!
其实最麻烦的还是隔离作用域，


我们稍微改动一下myDirective，改为输出`<p>{{内容}}</p>`。
于是我试着这样定义:

```
<body ng-app="myApp" >
    <p ng-controller="myController">
    <div my-directive="I have to leave." ></div>
        {{myDirective}}
    </p>
</body>
<script type="text/javascript">
var myApp = angular.module('myApp', [])
.directive('myDirective', function($rootScope) {
    $rootScope.myDirective = 'from rootScope';
    return {
        priority:1000,
        restrict: 'A',
        replace: true,
        scope: {
            myDirective: '@',
        },
        template: '<p>{{myDirective}}</p>'
    };
})
.controller('myController',function($scope){
    $scope.myDirective = 'from controller';
});
</script>
```

这里需要注意的不是@，重点是隔离作用域。
根据上面的例子输出，template中的{{myDirective}}不会影响到其他作用域。
我们再试试这样:

```
<input type="text" ng-model="content">
<p ng-controller="myController" >
<div my-directive="{{content}}" ></div>
    {{content}}
</p>
```  
发现大家都在一起变，也就是说值是通过复制DOM属性并传递到隔离作用域。
ng-model是个强大的指令，它将自己的隔离作用域和DOM作用域连在一起，这样就是一个双向数据绑定。


如何向指令的隔离作用域中传递数据，这里用了@。
或者也可以写成@myDirective，也就是说换个名字什么的也可以，比如我用@myCafe什么的给myDirective赋值也是没问题的，总之是和DOM属性进行绑定。

另外，我们也可以用=进行双向绑定，将本地作用域的属性同父级作用域的属性进行双向绑定。
比如下面的例子中，隔离作用域里的内容只能是'abc' :

```
<body ng-app="myApp" ng-init="content='abc'">
    <p ng-controller="myController" >
        <input type="text" ng-model="content">
        <div my-directive="content" ></div>
        {{content}}
    </p>
</body>
<script type="text/javascript">
var myApp = angular.module('myApp', [])
.directive('myDirective', function($rootScope) {
    return {
        priority:1000,
        restrict: 'A',
        replace: true,
        scope: {
            myDirective: '=',
        },
        template: '<p>from myDirective:{{myDirective}}</p>'
    };
})  
.controller('myController',function($scope){
    $scope.content = 'from controller';
});
</script>
```

在隔离作用域访问指令外部的作用域的方法还有一种，就是&。
我们可以使用&与父级作用域的函数进行绑定，比如下面的例子:

```
<body ng-app="myApp">
    <div ng-controller="myController">
        <table border='1'>
            <tr>
                <td>From</td>
                <td><input type="text" ng-model="from"/></td>
            </tr>
            <tr>
                <td>To</td>
                <td><input type="text" ng-model="to"/></td>
            </tr>
            <tr>
                <td>Content</td>
                <td><textarea cols="30" rows="10" ng-model="content"></textarea></td>
            </tr>
            <tr>
                <td>Preview:</td>
                <td><div scope-example to="to" on-send="sendMail(content)" from="from" /></td>
            </tr>
        </table>
    </div>
</div>

</body>
<script type="text/javascript">
var myApp = angular.module('myApp', [])
.controller('myController',function($scope){
    $scope.sendMail=function(content){
        console.log('content is:::'+content);
    }
})
.directive('scopeExample',function(){
    return{
        restrict:'EA',
        scope: {
            to: '=', 
            from: '=' ,
            send: '&onSend'
        },
        template:'<div>From:{{from}}<br>\
        To:{{to}}<br>\
        <button ng-click="send()">Send</button>\
        </div>'
    }
})
</script>
```

controller (String/Function)

控制器也可以在指令里定义，比如:

```
.directive('myDirective', function() {
    restrict: 'A',
    controller: 'myController'
}).controller('myController', function($scope, $element, $attrs,$transclude) {
    //...
})
相同的效果，也可以这样声明:

directive('myDirective', function() {
    restrict: 'A',
    controller:function($scope, $element, $attrs, $transclude) {
        //...
    }
});
```

controllerAs (String)

可以从名字和类型看出，这个选项是用来设置控制器的别名的。 
比如这样:

```
directive('myDirective', function() {
    return {
        restrict: 'A',
        template: '<p>{{ myController.name }}</p>',
        controllerAs: 'myController',
        controller: function() {
            this.name = "Kavlez"
        }
    };
});
```

compile (Object/Function)

虽说这个东西不是很常用吧，但却是值得了解的选项。
compile和link，这两个选项关系到AngularJS的生命周期。

先在这里简单记录一下我对生命周期的认识。

应用启动前，所有的指令以文本的形式存在。
应用启动后便开始进行compile和link，DOM开始变化，作用域与HTML进行绑定。
在编译阶段，AngularJS会遍历整个HTML并处理已声明的指令。
一个指令的模板中可能使用了另外一个指令，这个指令的模板中可能包含其他指令，如此层层下来便是一个模板树。
在DOM尚未进行数据绑定时对DOM进行操作开销相对较小，这时像ng-repeat之类的指令对DOM进行操作则再合适不过了。
我们可以用编译函数访问编译后的DOM，在数据绑定之前用编译函数对模板DOM进行转换，编译函数会返回模板函数。
也就是说，设置compile函数的意义在于:在指令和实时数据被放到DOM中之前修改DOM。 此时完全可以毫无顾虑地操作DOM。
接着我们便可以进入下一个阶段，链接阶段。
最后，模板函数传递给指令指定的链接函数，链接函数对作用域和DOM进行链接。

好了，接下来我们就试试compile:

```
<body ng-app="myApp">
    <my-directive ng-model="myName"></my-directive>
</body>
<script type="text/javascript">
var myApp = angular.module('myApp', [])
.directive('myDirective', function($rootScope) {
    $rootScope.myName = 'Kavlez';
    return {
        restrict: 'EA',
        compile:function(tEle, tAttrs, transcludeFn) {
            var h2 = angular.element('<h2></h2>');
            h2.attr('type', tAttrs.type);
            h2.attr('ng-model', tAttrs.ngModel);
            h2.html("hello {{"+tAttrs.ngModel+"}}");
            tEle.replaceWith(h2);
        }
    };
});
</script>
```  