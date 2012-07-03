/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

import groovy.lang.Closure
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.file.DefaultSourceDirectorySet
import org.gradle.api.internal.file.FileResolver
import org.gradle.util.ConfigureUtil


/**
 * @author pidster
 *
 */
class DefaultConfigurableSourceSet implements ConfigurableSourceSet {

    private final SourceDirectorySet sourceSetDir
    private final SourceDirectorySet allSourceSetDir

    public DefaultConfigurableSourceSet(String displayName, FileResolver fileResolver, String sourceFileExtension) {
        sourceSetDir = new DefaultSourceDirectorySet(String.format("%s source", displayName), fileResolver)
        sourceSetDir.getFilter().include("**/*.java", "**/*.$sourceFileExtension")
        allSourceSetDir = new DefaultSourceDirectorySet(String.format("%s source", displayName), fileResolver)
        allSourceSetDir.getFilter().include("**/*.$sourceFileExtension")
        allSourceSetDir.source(sourceSetDir)
    }

    public SourceDirectorySet getSourceSetDir() {
        return sourceSetDir
    }

    public ConfigurableSourceSet sourceSetDir(Closure configureClosure) {
        ConfigureUtil.configure(configureClosure, getSourceSetDir())
        return this
    }

    public SourceDirectorySet getAllSourceSetDir() {
        return allSourceSetDir
    }
}