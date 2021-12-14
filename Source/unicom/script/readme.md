
# 使用说明

> 文件路径

1、将 `UnicomManager.war` 和 `startwar.sh` 文件放置在 `/usr/local/unicom` 目录下

2、将 `UnicomServer.jar` 和 `startjar.sh` 文件放置在 `/usr/local/unicom/unicomServer` 目录下

3、将 `UnicomHttpServer.jar` 和 `startjar.sh` 文件放置在 `/usr/local/unicom/unicomHttpServer` 目录下

4、将此文件夹下 `config` 文件加下的脚本放置在 `/usr/local/unicom/config` 目录下


> 脚本设置

设置 `unicom/config` 下脚本格式为 unix ，并添加可执行权限

- 进入 unicom/config 添加可执行权限
```shell script
chmod +x *
```

- 设置脚本格式为unix(所有config下的脚本都要设置，这里只举例其中一个，剩余脚本请参照下面示例)
```shell script
vim changeip.sh
# 按 esc
:set ff=unix
# 保存并退出
:wq
```


> 启动程序

分别进入 `/usr/local/unicom` 、`/usr/local/unicom/unicomServer` 和 `/usr/local/unicom/unicomHttpServer`
执行 `sh startwar.sh` 或 `sh startjar.sh`