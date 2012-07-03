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

import java.net.URLClassLoader
import java.net.JarURLConnection
import java.util.Enumeration
import java.util.List
import java.util.jar.JarEntry
import java.util.jar.JarFile
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
class Pydoc extends DefaultTask {

	private List<String> sourceDirs = ['src/main/python', 'src/main/python_scripts']

	private File toolsDir

    private File destinationDir

	private String title = 'Python API Docs'

	private String readme = 'README'

	private String license = 'LICENSE'

	public Pydoc() {
		group 'documentation'
		description 'Generates Python API documentation for the main source code.'
		toolsDir = new File("${project.buildDir}/tmp/tools") // build/tmp/tools
		destinationDir = new File("${project.buildDir}/docs/python/api")
	}

	@TaskAction
	def run() {
		destinationDir.mkdirs()
		println "PyDoc stub: ${destinationDir.path}"

		if (!toolsDir.exists()) {
			println "Creating pydoc tools..."
			toolsDir.mkdirs()
		}

		File pydocx = new File("$toolsDir.path/pydocx.py")
		if (!pydocx.exists()) {
			println "Unpacking pydoc script..."
            final InputStream is = getClass().getClassLoader().getResourceAsStream('tools/pydocx.py')
            pydocx << is
            is?.close()
		}

		File doclib = new File("$toolsDir.path/doclib")
		if (!doclib.exists()) {
			println "Unpacking pydoc libs..."
			URL epydocURL = getClass().getClassLoader().findResource('tools/doclib/epydoc/')

			JarURLConnection connection = (JarURLConnection) epydocURL.openConnection()
			JarFile jarFile = connection.getJarFile()
			
			Enumeration<JarEntry> entries = jarFile.entries()
			while (entries.hasMoreElements()) {
				JarEntry entry = entries.nextElement()

				if (!entry.name.startsWith('tools') || entry.name.startsWith('tools/doclib/epydoc/test'))
					continue

				File entryFile = new File("$toolsDir.path/../$entry.name")
				entryFile = entryFile.getCanonicalFile()
				if (entryFile.exists()) continue

				if (entry.isDirectory()) {
					println "creating $entry.name ..."
					entryFile.mkdirs()
				}
				else {
					println "unpacking $entry.name ..."
					final InputStream is = getClass().getClassLoader().getResourceAsStream(entry.name)
					entryFile << is
			        is?.close()
				}
			}
		}

		Exec exec = project.tasks.add('pydocExec', Exec)
		exec.setIgnoreExitValue true
		exec.environment = System.getenv()
		exec.workingDir = project.projectDir

		def args = [
			"jython",
			'-J-classpath', project.files(project.configurations.compile).files.join(':'),
			"-Dpython.path=$toolsDir.path/doclib:${sourceDirs.join(':')}",
			"-Dproj.base=$project.projectDir.path",
			"-Doutput.base=$destinationDir.path",
			"build/tmp/tools/pydocx.py"
		]

		exec.commandLine = args

		exec.execute()		
	}

	@InputFiles
    public List<String> getSourceDirs() {
        return sourceDirs
    }

	public Pydoc setSourceDirs(List<String> sourceDirs) {
        this.sourceDirs = sourceDirs
		this
    }

	public Pydoc setSourceDir(String sourceDirs) {
        this.sourceDirs.add = sourceDirs
		this
    }

	@OutputDirectory
    public File getDestinationDir() {
        return destinationDir
    }

    public Pydoc setDestinationDir(File destinationDir) {
        this.destinationDir = destinationDir
		this
    }
	
	@Input
	@Optional
    public File getToolsDir() {
        return toolsDir
    }

    public Pydoc setToolsDir(File toolsDir) {
        this.toolsDir = toolsDir
		this
    }

	@Input
	@Optional
	public String getReadme() {
		return readme
	}

	public Pydoc setReadme(String readme) {
		this.readme = readme
		this
	}

	@Input
	@Optional
	public String getTitle() {
		return title
	}

	public Pydoc setTitle(String title) {
		this.title = title
		this
	}

	@Input
	@Optional
	public String getLicense() {
		return license
	}

	public Pydoc setLicense(String license) {
		this.license = license
		this
	}
}

/*

task pydoc(type: Exec) {
	group 'documentation'
	description 'Generates Python API documentation for the main source code.'
	
	def jythonHome = System.getProperty('jython.home')
	def projectClasspath = files(project.configurations.compile).files.join(':')

	workingDir projectDir
	inputs.files files("/src/main/python", "/src/main/python_scripts", "/tools/doclib")
	outputs.dir "/build/pydoc"

	commandLine = [ 
		"${jythonHome}/bin/jython",
		'-J-classpath',
		projectClasspath,
		"-Dpython.path=$projectDir/src/main/python:$projectDir/src/main/python_scripts:$projectDir/tools/doclib",
		"-Dproj.base=$projectDir",
		"-Doutput.base=$projectDir/build/python",
		"$projectDir/tools/pydocx.py"
	]
}

*/