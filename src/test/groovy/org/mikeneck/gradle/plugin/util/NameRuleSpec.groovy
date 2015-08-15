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

import spock.lang.Specification
import spock.lang.Unroll

import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.INVALID
import static org.mikeneck.gradle.plugin.test.support.BooleanExpected.VALID
import static org.mikeneck.gradle.plugin.util.NameRule.*

class NameRuleSpec extends Specification {

    @Unroll
    def '#type accepts "#entry" as #validity.' () {
        expect:
        assert type.matching(entry) == validity.asBoolean

        where:
        type                    | entry         | validity
        ENUM_VALUE_PATTERN      | 'A'           | VALID
        ENUM_VALUE_PATTERN      | 'a'           | INVALID
        ENUM_VALUE_PATTERN      | '1'           | INVALID
        ENUM_VALUE_PATTERN      | '_A'          | INVALID
        ENUM_VALUE_PATTERN      | 'Aa'          | INVALID
        ENUM_VALUE_PATTERN      | 'AB'          | VALID
        ENUM_VALUE_PATTERN      | 'A1'          | VALID
        ENUM_VALUE_PATTERN      | 'aA'          | INVALID
        ENUM_VALUE_PATTERN      | '1A'          | INVALID
        ENUM_VALUE_PATTERN      | 'AA_a'        | INVALID
        ENUM_VALUE_PATTERN      | 'AA_A'        | VALID
        ENUM_VALUE_PATTERN      | 'A_1'         | VALID
        ENUM_VALUE_PATTERN      | 'SPEC_OF_INT' | VALID

        TYPE_NAME_PATTERN       | 'A'           | VALID
        TYPE_NAME_PATTERN       | 'a'           | INVALID
        TYPE_NAME_PATTERN       | '1'           | INVALID
        TYPE_NAME_PATTERN       | 'Aa'          | VALID
        TYPE_NAME_PATTERN       | 'A1'          | VALID
        TYPE_NAME_PATTERN       | 'aA'          | INVALID
        TYPE_NAME_PATTERN       | 'a1'          | INVALID
        TYPE_NAME_PATTERN       | 'AA'          | VALID
        TYPE_NAME_PATTERN       | '1A'          | INVALID
        TYPE_NAME_PATTERN       | '1a'          | INVALID
        TYPE_NAME_PATTERN       | 'AsNum'       | VALID
        TYPE_NAME_PATTERN       | 'Func1'       | VALID

        PROPERTY_NAME_PATTERN   | 'A'           | INVALID
        PROPERTY_NAME_PATTERN   | 'a'           | VALID
        PROPERTY_NAME_PATTERN   | '1'           | INVALID
        PROPERTY_NAME_PATTERN   | '_'           | INVALID
        PROPERTY_NAME_PATTERN   | 'a1'          | VALID
        PROPERTY_NAME_PATTERN   | 'A1'          | INVALID
        PROPERTY_NAME_PATTERN   | '_1'          | INVALID
        PROPERTY_NAME_PATTERN   | 'sus'         | VALID
        PROPERTY_NAME_PATTERN   | 'asIf'        | VALID
        PROPERTY_NAME_PATTERN   | 'as1'         | VALID
        PROPERTY_NAME_PATTERN   | 'moreThan1'   | VALID

        PACKAGE_NAME_PATTERN    | 'org'         | VALID
        PACKAGE_NAME_PATTERN    | '1s'          | INVALID
        PACKAGE_NAME_PATTERN    | 'Invalid'     | INVALID
        PACKAGE_NAME_PATTERN    | 'a'           | VALID
        PACKAGE_NAME_PATTERN    | 'org.1s'      | INVALID
        PACKAGE_NAME_PATTERN    | 'org.Inv'     | INVALID
        PACKAGE_NAME_PATTERN    | 'org.valid'   | VALID
        PACKAGE_NAME_PATTERN    | 'org.valid2'  | VALID
        PACKAGE_NAME_PATTERN    | 'deep.pkg.ptn'| VALID
        PACKAGE_NAME_PATTERN    | 'dot.end.'    | INVALID
    }
}
