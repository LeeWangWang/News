# News
大三下学期安卓实训项目：新闻类软件
## 添加依赖：
1.将其添加到存储库末尾的root build.gradle中：
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  	dependencies {
	        implementation 'com.github.LeeWangWang:News:Tag'
	}
