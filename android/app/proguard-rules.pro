# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# retrofit
-keep class com.squareup.retrofit2.** { *; }
-keep class retrofit2.** { *; }
-keepclassmembers,allowobfuscation class * {
    @retrofit2.http.* <methods>;
}
-keepattributes Signature
-keepattributes *Annotation*
-keep class com.google.gson.** { *; }
-keep class kotlinx.serialization.** { *; }
-keep class com.zzang.chongdae.data.remote.dto.* { <fields>; }


# room
-keep class androidx.room.** { *; }
-keep interface androidx.room.** { *; }
-keepclassmembers class * {
    @androidx.room.* <fields>;
}

# glide
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep public class * extends com.bumptech.glide.module.LibraryGlideModule

-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

-keep class * extends com.bumptech.glide.GeneratedAppGlideModule { *; }

# firebase
-keep class com.google.firebase.** { *; }
-keep class com.google.android.gms.** { *; }
-keepattributes *Annotation*
-keepattributes SourceFile,LineNumberTable
-keep public class * extends java.lang.Exception

# kakao login
-keep class com.kakao.sdk.**.model.* { <fields>; }

# R8 full mode strips generic signatures from return types if not kept.
-if interface * { @retrofit2.http.* public *** *(...); }
-keep,allowoptimization,allowshrinking,allowobfuscation class <3>

# paging
-keep class androidx.paging.** { *; }

# datastore
-keep class androidx.datastore.** { *; }
-keepclassmembers class androidx.datastore.** { *; }

# mock
-keep class io.mockk.** { *; }
-dontwarn okhttp3.mockwebserver.**

# webview
-keepclassmembers class * {
    @android.webkit.JavascriptInterface <methods>;
}

-keepclassmembers class com.zzang.chongdae.presentation.view.address.JavascriptInterface{
    public *;
}

-keep public class com.zzang.chongdae.presentation.view.address.JavascriptInterface

-keepclassmembers class kotlinx.coroutines.** {
    *;
}


-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.*
-dontwarn org.openjsse.**

-dontwarn javax.swing.**
-dontwarn java.awt.**
-dontwarn java.lang.instrument.**
-dontwarn java.lang.management.**
-dontwarn org.w3c.dom.bootstrap.DOMImplementationRegistry
-dontwarn reactor.blockhound.**
