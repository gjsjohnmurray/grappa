/*
 * Copyright (C) 2014 Francis Galiegue <fgaliegue@gmail.com>
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

package com.github.parboiled1.grappa.parserunners;

import org.parboiled.BaseParser;
import org.parboiled.Parboiled;
import org.parboiled.Rule;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ParseRunner;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public final class BasicParseRunnerTest
{
    static class SimpleParser
        extends BaseParser<Object>
    {
        Rule rule()
        {
            return oneOrMore('a');
        }
    }

    private final SimpleParser parser
        = Parboiled.createParser(SimpleParser.class);
    private final ParseRunner<Object> runner
        = new BasicParseRunner<Object>(parser.rule());

    @Test
    public void basicParseRunnerCanReliablyReportErrors()
    {
        assertThat(runner.run("bbb").hasMatch()).as("errors are reported")
            .isFalse();
    }
}