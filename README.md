# Gradle build tools for vert.x 

These tools add build support for various languages to vert.x.

The following is added to the classpath:

    src/main/<language>
    src/test/<language>

The following is added to the resource path:

    src/main/<language>_scripts
    src/test/<language>_scripts

In most cases, the latter is recommended as the appropriate location for script files.
The precompile support for the alternative location is presently incomplete and in
may not offer any benefit.


## Language Support

The build tools support the following languages:

### JRuby

Ruby is supported by JRuby version 1.6.7, the &lt;language&gt; variable is `ruby`

### Jython

The Python language is supported via Jython 2.5.2, the &lt;language&gt; variable is `python`

### Rhino

Javascript is support via Rhino 1.7R4, the &lt;language&gt; variable is `javascript`

## Usage

Add the following to the 'project/build.gradle':

    buildscript {
        repositories {
            maven { url 'https://oss.sonatype.org/content/repositories/snapshots' }
            mavenCentral()
        }
        dependencies {
            classpath "org.vert-x:build-tools:$vertxBuildTools"
        }
    }

Add the following to the 'project/gradle.properties'

    vertxBuildTools=0.1-SNAPSHOT  # (or current version)

Each project can now add one or a combination of the following plugins, to trigger the plugin & task additions:

    apply plugin: 'vertx-jruby'
    apply plugin: 'vertx-jython'
    apply plugin: 'vertx-rhino'


The plugins configure the source and resource paths and a non-transitive configuration namespace each.
This means that it's possible to declare the vertx components as dependencies, but they won't be exported.

    dependencies {
        compile "somegroup:somemodule:version"
        vertxJRuby "org.vert-x:vertx-lang-jruby:$vertxVersion"
        vertxJython "org.vert-x:vertx-lang-jython:$vertxVersion"
        vertxRhino "org.vert-x:vertx-lang-rhino:$vertxVersion"
    }


