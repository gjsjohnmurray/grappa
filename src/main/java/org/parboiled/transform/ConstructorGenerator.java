/*
 * Copyright (C) 2009-2011 Mathias Doenitz
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.parboiled.transform;

import com.github.parboiled1.grappa.annotations.VisibleForDocumentation;
import com.github.parboiled1.grappa.transform.CodeBlock;
import com.google.common.base.Preconditions;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.parboiled.BaseParser;
import org.parboiled.support.Checks;

import java.util.List;

import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.parboiled.transform.AsmUtils.createArgumentLoaders;

/**
 * Adds one constructor for each of the ParserClassNode.constructors,
 * which simply delegates to the respective super constructor.
 */
@VisibleForDocumentation
public final class ConstructorGenerator
{
    public void process(final ParserClassNode classNode)
    {
        Preconditions.checkNotNull(classNode, "classNode");
        Checks.ensure(!classNode.getConstructors().isEmpty(),
            "Could not extend parser class '%s', no constructor visible to" +
            " derived classes found", classNode.getParentType().getClassName());
        for (final MethodNode constructor: classNode.getConstructors())
            createConstuctor(classNode, constructor);

        createNewInstanceMethod(classNode);
    }

    private static void createConstuctor(final ParserClassNode classNode,
        final MethodNode constructor)
    {
        final List<String> exceptions = constructor.exceptions;
        final MethodNode newConstructor = new MethodNode(ACC_PUBLIC,
            constructor.name, constructor.desc, constructor.signature,
            exceptions.toArray(new String[exceptions.size()])
        );

        final InsnList instructions = newConstructor.instructions;

        final CodeBlock block = CodeBlock.newCodeBlock()
            .aload(0)
            .addAll(createArgumentLoaders(constructor.desc))
            .invokespecial(classNode.getParentType().getInternalName(),
                "<init>", constructor.desc)
            .rawReturn();

        instructions.add(block.getInstructionList());

        classNode.methods.add(newConstructor);
    }

    private static void createNewInstanceMethod(final ParserClassNode classNode)
    {
        final String desc = "()L" + Type.getType(BaseParser.class)
            .getInternalName() + ';';
        final MethodNode method = new MethodNode(ACC_PUBLIC, "newInstance",
            desc, null, null);
        final InsnList instructions = method.instructions;

        instructions.add(new TypeInsnNode(NEW, classNode.name));
        instructions.add(new InsnNode(DUP));
        instructions.add(new MethodInsnNode(INVOKESPECIAL, classNode.name,
            "<init>", "()V", false));
        instructions.add(new InsnNode(ARETURN));

        classNode.methods.add(method);
    }
}