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

package org.parboiled;

import com.github.fge.grappa.matchers.base.Matcher;
import com.github.fge.grappa.parsers.BaseParser;
import com.github.fge.grappa.rules.Rule;
import org.parboiled.annotations.BuildParseTree;
import org.parboiled.support.Var;
import org.parboiled.test.ParboiledTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;

public class ActionVarTest extends ParboiledTest<Integer>
{

    @BuildParseTree
    static class Parser extends BaseParser<Integer>
    {

        @SuppressWarnings("InfiniteRecursion")
        public Rule A() {
            final Var<List<String>> list = new Var<List<String>>(new ArrayList<String>());
            return sequence('a', optional(A(), list.get().add("Text"), push(list.get().size())));
        }

    }

    @Test
    public void test() {
        final Parser parser = Parboiled.createParser(Parser.class);
        final Matcher rule = (Matcher) parser.A();

        assertEquals(rule.getClass().getName(), "com.github.fge.grappa.matchers.wrap.VarFramingMatcher");

        test(rule, "aaaa").hasNoErrors();
    }

}
