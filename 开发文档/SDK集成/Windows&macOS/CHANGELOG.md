# 2020-12-21 @ v1.5.0

## Added

* 支持视频美颜 NESettingsService.GetBeautyFaceController()
* 支持直播功能 NESettingsService.GetLiveController()
* 支持会中禁止息屏
* 支持全局静音举手功能
* 支持展示SIP客户端入会信息

## Changed

* MacOS共享支持WPS
* 应用共享优化
* 支持自定义工具栏
* 适配部分分辨率下共享工具栏显示

## Fixed

* 修复共享状态下断网重连后其他端画面异常问题

* 修复win7下共享崩溃问题

* 修复窗口闪烁问题

  

# 2020-11-27 @ v1.3.3

## Added

* 支持共享应用

## Changed

* G2 SDK 升级到3.8.1
* 成员列表搜索时自动去掉首尾空格
* 会议画廊模式每页不在展示自己，只在首页展示

## Fixed

* 修复多端入会互踢时偶现崩溃问题 
* 修复会议画廊模式修改数量不生效的问题
* 修复移交主持人时成员离开房间，成员再进入房间时，成员列表有重复数据

# 2020-11-20 @ v1.3.2

## Added

* 会议举手功能
* 支持录制配置能力

## Changed

* G2 SDK 升级到3.8.0
* quick controls 1升级到quick controls 2
* 会议预约自适应窗口比例
* 调整更新升级页面视觉样式

## Fixed

* 修复应用内更新无法自动
* 修复拔掉副屏，副屏无法停止共享
* 修复共享结束时收到聊天消息，消息报错问题

# 2020-11-13 @ v1.3.1

## Added

* 会中反馈及会后反馈
* 预约会议编辑功能
* 使用网易会议账号登录接口 `NEAuthService::loginWithNEMeeting`
* 使用 SSO token 登录接口 `NEAuthService::loginWithSSOToken`
* 自动登录接口 `NEAuthService::tryAutoLogin`
* 创建及加入会议 option 中增加 NEShowMeetingIdOption 配置会议中显示会议 ID 的策略

## Updated

* `NEMeetingSDK::initialized` config 参数中增加 AppKey 参数用于设置全局默认应用 Key 信息
* `NEAuthService::logout` 新增了带默认参数的形参，用以决定在退出时是否清理 SDK 缓存的用户信息

# 2020-10-29 @ v1.3.0

## Added

* 个人会议短号解析能力
* 组件在 `AuthService` 中增加 `getAccountInfo` 接口用于获取用户资料信息
* 组件对私有化环境能力支持
* 共享中不显示工具条
* 预约会议详情页

## Changed

* 调整入会前后的整体 UI 视觉样式
* 升级 G2 SDK 到 3.7.0

## Fixed

* 匿名入会输入错误会议码后无法再次入会
* 安装包签名失败导致部分场景无法正常安装（Windows only）
* 本地视频画面无法镜像
* 多端登录后入会本地视频无法渲染
* 多拓展屏下缩放比例不一致拖动导致界面异常
* 多拓展屏下部分窗口和全局 Toast 提示不跟随窗口
* 会议持续时间计时不准确
* 断网后无法再次开启会议（macOS only）
* 屏幕共享正在讲话文案优化

## Removed

* 组件入会过程中取消按钮

## Deprecated

* 组件原有 `AccountService` 及功能函数 `getPersonalMeetingId()` 废弃不再使用
