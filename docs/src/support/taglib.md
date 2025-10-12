该功能由[`ymate-webui-bootstrap`](https://github.com/suninformation/ymate-webui)源改而来

# 1. 标签类型

| 类型          | 描述                                 |
| ------------- | ------------------------------------ |
| <fb:ui>       | 提取 html、jsp 中的 body 作为模板    |
| <fb:property> | 父类模板中的自定义属性               |
| <fb:meta>     | 当前页面额外加载的 meta 脚本         |
| <fb:css>      | 当前页面额外加载的 script 文件       |
| <fb:script>   | 当前页面额外加载的 script 文件、脚本 |
| <fb:layout>   | 当前页面主体 html 文件               |

---

# 2. 父模板 template.jsp

```
<%@ page language="java" contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
@{meta}
<title>@{title} - GOTV</title>
<!--  -->
<link href="https://layuicdn.com/layui/css/layui.css" rel="stylesheet" />
<link href="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.css" rel="stylesheet" />
@{css}
<!--  -->
<script src="https://layuicdn.com/layui/layui.js"></script>
<script src="https://cdn.bootcss.com/jquery/2.2.1/jquery.min.js"></script>
<script src="https://cdn.bootcss.com/html5shiv/r29/html5.min.js"></script>
<script src="https://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
<script src="https://cdn.bootcss.com/nprogress/0.2.0/nprogress.min.js"></script>
<!--  -->
<script type="text/javascript" charset="utf-8">
	NProgress.configure({
		speed : 500,
		showSpinner : false
	});
	$().ready(function() {
		NProgress.start();
	});
	$(window).load(function() {
		NProgress.done();
	});
</script>
@{script}
</head>
<body layadmin-themealias="default">
	<!--  -->
	<div class="layui-fluid">
		<div class="layui-row layui-col-space15">@{body}</div>
	</div>
</body>
</html>
```

---

# 3. 子模板 xxx.jsp

```
<%@ page language="java" contentType="text/html;charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/FastBoot" prefix="fb"%>
<fb:ui src="base/template">

	<fb:property name="title">xxx</fb:property>

	<fb:css href="xxxx"></fb:css>

	<fb:script href="xxxx"></fb:script>

	<fb:script type="text/javascript">
		script代码
	</fb:script>

	<fb:layout>
		html代码
	</fb:layout>
</fb:ui>
```

# 友情链接

[`Ymate 官网`](https://ymate.net/)
