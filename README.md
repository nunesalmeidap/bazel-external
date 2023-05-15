## External dependencies
This repository is an introduction to using Bazel as build system for a Java project 
that uses external dependencies from Maven or Private repositories. It shows how you can
use Bazel to fetch/download them and then use it in your source code.
### Requirements
- Bazel build
- rules_jvm_external v3.4 or above

### Rules jvm external
```bazel
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

RULES_JVM_EXTERNAL_TAG = "4.5"
RULES_JVM_EXTERNAL_SHA = "b17d7388feb9bfa7f2fa09031b32707df529f26c91ab9e5d909eb1676badd9a6"

http_archive(
    name = "rules_jvm_external",
    strip_prefix = "rules_jvm_external-%s" % RULES_JVM_EXTERNAL_TAG,
    sha256 = RULES_JVM_EXTERNAL_SHA,
    url = "https://github.com/bazelbuild/rules_jvm_external/archive/%s.zip" % RULES_JVM_EXTERNAL_TAG,
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")

rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")

rules_jvm_external_setup()

load("@rules_jvm_external//:defs.bzl", "maven_install")
```
### Maven install
Maven install is the interface with external artifacts. Through it, you can fetch/download
and use artifacts as dependencies in your source code.

If you give no `name` to `maven_install` rule, it will automatically set it to `maven`
and then you can reference to it using `@maven//:`.
```starlark
maven_install(
    artifacts = [
        "junit:junit:4.12",
        "androidx.test.espresso:espresso-core:3.1.1",
        "org.hamcrest:hamcrest-library:1.3",
        "org.apache.commons:commons-lang3:3.12.0",
    ],
    repositories = [
        "https://maven.google.com",
        "https://repo1.maven.org/maven2",
    ],
)
```
#### Querying
To check what artifacts you have linked in your `maven_install`, use `bazel query`
```
bazel query @maven//:all
```
#### Private repositories
To use artifacts stored in your private repository, you can link to it using:
```starlark
maven_install(
    name = "mymavenrepository"
    artifacts = [
        "groupid:artifactId:version"
    ],
    repositories = [
        "http://username:password@localhost:8081/artifactory/my-repository"
    ],
)
```

### Using the dependencies
After linking them with `maven_install` you can then reference it in your `BUILD.bazel` files:
```bazel
java_binary (
    name = "BazelApp",
    srcs = glob(["src/main/*.java"]),
    main_class = "Main",
    deps = [
        "//bazelgreeting:greeter",
         "@maven//:org_apache_commons_commons_lang3",
         "@mymavenrepository//:groupid_artifactId"
    ],
)

```

