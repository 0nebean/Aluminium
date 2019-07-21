[![GitHub release](https://img.shields.io/badge/release-1.0.0-28a745.svg)](https://github.com/0nebean/com.alibaba.druid-0nebean.custom/releases)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)


Introduction
---
- 一言蔽之 (带有权限控制和代码生成的TMA架构后台管理系统)
- 框架特性
  - 泛型的增删改查，继承就即可使用
  - 查询sql条件封装，简单的API调用实现自定义查询
  - 灵活的结构调整,多端程序共用代码，可以实现前后台共用dao，service甚至controller
  - 由spring security实现健壮可靠的权限控制
  - 基于以上实现自动生成代码的功能
  
  
  

一些框架的做法，他们在生成代码的模板中添加了增删改查的方法，但是这么做，不是生成的代码就没有增删改查，而且这些生成的增删改查方法霸占了方法名，如果你不用，那看着还很碍眼。
Aluminium用另一种思路可以解决了这个问题:
`sqlSessionFactory`可以接受用 `org.springframework.core.io.Resource`的方式指定mybatis的mapper xml,如此我们就可以将mapper内容生成字符串转成流的格式，而不需要生成文件，你就不用看到那些碍眼的方法，同样java代码中我们可通过继承来对子类隐藏这些方法的实现。
框架启动时获取了jvm里所有model，为他们每一位单独生成一个包含增删改查的xml的流，放入了mybatis的xml资源中初始化,这和项目里真实的mapper xml是共存的,
这样一来代码里没有碍眼的增删改查方法，但其实它也是有的，享用即可。

sql条件封装，简单的API调用实现自定义查询。
hibernate中hql可以指定javaBean中属性作为查询条件，在Aluminium中利用mybatis也有类似效果的实现。
通过建立一个通用的javaBean用于传递查询条件，在上一步中为每个model生成mapper xml的时候，把这个javaBean所有的条件都拼接在find方法sql上。
这样每个model都将拥有一个可以传入查询条件的find方法。
`net.onebean.core.Condition`就是那个封装了查询条件的javaBean。
`common_mapper.xml`中包含了解析查询条件的mybatis代码片段，并且在初始化`sqlSessionFactory`的时候已经加入了mapper资源中。
`net.onebean.core.extend.codebuilder.MybatisCRUDBuilder`中实现加工sql的具体实现。
此外我们还可以通过mybatis配置中指定的分页插件给查询分页，排序，甚至多字段排序，而这一切都不需要你实现。


多端程序共用代码，实现前后台共用dao，service甚至controller。
通常我们可能需要同时开发交付多端程序，这意味着我们很多sql查询，service方法都是重复的写在两个或多个项目里，比如：findById，findByName，我们可能要花费很多时间多次与去完成那些重复的查询方法的实现。
所以在Aluminium中，dao，service，controller被分为了三个不同的项目在父pom的聚合下相互依赖，这时如果你需要开发多段项目直接让哥端项目依赖service即可
你在完成一端的开发后，你会发现很多查询和方法都被定义过，直接调用即可。

由spring security实现健壮可靠的权限控制并可以简单拓展成SSO单点登录。
为了保证代码的健壮性和可拓展性，Aluminium择用了spring security实现权限控制层,确保代码的健壮性和配置风格统一,所有权限皆由页面表单配置,简单易读。


Aluminium基于以上特性实现一套针对数据列表，树形结构，父子结构的代码生成功能，主流框架都有，不过他们通常生成出来的代码里已经包含了长篇大论的默认实现，Aluminium生成的代码则非常纯净,并且Aluminium生成的代码可以直接选择字段，对应的页面控件类型，校验类型，直接生成功能对应的菜单和权限，并为其页面加好默认的权限控制标签。

  
 
Documentation
---
[Aluminium Documentation](https://www.jianshu.com/u/2bb3ca25a3e0)

