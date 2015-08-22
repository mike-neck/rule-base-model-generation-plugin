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

class InterfaceTemplate extends AbstractTemplate {

    static final String TEMPLATE_NAME = 'templates/interface-template.txt'

    String pkg
    List<String> imps
    boolean isEnum
    String name
    List<String> entries

    @Override
    String getTemplateName() {
        return TEMPLATE_NAME
    }

    @Override
    Map<String, ?> getModel() {
        return [
                packageName: pkg,
                imports: imps,
                enumType: isEnum,
                name: name,
                entries: entries
        ]
    }

    @Override
    String getContents() {
        return new SimpleTemplateEngine()
                .createTemplate(this.template)
                .make(this.model)
                .writeTo(new StringWriter())
                .toString()
    }
}
