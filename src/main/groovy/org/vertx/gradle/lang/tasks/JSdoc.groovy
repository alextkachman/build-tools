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

package org.vertx.gradle.lang.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.tasks.Exec
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction


/**
 * @author pidster
 *
 */
class JSdoc extends DefaultTask {
	
	private FileCollection classpath = getProject().files()

    private File destinationDir

	public JSdoc() {
		group 'documentation'
		description 'Generates Javascript API documentation for the main source code.'
		destinationDir = new File("${project.buildDir}/docs/javascript/api")
	}

	@TaskAction
	def run() {
		destinationDir.mkdirs()
		println "JSDoc destinationDir: $destinationDir"
	}

	@InputFiles
    public FileCollection getClasspath() {
        return classpath;
    }

	public void setClasspath(FileCollection classpath) {
        this.classpath = classpath;
    }

	@OutputDirectory
    public File getDestinationDir() {
        return destinationDir;
    }

    public void setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir;
    }

}