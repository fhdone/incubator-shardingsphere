/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.core.parse.lexer.dialect.mysql;

import org.apache.shardingsphere.core.parse.lexer.LexerAssert;
import org.apache.shardingsphere.core.parse.old.lexer.dialect.mysql.MySQLLexer;
import org.apache.shardingsphere.core.parse.old.lexer.token.Assist;
import org.apache.shardingsphere.core.parse.old.lexer.token.DefaultKeyword;
import org.apache.shardingsphere.core.parse.old.lexer.token.Literals;
import org.apache.shardingsphere.core.parse.old.lexer.token.Symbol;
import org.junit.Test;

public final class MySQLLexerTest {
    
    @Test
    public void assertNextTokenForHint() {
        MySQLLexer lexer = new MySQLLexer("SELECT * FROM XXX_TABLE /*! hint 1 \n xxx */ WHERE XX>1 /*!hint 2*/");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.SELECT, "SELECT");
        LexerAssert.assertNextToken(lexer, Symbol.STAR, "*");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.FROM, "FROM");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XXX_TABLE");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.WHERE, "WHERE");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XX");
        LexerAssert.assertNextToken(lexer, Symbol.GT, ">");
        LexerAssert.assertNextToken(lexer, Literals.INT, "1");
        LexerAssert.assertNextToken(lexer, Assist.END, "");
    }
    
    @Test
    public void assertNextTokenForComment() {
        MySQLLexer lexer = new MySQLLexer("SELECT * FROM XXX_TABLE # xxx ");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.SELECT, "SELECT");
        LexerAssert.assertNextToken(lexer, Symbol.STAR, "*");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.FROM, "FROM");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XXX_TABLE");
        LexerAssert.assertNextToken(lexer, Assist.END, "");
    }
    
    @Test
    public void assertNextTokenForMultipleLinesComment() {
        MySQLLexer lexer = new MySQLLexer("SELECT * FROM XXX_TABLE # comment 1 \n #comment 2 \r\n WHERE XX<=1");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.SELECT, "SELECT");
        LexerAssert.assertNextToken(lexer, Symbol.STAR, "*");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.FROM, "FROM");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XXX_TABLE");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.WHERE, "WHERE");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XX");
        LexerAssert.assertNextToken(lexer, Symbol.LT_EQ, "<=");
        LexerAssert.assertNextToken(lexer, Literals.INT, "1");
        LexerAssert.assertNextToken(lexer, Assist.END, "");
    }
    
    @Test
    public void assertNextTokenForVariable() {
        MySQLLexer lexer = new MySQLLexer("SELECT @x1:=1 FROM XXX_TABLE WHERE XX=  @@global.x1");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.SELECT, "SELECT");
        LexerAssert.assertNextToken(lexer, Literals.VARIABLE, "@x1");
        LexerAssert.assertNextToken(lexer, Symbol.COLON_EQ, ":=");
        LexerAssert.assertNextToken(lexer, Literals.INT, "1");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.FROM, "FROM");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XXX_TABLE");
        LexerAssert.assertNextToken(lexer, DefaultKeyword.WHERE, "WHERE");
        LexerAssert.assertNextToken(lexer, Literals.IDENTIFIER, "XX");
        LexerAssert.assertNextToken(lexer, Symbol.EQ, "=");
        LexerAssert.assertNextToken(lexer, Literals.VARIABLE, "@@global.x1");
        LexerAssert.assertNextToken(lexer, Assist.END, "");
    }
}
