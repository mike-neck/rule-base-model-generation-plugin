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

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class FieldVisitor extends VoidVisitorAdapter<Void> {

    private final List<Type> types = new ArrayList<>();

    private final List<String> data = new ArrayList<>();

    @Override
    public void visit(FieldDeclaration n, Void arg) {
        types.add(n.getType());
        data.add(n.getVariables()
                .stream()
                .map(Node::toStringWithoutComments)
                .collect(joining(", ")));
    }

    public List<Type> getTypes() {
        return types;
    }

    public List<String> getData() {
        return data;
    }
}
