-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-keepattributes *Annotation*     # 保持注解
-ignorewarning                   # 忽略警告
-dontoptimize                    # 不优化输入的类文件
-dontshrink                       #不压缩输入的类文件

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService

#生成日志数据，gradle build时在本项目根目录输出
-dump class_files.txt            #apk包内所有class的内部结构
-printseeds seeds.txt            #未混淆的类和成员
-printusage unused.txt           #打印未被使用的代码
-printmapping mapping.txt        #混淆前后的映射

#如果有引用v4或者v7包，需添加
-dontwarn android.support.v4.**
-keep class * extends android.support.v4.**{*;}
-dontwarn android.support.v7.**
-keep class * extends android.support.v7.**{*;}

-keep class **$$ViewBinder { *; } #butterknife 7.x即之后的版本生成的类
-dontwarn butterknife.*
-keep class **$$ViewInjector { *; } #butterknife 7.x 版之前生成的类
-keepnames class * { @butterknife.InjectView *;}

-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}
#保持eventbus
-keepclassmembers class ** {
    @org.simple.eventbus.Subscribe <methods>;
}
-keep enum org.simple.eventbus.ThreadMode { *; }

#混淆第三方jar包，其中xxx为jar包名
#-libraryjars libs/pinyin4j-2.5.0.jar #这个会与build.gradle里面重复
#不混淆某个包内的所有文件
-keep class com.juns.wechat.bean.**{*;}
-keep class com.juns.wechat.chat.bean.**{*;}
-keep class com.juns.wechat.chat.xmpp.bean.**{*;}
-keep class com.juns.wechat.net.response.**{*;}
-keep class com.style.net.core.**{*;}

-keep class xiajun.example.ndk.JniTest

#-dontwarn com.xxx**              #忽略某个包的警告
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable

#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
-keepclasseswithmembernames class * {  # 保持 native 方法不被混淆
    native <methods>;
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {      # 保持自定义控件类不被混淆
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity { # 保持自定义控件类不被混淆
    public void *(android.view.View);
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}

### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
public static void dropTable(org.greenrobot.greendao.database.Database, boolean);
    public static void createTable(org.greenrobot.greendao.database.Database, boolean);
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**