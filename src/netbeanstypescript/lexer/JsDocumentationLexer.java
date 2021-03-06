/*
 * Adapted from NetBeans 10.0; modified for nbts
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package netbeanstypescript.lexer;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.lexer.Token;
import netbeanstypescript.lexer.api.JsDocumentationTokenId;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;


/**
 * Base JsDocumentation Lexer class.
 * <p>
 * <i>Created on base of {@link JsDocumentationLexer}</i>
 *
 * @author Martin Fousek <marfous@netbeans.org>
 */
public final class JsDocumentationLexer implements Lexer<JsDocumentationTokenId> {

    private static final Logger LOGGER = Logger.getLogger(JsDocumentationLexer.class.getName());

    private final JsDocumentationColoringLexer coloringLexer;

    private TokenFactory<JsDocumentationTokenId> tokenFactory;

    private JsDocumentationLexer(LexerRestartInfo<JsDocumentationTokenId> info) {
        coloringLexer = new JsDocumentationColoringLexer(info);
        tokenFactory = info.tokenFactory();
    }

    public static JsDocumentationLexer create(LexerRestartInfo<JsDocumentationTokenId> info) {
        synchronized (JsDocumentationLexer.class) {
            return new JsDocumentationLexer(info);
        }
    }

    @Override
    public Token<JsDocumentationTokenId> nextToken() {
        try {
            JsDocumentationTokenId tokenId = coloringLexer.nextToken();
            LOGGER.log(Level.FINEST, "Lexed token is {0}", tokenId);
            Token<JsDocumentationTokenId> token = null;
            if (tokenId != null) {
                token = tokenFactory.createToken(tokenId);
            }
            return token;
        } catch (IOException ex) {
            Logger.getLogger(JsDocumentationLexer.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object state() {
        return coloringLexer.getState();
    }

    @Override
    public void release() {
    }
}
