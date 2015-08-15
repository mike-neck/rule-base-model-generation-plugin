/*
 * Copyright 2015 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mikeneck.gradle.plugin.test.support;

import org.junit.rules.TemporaryFolder;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TemporaryProject extends TemporaryFolder {

    public static final String BUILD_GRADLE = "build.gradle";

    private static final char NEW_LINE = '\n';

    private static final String TAB = "    ";

    private final TemporaryFolder delegate;

    private final Class<?> klass;

    private File buildFile;

    public static TemporaryProject testTargetClass(Class<?> klass) throws IOException {
        return new TemporaryProject(klass);
    }

    private TemporaryProject(Class<?> klass) throws IOException {
        this.klass = klass;
        this.delegate = new TemporaryFolder();
    }

    public void buildGradle(String contents) throws URISyntaxException, IOException {
        URL url = klass.getProtectionDomain().getCodeSource().getLocation();
        URI uri = url.toURI();
        StringBuilder sb = new StringBuilder();
        Path path = Paths.get(uri);
        if (url.getProtocol().equals("file") && uri.toString().endsWith(".jar")) {
            sb.append("buildscript {").append(NEW_LINE)
                    .append(TAB).append("dependencies {").append(NEW_LINE)
                    .append(TAB).append(TAB).append("classpath file('").append(path.toString()).append("')").append(NEW_LINE)
                    .append(TAB).append('}').append(NEW_LINE)
                    .append('}').append(NEW_LINE);
        } else {
            sb.append("project.class.classLoader.addURLs([")
                    .append("new URL('").append(path.toUri().toURL()).append("')");
            Path bld = path.getParent().getParent();
            if (bld.endsWith("build")) {
                sb.append(",")
                        .append("new URL('").append(bld.resolve("resources/main").toUri().toURL()).append("')");
            }
            sb.append("])").append(NEW_LINE);
        }
        sb.append(contents);
        Files.write(buildFile.toPath(), sb.toString().getBytes("UTF-8"));
    }

    public void ready() throws Throwable {
        before();
    }

    public void end() {
        after();
    }

    @Override
    protected void before() throws Throwable {
        create();
        buildFile = delegate.newFile(BUILD_GRADLE);
    }

    @Override
    protected void after() {
        delete();
    }

    @Override
    public void create() throws IOException {
        delegate.create();
    }

    @Override
    public File newFile(String fileName) throws IOException {
        return delegate.newFile(fileName);
    }

    @Override
    public File newFile() throws IOException {
        return delegate.newFile();
    }

    @Override
    public File newFolder(String folder) throws IOException {
        return delegate.newFolder(folder);
    }

    @Override
    public File newFolder(String... folderNames) throws IOException {
        return delegate.newFolder(folderNames);
    }

    @Override
    public File newFolder() throws IOException {
        return delegate.newFolder();
    }

    @Override
    public File getRoot() {
        return delegate.getRoot();
    }

    @Override
    public void delete() {
        delegate.delete();
    }

    @Override
    public Statement apply(Statement base, Description description) {
        return delegate.apply(base, description);
    }
}
