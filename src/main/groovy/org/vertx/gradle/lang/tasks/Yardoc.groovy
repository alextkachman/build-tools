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

import java.util.List
import org.gradle.api.DefaultTask
import org.gradle.api.file.FileCollection
import org.gradle.api.file.FileTree
import org.gradle.api.file.FileTreeElement
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
class Yardoc extends DefaultTask {
	
	private List<String> sources = ['src/main/ruby/**/*.rb', 'src/main/ruby_scripts/**/*.rb']

    private File destinationDir

	private String title = 'Ruby API Docs'

	private String readme = 'README'

	private String license = 'LICENSE'

	public Yardoc() {
		group 'documentation'
		description 'Generates Ruby API documentation for the main source code.'
		destinationDir = new File("${project.buildDir}/docs/ruby/api")
	}

	@TaskAction
	def run() {
		destinationDir.mkdirs()

		Exec exec = project.tasks.add('yardocExec', Exec)
		exec.environment = System.getenv()
		exec.workingDir = project.projectDir
		
		def args = [
			'yardoc',
			'--title', "'$title'",
			'--readme', readme,
			'--no-private',
			'--output-dir', destinationDir.path
		]
		
		sources.each { source->
			args << source
		}
		
		args << '-'
		args << license
		exec.commandLine = args

		exec.execute()
	}

	@InputFiles
    public List<String> getSources() {
        return sources
    }

	public Yardoc setSources(List<String> sources) {
        this.sources = sources
		this
    }

	public Yardoc setSources(String source) {
        this.sources.add source
		this
    }

	@OutputDirectory
    public File getDestinationDir() {
        return destinationDir
    }

    public Yardoc setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir
		this
    }

	@Input
	@Optional
	public String getReadme() {
		return readme
	}

	public Yardoc setReadme(String readme) {
		this.readme = readme
		this
	}

	@Input
	@Optional
	public String getTitle() {
		return title
	}

	public Yardoc setTitle(String title) {
		this.title = title
		this
	}

	@Input
	@Optional
	public String getLicense() {
		return license
	}

	public Yardoc setLicense(String license) {
		this.license = license
		this
	}
}
