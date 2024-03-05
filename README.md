# OnePlus No Red One

## Description / 介绍

This is a simple Xposed module that removes the red color of the "1"s from the digital clocks
in screen guard, notifcation drawer, and AOD screen for OxygenOS 14.

移除一加 OxygenOS 14 系统锁屏、通知栏、息屏显示的时间中的红色"1"。

## Compatibility / 兼容性

Only OxygenOS 14 is supported and tested. Not likely to work on other versions.
For OxygenOS 13 and below, consider using other Xposed modules.

仅支持 OxygenOS 14, 不支持其他版本。
对于 OxygenOS 13 及以下版本，可以考虑使用其他 Xposed 模块。

## Requirements / 要求

Xposed Framework with system app modification support is required.

需要安装支持修改系统应用的 Xposed 框架。

## Compilation / 编译

This project is built using Gradle. You can build the module by running the following command:

执行以下命令编译项目：

```bash
./gradlew :app:assembleDebug
```

Alternatively, you can download the pre-built module from the releases page.

或者你可以直接从发布页面下载预编译的模块。

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
