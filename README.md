# jmtrace 171240524 张灵毓

## 编译说明

- java version: >= 11.0.8.
- asm-9.0-beta.jar(可在[Download](https://repository.ow2.org/nexus/content/repositories/releases/org/ow2/asm/asm/)处下载).

下载好的asm-9.0-beta.jar需放在项目libs目录中, 随后即可使用提供的Makefile进行编译等操作.

- `make compile`: 将`src/main/java`下的程序编译到根目录下`com`中.
- `make jar`: 
  - 先执行`make compile`编译源文件.
  - 然后在`/src/main`下创建一个`MANIFEST.MF`文件并写入对应的内容.
  - 最后将这些文件打包为`jmtrace.jar`.
- `make run`:
  - 先执行`make jar`打包
  - 然后会将`test`目录（需用户自己创建）下的jar包当作待插桩的jar包执行.
  - 执行后会保留一份插桩好的class文件于目录`output`下.
- `make clean`: 删除编译打包所产生的文件及目录.