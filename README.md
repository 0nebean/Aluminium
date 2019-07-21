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
  
  
  
了解事物的本质，才能更好的控制它。

那么让我们一起看一下我们是怎么实现框架这些特性的。

泛型的增删改查，继承就即可使用。
我参考了一些主流的开源框架的做法，他们在生成代码的模板中添加了增删改查的方法，但是这么做，不是生成的代码就没有增删改查，而且这些生成的增删改查方法霸占了方法名，即使你不用，看着也很碍眼。

很快我发现另一种思路可以解决这个问题。

我们在配置mybatis MapperScannerConfigurer的时候可以指定一个sqlSessionFactory，这个类中又有一个方法如下：

        public void setMapperLocations(Resource[] mapperLocations) {
          this.mapperLocations = mapperLocations;
        }
        
我们能以 org.springframework.core.io.Resource（下文直接称为 Resource）的方式指定mybatis的mapper xml

Resource有一个继承的构造器，可以指定文件路径来创建一个Resource,如下，我们可以直接指定mapper的路径

        public InputStreamResource(InputStream inputs, String desc){
           ···
        }/
          
如此我们就可以生成xml的流，不生成文件，你就不用看到那些碍眼的方法，同样java代码中我们可通过继承来对子类隐藏这些方法的实现。

不需要生成文件，freemarker就派不上用场了，Kepler中选用了可以通过模板生成字符串的Apache velocity模板来完成这一任务，后通过字符串生成流即可。

生成代码也需要将model传进模板文件作为参数生成xml的包名等字段，那我们如何在运行时获取项目里的所有model？

这里我们运用了springframework中的另外两个类分别是PathMatchingResourcePatternResolver和MetadataReaderFactory

PathMatchingResourcePatternResolver用于获取jvm当前运行时中的指定路径下的所有class为Resource

MetadataReaderFactory用于从Resource的流中获取class的类信息和其他反射信息

主角全部登场，总结一下，上面的几个类在框架启动时获取jvm里所有model，为他们每一位单独生成一个包含增删改查的xml的流，并和项目里真实的mapper xml合并后一同放入了mybatis的xml资源中初始化

如果你没看懂这一段，不要紧，你只需要知道，Kepler生成的代码看起来很清爽，里面没有看的见的碍眼的增删改查方法，其实它也是有的，直接享用即可。

sql条件封装，简单的API调用实现自定义查询。
hibernate中hql可以指定javaBean中属性作为查询条件，在Kepler中利用mybatis也有类似效果的实现。

我们实现的思路也很简单，建立一个通用的javaBean用于传递查询条件，在上一步中为每个model生成mapper xml的时候，把这个javaBean所有的条件都拼接在find方法sql上。

这样每个model都将拥有一个可以传入查询条件的find方法。

net.onebean.core.Condition就是那个封装了查询条件的javaBean。

common_mapper.xml中包含了解析查询条件的mybatis代码片段，并且在初始化sqlSessionFactory的时候已经加入了mapper资源中。

net.onebean.core.extend.codebuilder.MybatisCRUDBuilder中实现加工sql的具体实现。

此外我们还可以通过mybatis配置中指定的分页插件给查询分页，排序，甚至多字段排序，而这一切都不需要你实现。

（更多查询条件的API请点击这里）

多端程序共用代码，实现前后台共用dao，service甚至controller。
通常我们可能需要同时开发交付多端程序如：cms，后台+移动端项目，这意味着我们很多sql查询，service方法都是重复的写在两个或多个项目里，比如：findById，findByName，即使人力紧缺，我们还是要花费很多时间多次与去完成那些重复的查询方法的实现。

所以在Kepler中，dao，service，controller被分为了三个不同的项目在父pom的聚合下相互依赖，这时如果你需要实现类似CMS或者移动端项目API功能只需要建立一个额外的项目，它和后台管理的项目很大程度上是共用了，dao，service甚至controller。

让你在完成后台开发的时候几乎就完成了很多前台项目的开发，你会发现很多查询和方法都被定义过，直接调用即可。

由spring security实现健壮可靠的权限控制并可以简单拓展成SSO单点登录。
为了保证代码的健壮性和可拓展性，Kepler择用了spring security实现权限控制层。

这样做目的主要是为了Spring Security OAuth对security拓展可以很轻松实现SSO单点登录功能，现在项目向前后端分离的流行方向日益转变，SSO的重要性不言而喻。

后台的权限管理功能和单点登录功能重合度非常高，如果后台项目本身也可实现oauth，那么对前台用户的角色以及权限配置都可以通过后台进行设置，将一举解决多个难题。

基于以上实现自动生成代码的功能。
基于以上特性实现一套针对数据列表，树形结构，父子结构的代码生成功能，主流框架都有，不过他们通常生成出来的代码里已经包含了长篇大论，Kepler生成的代码则非常纯净。

并且Kepler生成的代码可以直接选择字段，对应的页面控件类型，校验类型，直接生成功能对应的菜单和权限，并为其页面加好默认的权限控制标签。

  
 
Documentation
---
[Aluminium Documentation](https://www.jianshu.com/u/2bb3ca25a3e0)

