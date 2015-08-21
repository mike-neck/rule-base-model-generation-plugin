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
package org.mikeneck.gradle.plugin

import groovy.text.SimpleTemplateEngine
import org.junit.Before
import org.junit.Test

class TemplateTest {

    static final String INTERFACE = 'templates/interface-template.txt'

    final ClassLoader loader = this.class.classLoader

    SimpleTemplateEngine engine

    @Before
    void setup() {
        engine = new SimpleTemplateEngine()
    }

    static Map interfaceType() {
        [packageName: 'org.mikeneck.sample',
                imports: [
                        'import org.gradle.model.ModelMap;',
                        'import java.io.File;'],
                enumType: false,
                name: 'Sample',
                entries: [
                        'String getName();',
                        'void setName(String name);',
                        '',
                        'ModelMap<Storage> getStore();',
                        '',
                        'File getDirectory();',
                        'void setDirectory(File directory);'
                ]]
    }

    static Map enumType() {
        [packageName: 'org.mikeneck.sample',
                imports: [],
                enumType: true,
                name: 'StorageType',
                entries: ['LOCAL', 'S3', 'DROP_BOX', 'GOOGLE_DRIVE']]
    }

    @Test
    void interfaceTest() {
        String text = engine.createTemplate(loader.getResource(INTERFACE)).make(interfaceType()) as String
        assert text == $/|package org.mikeneck.sample;
                       |
                       |import org.gradle.model.ModelMap;
                       |import java.io.File;
                       |import org.gradle.model.Managed;
                       |
                       |@Managed
                       |public interface Sample {
                       |
                       |    String getName();
                       |    void setName(String name);
                       |
                       |    ModelMap<Storage> getStore();
                       |
                       |    File getDirectory();
                       |    void setDirectory(File directory);
                       |}
                       |/$.stripMargin()
    }

    @Test
    void enumTest() {
        def sw = new StringWriter()
        engine.createTemplate(loader.getResource(INTERFACE)).make(enumType()).writeTo(sw)
        assert sw.toString() == """\
                                   |package org.mikeneck.sample;
                                   |
                                   |
                                   |
                                   |public enum StorageType {
                                   |
                                   |    LOCAL,
                                   |    S3,
                                   |    DROP_BOX,
                                   |    GOOGLE_DRIVE
                                   |}
                                   |""".stripMargin()
    }
}