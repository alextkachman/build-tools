/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.vertx.gradle.lang.plugins

import org.gradle.api.Project
import org.gradle.api.Plugin
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.FileTreeElement
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaPlugin
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.tasks.SourceSet


/**
 * @author pidster
 *
 */
abstract class AbstractSourcePlugin implements Plugin<Project> {

	public static final String COMPILE_CONFIGURATION = 'compile'

	private String configurationName

	private String sourceDirName

	private String sourceFileExtension

	protected AbstractSourcePlugin(String configurationName, String sourceDirName, String sourceFileExtension) {
		this.configurationName = configurationName
		this.sourceDirName = sourceDirName
		this.sourceFileExtension = sourceFileExtension
	}

	void apply(Project project) {
		JavaBasePlugin javaBasePlugin = project.plugins.apply(JavaBasePlugin)
		JavaPlugin javaPlugin = project.plugins.apply(JavaPlugin)

		Configuration config = project.configurations
			.add(configurationName)
			.setVisible(false)
			.setTransitive(true)
			.setDescription("The $configurationName tools libraries to be used for this $configurationName project.")

		Configuration compile = project.configurations.findByName(COMPILE_CONFIGURATION)
		compile.extendsFrom config

		configureSourceSets(project, config, sourceDirName, sourceFileExtension)
		configureCompiler(project, config)
		configureResources(project, config, sourceDirName)
		configureTasks(project, config)
	}

	protected void configureSourceSets(Project project, Configuration configuration, String sourceDirName, String sourceFileExtension) {
		project.sourceSets.add configuration.name
		project.configure(project) {
			sourceSets {
				main {
					resources {
						srcDirs project.file("src/main/${sourceDirName}")
					}
				}
				test {
					resources {
						srcDirs project.file("src/test/${sourceDirName}")
					}
				}
			}
		}
/*
		project.convention.getPlugin(JavaPluginConvention).sourceSets.all { SourceSet sourceSet ->
			sourceSet.convention.plugins."${configuration.name}" =
				new DefaultConfigurableSourceSet(sourceSet.displayName, project.fileResolver, "${sourceFileExtension}")

            sourceSet.sourceSetDir.srcDir { project.file("src/${sourceSet.name}/${sourceDirName}") }
            sourceSet.allJava.source(sourceSet.sourceSetDir)
            sourceSet.allSource.source(sourceSet.allSourceSetDir)
            sourceSet.resources.filter.exclude { FileTreeElement element ->
				sourceSet.sourceSetDir.contains(element.file)
			}

            // String taskName = sourceSet.getCompileTaskName(configuration.name)

            ScalaCompile scalaCompile = project.tasks.add(taskName, ScalaCompile.class);
            scalaCompile.dependsOn sourceSet.compileJavaTaskName
            javaPlugin.configureForSourceSet(sourceSet, scalaCompile);
            scalaCompile.description = "Compiles the $sourceSet.scala.";
            scalaCompile.source = sourceSet.scala

            project.tasks[sourceSet.classesTaskName].dependsOn(taskName)
        }

		// project.sourceSets."${configuration.name}".compileClasspath = configuration

		sourceSet.srcDir = { project.file("src/${configuration.name}/${sourceDirName}") }
		sourceSet.java.source sourceSet
		sourceSet.allJava.source sourceSet
		sourceSet.resources.filter.exclude { FileTreeElement element ->
			sourceSet."${configuration.name}".contains(element.file)
		}
		sourceSets."${configuration.name}".compileClasspath = configuration


		*/

	}

	protected void configureCompiler(Project project, Configuration configuration) {
		//
	}

	protected void configureResources(Project project, Configuration configuration, String sourceDirName) {
		project.configure(project) {
			sourceSets {
				main {
					resources {
						srcDirs project.file("src/main/${sourceDirName}_scripts")
					}
				}
				test {
					resources {
						srcDirs project.file("src/test/${sourceDirName}_scripts")
					}
				}
			}
		}
	}

	protected void configureTasks(Project project, Configuration configuration) {
		//
	}
}
