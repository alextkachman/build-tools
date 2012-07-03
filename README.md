# Gradle build tools for vert.x 

These tools add build support for various languages to vert.x.

The following is added to the classpath:

    src/main/&lt;language&gt;
    src/test/&lt;language&gt;

The following is added to the resource path:

    src/main/&lt;language&gt;_scripts
    src/test/&lt;language&gt;_scripts

In most cases, the latter is recommended.

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
