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
package org.mikeneck.gradle.plugin.util

import org.gradle.api.Action
import org.gradle.model.ModelSet
import org.mikeneck.gradle.plugin.model.*
import spock.lang.Specification
import spock.lang.Unroll

import static org.mikeneck.gradle.plugin.model.ClassType.ENUM
import static org.mikeneck.gradle.plugin.model.ClassType.INTERFACE
import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.INVALID
import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.VALID
import static org.mikeneck.gradle.plugin.util.ClassEntryUtil.wellDefined
import static org.mikeneck.gradle.plugin.util.ClassEntryUtilSpec.Entry.entry
import static org.mikeneck.gradle.plugin.util.ClassEntryUtilSpec.Entry.name
import static org.mikeneck.gradle.plugin.util.ClassEntryUtilSpec.MockEnum.randomValues
import static org.mikeneck.gradle.plugin.util.ClassEntryUtilSpec.MockProperty.randomFields

class ClassEntryUtilSpec extends Specification {

    @Unroll
    def '#classEntry is #validity'() {
        expect:
        assert wellDefined(classEntry) == validity.asBoolean

        where:
        classEntry                      | validity
        validInterface()                | VALID
        validEnum()                     | VALID
        nameIsNull()                    | INVALID
        emptyName()                     | INVALID
        nameNotMatchesRule()            | INVALID
        nameIsInteger()                 | INVALID
        nameIsObject()                  | INVALID
        typeIsNull()                    | INVALID
        enumWithoutValues()             | INVALID
        enumWithValuesAndFields()       | INVALID
        interfaceWithoutFields()        | INVALID
        interfaceWithFieldsAndValues()  | INVALID
    }

    static ClassEntry validInterface() {
        return name('ValidInterface')
                .type(INTERFACE)
                .fields(randomFields(3))
                .build()
    }

    static ClassEntry validEnum() {
        return name('ValidEnum')
                .type(ENUM)
                .values(randomValues(3))
                .build()
    }

    static ClassEntry nameIsNull() {
        entry()
    }

    static ClassEntry emptyName() {
        return name('')
                .type(INTERFACE)
                .fields(randomFields(3))
                .build()
    }

    static ClassEntry nameNotMatchesRule() {
        return name('invalidNameForType')
                .type(INTERFACE)
                .fields(randomFields(3))
                .build()
    }

    static ClassEntry nameIsInteger() {
        return name('Integer')
                .type(INTERFACE)
                .fields(randomFields(3))
                .build()
    }

    static ClassEntry nameIsObject() {
        return name('Object')
                .type(INTERFACE)
                .fields(randomFields(3))
                .build()
    }

    static ClassEntry typeIsNull() {
        return name('TypeIsNull')
                .build()
    }

    static ClassEntry enumWithoutValues() {
        return name('EnumWithoutValues')
                .type(ENUM)
                .values(randomValues(0))
                .build()
    }

    static ClassEntry enumWithValuesAndFields() {
        return name('EnumWithValuesAndFields')
                .type(ENUM)
                .values(randomValues(3))
                .fields(randomFields(1))
    }

    static ClassEntry interfaceWithoutFields() {
        return name('InterfaceWithoutFields')
                .type(INTERFACE)
                .fields(randomFields(0))
                .build()
    }

    static ClassEntry interfaceWithFieldsAndValues() {
        return name('InterfaceWithFieldsAndValues')
                .type(INTERFACE)
                .fields(randomFields(3))
                .values(randomValues(1))
    }

    static class Entry implements ClassEntry {
        String name
        ClassType type
        final ModelSet<PropertyEntry> fields = new SetImpl<>()
        final ModelSet<EnumEntry> values = new SetImpl<>()
        @Override
        String toString() {
            "ClassEntry(name[$name], type[$type], fields-size[${fields.size()}], values-size[${values.size()}])"
        }
        static ClassEntry entry() {
            new Entry()
        }
        static def name(String n) {
            [type: {ClassType t ->
                [fields: {Collection<PropertyEntry> f ->
                    [build:{
                        def e = new Entry(name: n, type: t)
                        e.fields.addAll(f)
                        return e
                    }, values: {Collection<EnumEntry> v ->
                        def e = new Entry(name: n, type: t)
                        e.fields.addAll(f)
                        e.values.addAll(v)
                        return e
                    }]
                }, values: {Collection<EnumEntry> v ->
                    [build: {
                        def e = new Entry(name: n, type: t)
                        e.values.addAll(v)
                        return e
                    }, fields: {Collection<PropertyEntry> f ->
                        def e = new Entry(name: n, type: t)
                        e.fields.addAll(f)
                        e.values.addAll(v)
                        return e
                    }]
                }, build: {
                    new Entry(name: n, type: t)
                }]
            }, build: {
                new Entry(name: n)
            }]
        }
    }

    static char randSmallChar() {
        def r = new IntRange(('a' as char) as int, ('z' as char) as int)
        def i = new Random().nextInt(r.size())
        return r[i] as char
    }

    static class MockProperty implements PropertyEntry {
        String name
        Type type
        String refType
        static PropertyEntry randomProperty() {
            def n = "a${(1..4).collect{randSmallChar()}.join('')}"
            def r = "A${(1..4).collect{randSmallChar()}.join('')}"
            def vs = Type.values()
            def t = vs[new Random().nextInt(vs.size())]
            new MockProperty(name: n, type: t, refType: r)
        }
        static Collection<PropertyEntry> randomFields(int size) {
            if (size < 0) throw new IllegalArgumentException('size should be larger than or equals 0')
            if (size == 0) return Collections.emptySet()
            else return new IntRange(1, size).collect{randomProperty()}
        }
    }

    static class MockEnum implements EnumEntry {
        String value
        static EnumEntry randomEnum() {
            new MockEnum(value: (1..5).collect{randSmallChar()}.join('').toUpperCase())
        }
        static Collection<EnumEntry> randomValues(int size) {
            if (size < 0) throw new IllegalArgumentException('size should be larger than or equals 0')
            if (size == 0) return Collections.emptySet()
            else return new IntRange(1, size).collect{randomEnum()}
        }
    }

    static class SetImpl<T> extends HashSet<T> implements ModelSet<T> {
        @Override
        void create(Action<? super T> action) {}

        @Override
        void beforeEach(Action<? super T> configAction) {}

        @Override
        void afterEach(Action<? super T> configAction) {}
    }
}
