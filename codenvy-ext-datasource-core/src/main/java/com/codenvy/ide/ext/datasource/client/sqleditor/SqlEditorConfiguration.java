package com.codenvy.ide.ext.datasource.client.sqleditor;

import javax.validation.constraints.NotNull;

import com.codenvy.ide.collections.Collections;
import com.codenvy.ide.collections.StringMap;
import com.codenvy.ide.ext.datasource.client.SqlEditorExtension;
import com.codenvy.ide.ext.datasource.client.sqleditor.codeassist.SqlCodeAssistProcessor;
import com.codenvy.ide.text.Document;
import com.codenvy.ide.texteditor.api.TextEditorConfiguration;
import com.codenvy.ide.texteditor.api.TextEditorPartView;
import com.codenvy.ide.texteditor.api.codeassistant.CodeAssistProcessor;
import com.codenvy.ide.texteditor.api.parser.BasicTokenFactory;
import com.codenvy.ide.texteditor.api.parser.CmParser;
import com.codenvy.ide.texteditor.api.parser.Parser;

public class SqlEditorConfiguration extends TextEditorConfiguration {

    private SqlCodeAssistProcessor codeAssistProcessor;

    @Override
    public Parser getParser(@NotNull final TextEditorPartView view) {
        CmParser parser = getParserForMime(SqlEditorExtension.GENERIC_SQL_MIME_TYPE);
        parser.setNameAndFactory("sql", new BasicTokenFactory());
        return parser;
    }

    @Override
    public StringMap<CodeAssistProcessor> getContentAssistantProcessors(final @NotNull TextEditorPartView view) {
        StringMap<CodeAssistProcessor> map = Collections.createStringMap();
        map.put(Document.DEFAULT_CONTENT_TYPE, getOrCreateCodeAssistProcessor());
        return map;
    }

    private SqlCodeAssistProcessor getOrCreateCodeAssistProcessor() {
        if (codeAssistProcessor == null) {
            codeAssistProcessor = new SqlCodeAssistProcessor();
        }
        return codeAssistProcessor;
    }
}
