# 注意

## 使用ObjectUtil相关内容

所在的项目，maven配置请加上`compilerargs-parameters` 否则在使用有参构造器动态初始化类的时候，可能找不到正确的值。

即在编译时，会丢失参数名，而无法匹配到正确的参数名。如mapToBean相关方法
