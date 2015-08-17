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
package org.mikeneck.gradle.plugin.model;

import org.gradle.model.ModelSet;

import java.util.Objects;
import java.util.StringJoiner;

public class ManagedModelPojo implements ManagedModel {

    private String dir;

    private String packageName;

    private ModelSet<ClassEntry> classes;

    public ManagedModelPojo() {
        this.classes = new ModelSetImpl<>();
    }

    public ManagedModelPojo(ManagedModel mm) {
        this.dir = mm.getSrcDir();
        this.packageName = mm.getPackageName();
        this.classes = mm.getClasses();
    }

    public ManagedModelPojo(String dir, String packageName, ModelSet<ClassEntry> classes) {
        this.dir = dir;
        this.packageName = packageName;
        this.classes = classes;
    }

    @Override
    public String getSrcDir() {
        return dir;
    }

    @Override
    public void setSrcDir(String dir) {
        this.dir = dir;
    }

    @Override
    public String getPackageName() {
        return packageName;
    }

    @Override
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public ModelSet<ClassEntry> getClasses() {
        return classes;
    }

    public void setClasses(ModelSet<ClassEntry> classes) {
        this.classes = classes;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", "ManagedModelPojo:[", "]")
                .add("dir: [" + (dir == null ? "null" : dir) + "]")
                .add("packageName: [" + (packageName == null ? "null" : packageName) + "]")
                .add("classes: [" + (classes == null ? "null" : classes) + "]")
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ManagedModel)) return false;
        ManagedModel that = (ManagedModel) o;
        return Objects.equals(dir, that.getSrcDir()) &&
                Objects.equals(packageName, that.getPackageName()) &&
                Objects.equals(classes, that.getClasses());
    }

    @Override
    public int hashCode() {
        return Objects.hash(dir, packageName, classes);
    }
}
