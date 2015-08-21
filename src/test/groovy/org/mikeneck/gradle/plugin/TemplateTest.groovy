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
import org.junit.Test

class TemplateTest {

    static final String INTERFACE = 'templates/interface-template.txt'

    final ClassLoader loader = this.class.classLoader

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

    @Test
    void interfaceTest() {
        def engine = new SimpleTemplateEngine()
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

    static class Model {
        String packageName
        List<String> imports
        boolean enumType
        String name
        List<String> entries
    }
}
