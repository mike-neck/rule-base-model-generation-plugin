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
package org.mikeneck.gradle.plugin.templates

import groovy.text.SimpleTemplateEngine
import org.junit.Before
import org.junit.Test

/**
 * Pojo template accepts map with data...
 * <ul>
 *     <li>{@code imports} - {@code List<String>} - {@code import} declarations</li>
 *     <li>{@code name} - {@code String} - a name of type</li>
 *     <li>{@code privateFields} - {@code List<String>} - a list of type declarations</li>
 *     <li>{@code accessorImpl} - {@code List<String>} - a list of accessor implementations</li>
 * </ul>
 */
class PojoTest {

    static final String TEMPLATE = 'templates/pojo-template.txt'

    static final String POJO_EXPECTED = """\
           |package org.mikeneck.sample;
           |
           |import org.gradle.model.ModelSet;
           |import java.io.File;
           |
           |public class SamplePojo implements Sample {
           |
           |    private String name;
           |
           |    private ModelSet<Storage> stores;
           |
           |    private File directory;
           |
           |    @Override
           |    public String getName() {
           |        return name;
           |    }
           |
           |    @Override
           |    public void setName(String name) {
           |        this.name = name;
           |    }
           |
           |    @Override
           |    public ModelSet<Storage> getStores() {
           |        return stores;
           |    }
           |
           |    public void setStores(ModelSet<Storage> stores) {
           |        this.Stores = Stores;
           |    }
           |
           |    @Override
           |    public File getDirectory() {
           |        return name;
           |    }
           |
           |    @Override
           |    public void setDirectory(File directory) {
           |        this.directory = directory;
           |    }
           |
           |}
           |""".stripMargin()

    final ClassLoader loader = this.class.classLoader

    SimpleTemplateEngine engine

    StringWriter sw

    @Before
    void setup() {
        engine = new SimpleTemplateEngine()
        sw = new StringWriter()
    }

    @Test
    void templateTest() {
        def text = engine
                .createTemplate(loader.getResource(TEMPLATE))
                .make(model())
                .writeTo(sw)
                .toString()
        assert text == POJO_EXPECTED
    }

    private static Map model() {
        [
                packageName: 'org.mikeneck.sample',
                imports: [
                        'import org.gradle.model.ModelSet;',
                        'import java.io.File;'],
                name: 'Sample',
                privateFields: [
                        '    private String name;\n',
                        '    private ModelSet<Storage> stores;\n',
                        '    private File directory;\n'],
                accessorImpl: [
                        """|    @Override
                           |    public String getName() {
                           |        return name;
                           |    }
                           |""".stripMargin(),
                        """|    @Override
                           |    public void setName(String name) {
                           |        this.name = name;
                           |    }
                           |""".stripMargin(),
                        """|    @Override
                           |    public ModelSet<Storage> getStores() {
                           |        return stores;
                           |    }
                           |""".stripMargin(),
                        """|    public void setStores(ModelSet<Storage> stores) {
                           |        this.Stores = Stores;
                           |    }
                           |""".stripMargin(),
                        """|    @Override
                           |    public File getDirectory() {
                           |        return name;
                           |    }
                           |""".stripMargin(),
                        """|    @Override
                           |    public void setDirectory(File directory) {
                           |        this.directory = directory;
                           |    }
                           |""".stripMargin()]
        ]
    }

    @Test
    void withPojoTemplateClass() {
        def map = model()
        def tmp = new PojoTemplate(
                pkg: map.packageName,
                imps: map.imports,
                name: map.name,
                fields: map.privateFields,
                accessor: map.accessorImpl)
        assert tmp.contents == POJO_EXPECTED
    }
}
