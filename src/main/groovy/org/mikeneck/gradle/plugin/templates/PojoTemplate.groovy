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

class PojoTemplate extends AbstractTemplate {

    static final String TEMPLATE_NAME = 'templates/pojo-template.txt'

    String pkg
    Collection<String> imps
    String name
    Collection<String> fields
    Collection<String> constructors
    Collection<String> accessor

    @Override
    String getTemplateName() {
        return TEMPLATE_NAME
    }

    @Override
    String getContents() {
        return new SimpleTemplateEngine()
                .createTemplate(this.template)
                .make(this.model)
                .writeTo(new StringWriter())
                .toString()
    }

    @Override
    Map<String, ?> getModel() {
        return [
                packageName: pkg,
                imports: imps,
                name: name,
                constructors: constructors,
                privateFields: fields,
                accessorImpl: accessor]
    }
}
