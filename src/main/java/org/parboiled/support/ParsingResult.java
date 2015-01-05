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

package org.parboiled.support;

import com.github.parboiled1.grappa.annotations.WillBePrivate;
import com.github.parboiled1.grappa.buffers.InputBuffer;
import com.github.parboiled1.grappa.stack.ValueStack;
import com.google.common.base.Preconditions;
import org.parboiled.Node;
import org.parboiled.errors.ParseError;
import org.parboiled.parserunners.BasicParseRunner;
import org.parboiled.parserunners.ReportingParseRunner;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * A simple container encapsulating the result of a parsing run.
 */
public final class ParsingResult<V>
{

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #isSuccess()} instead</p>
     */
    @WillBePrivate(version = "1.1")
    public final boolean matched;

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #getParseTree()} instead.</p>
     */
    @WillBePrivate(version = "1.1")
    public final Node<V> parseTreeRoot;

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #getTopStackValue()} instead</p>
     */
    @WillBePrivate(version = "1.1")
    public final V resultValue;

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #getValueStack()} instead</p>
     */
    @WillBePrivate(version = "1.1")
    public final ValueStack<V> valueStack;

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #getParseErrors()} instead</p>
     */
    @WillBePrivate(version = "1.1")
    public final List<ParseError> parseErrors;

    /**
     * DO NOT USE DIRECTLY!
     *
     * <p>Use {@link #getParseErrors()} instead</p>
     */
    @WillBePrivate(version = "1.1")
    public final InputBuffer inputBuffer;

    /**
     * Creates a new ParsingResult.
     *
     * @param matched true if the rule matched the input
     * @param parseTreeRoot the parse tree root node
     * @param valueStack the value stack of the parsing run
     * @param parseErrors the list of parse errors
     * @param inputBuffer the input buffer
     */
    public ParsingResult(final boolean matched, final Node<V> parseTreeRoot,
        @Nonnull final ValueStack<V> valueStack,
        @Nonnull final List<ParseError> parseErrors,
        @Nonnull final InputBuffer inputBuffer)
    {
        this.matched = matched;
        this.parseTreeRoot = parseTreeRoot;
        this.valueStack = Preconditions.checkNotNull(valueStack);
        resultValue = valueStack.isEmpty() ? null : valueStack.peek();
        this.parseErrors = Preconditions.checkNotNull(parseErrors);
        this.inputBuffer = Preconditions.checkNotNull(inputBuffer);
    }


    /**
     * Return true if this parse result is a match
     *
     * <p>Note that a result NOT having a match does not mean that it has
     * collected parse errors (see {@link #hasCollectedParseErrors()}; a
     * {@link BasicParseRunner}, for instance, does not collect errors.</p>
     *
     * @return see description
     */
    public boolean isSuccess()
    {
        return matched;
    }

    /**
     * Gets the generated parse tree, if any
     *
     * <p>Only a {@link ReportingParseRunner} will generate a parse tree.</p>
     *
     * @return the tree, or (unfortunately) {@code null} if no tree
     */
    @Nullable
    public Node<V> getParseTree()
    {
        return parseTreeRoot;
    }

    /**
     * Gets the value at the top of the stack, if any
     *
     * <p>Note that unfortunately the top of the stack can also be null, so this
     * method is not reliable.</p>
     *
     * @return see description
     */
    @Nullable
    public V getTopStackValue()
    {
        return resultValue;
    }

    /**
     * Get the value stack
     *
     * @return the value stack used during the parsing run
     */
    @Nonnull
    public ValueStack<V> getValueStack()
    {
        return valueStack;
    }

    /**
     * Get the parse errors generated by the parsing process, if any
     *
     * <p>Note that a {@link BasicParseRunner} <strong>does not</strong> store
     * any parsing errors.</p>
     *
     * @return see description
     * @see #isSuccess()
     */
    @Nonnull
    public List<ParseError> getParseErrors()
    {
        return parseErrors;
    }

    /**
     * Get the input buffer used by the parsing run
     *
     * @return see description
     */
    @Nonnull
    public InputBuffer getInputBuffer()
    {
        return inputBuffer;
    }

    /**
     * Has this result collected any parse errors?
     *
     * <p><strong>note</strong>: this method does not guarantee that the result
     * is a success; for this use {@link #isSuccess()} instead.</p>
     *
     * @return true if the parse error list is not empty
     */
    // TODO: not clear whether parseErrors can be null
    public boolean hasCollectedParseErrors()
    {
        return !parseErrors.isEmpty();
    }
}
