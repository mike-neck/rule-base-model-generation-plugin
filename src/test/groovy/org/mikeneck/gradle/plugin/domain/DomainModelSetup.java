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
package org.mikeneck.gradle.plugin.domain;

import org.gradle.model.ModelSet;
import org.mikeneck.gradle.plugin.model.ClassEntry;
import org.mikeneck.gradle.plugin.model.ClassEntryPojo;
import org.mikeneck.gradle.plugin.model.ClassType;
import org.mikeneck.gradle.plugin.model.EnumEntry;
import org.mikeneck.gradle.plugin.model.EnumEntryPojo;
import org.mikeneck.gradle.plugin.model.ManagedModel;
import org.mikeneck.gradle.plugin.model.ManagedModelPojo;
import org.mikeneck.gradle.plugin.model.PropertyEntry;
import org.mikeneck.gradle.plugin.model.PropertyEntryPojo;
import org.mikeneck.gradle.plugin.model.Type;

public final class DomainModelSetup {

    private DomainModelSetup() {}

    public static ManagedModel getModelWithEnum() {
        ManagedModel model = new ManagedModelPojo();
        model.setSrcDir("src/main/groovy");
        model.setPackageName("org.mikeneck");
        ModelSet<ClassEntry> classes = model.getClasses();

        ClassEntryPojo person = new ClassEntryPojo();
        person.setName("Person");
        person.setType(ClassType.INTERFACE);
        ModelSet<PropertyEntry> personFields = person.getFields();
        personFields.add(new PropertyEntryPojo("name", Type.STRING));
        personFields.add(new PropertyEntryPojo("favorite", Type.ENUM, "Favorite"));
        classes.add(person);

        ClassEntryPojo favorite = new ClassEntryPojo();
        favorite.setName("Favorite");
        favorite.setType(ClassType.ENUM);
        ModelSet<EnumEntry> favValues = favorite.getValues();
        favValues.add(new EnumEntryPojo("GAME"));
        favValues.add(new EnumEntryPojo("READING"));
        classes.add(favorite);

        return model;
    }
}
