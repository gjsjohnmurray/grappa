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

package org.parboiled.parserunners;

import com.github.parboiled1.grappa.annotations.WillBeProtected;
import com.github.parboiled1.grappa.buffers.CharSequenceInputBuffer;
import com.github.parboiled1.grappa.buffers.InputBuffer;
import com.github.parboiled1.grappa.stack.DefaultValueStack;
import com.github.parboiled1.grappa.stack.ValueStack;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.parboiled.DefaultMatcherContext;
import org.parboiled.MatchHandler;
import org.parboiled.MatcherContext;
import org.parboiled.Rule;
import org.parboiled.errors.ParseError;
import org.parboiled.matchers.Matcher;
import org.parboiled.support.ParsingResult;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractParseRunner<V>
    implements ParseRunner<V>
{
    protected final Matcher rootMatcher;
    // TODO: make final
    protected List<ParseError> parseErrors = Lists.newArrayList();
    protected ValueStack<V> valueStack = new DefaultValueStack<V>();
    protected Object initialValueStackSnapshot;

    @WillBeProtected(version = "1.1")
    public AbstractParseRunner(@Nonnull final Rule rule)
    {
        rootMatcher = Preconditions.checkNotNull((Matcher) rule, "rule");
    }

    public final Matcher getRootMatcher()
    {
        return rootMatcher;
    }

    @Override
    // TODO: for now, parseErrors is @Nullable here
    public final ParseRunner<V> withParseErrors(
        final List<ParseError> parseErrors)
    {
        /*
         * TODO: figure out why the below is true... That really, really sucks
         *
         * The code below IS NOT equivalent to:
         *
         *     this.parseErrors.clear();
         *     this.parseErrors.addAll(parseErrors);
         *
         * No idea why :/
         *
         * Note that this event stands true for:
         *
         *     this.parseErrors.allAll(parseErrors);
         */
        this.parseErrors = parseErrors;
        return this;
    }

    @Override
    public final ParseRunner<V> withValueStack(
        @Nonnull final ValueStack<V> valueStack)
    {
        this.valueStack = Preconditions.checkNotNull(valueStack, "valueStack");
        initialValueStackSnapshot = valueStack.takeSnapshot();
        return this;
    }

    public final ValueStack<V> getValueStack()
    {
        return valueStack;
    }

    @Override
    public final ParsingResult<V> run(final CharSequence input)
    {
        Preconditions.checkNotNull(input, "input");
        return run(new CharSequenceInputBuffer(input));
    }

    @Override
    public final ParsingResult<V> run(final char[] input)
    {
        Preconditions.checkNotNull(input, "input");
        return run(new CharSequenceInputBuffer(input));
    }

    protected final void resetValueStack()
    {
        // FIXME: hack
        if (initialValueStackSnapshot == null)
            initialValueStackSnapshot = new ArrayList<>();
        valueStack.restoreSnapshot(initialValueStackSnapshot);
    }

    protected final MatcherContext<V> createRootContext(
        final InputBuffer inputBuffer, final MatchHandler matchHandler,
        final boolean fastStringMatching)
    {
        return new DefaultMatcherContext<V>(inputBuffer, valueStack,
            parseErrors, matchHandler, rootMatcher, fastStringMatching);
    }

    protected final ParsingResult<V> createParsingResult(final boolean matched,
        final MatcherContext<V> rootContext)
    {
        return new ParsingResult<V>(matched, rootContext.getNode(), valueStack,
            parseErrors, rootContext.getInputBuffer());
    }
}
