/*******************************************************************************
 * Copyright (c) 2017 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.ui.internal.rules;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.core.runtime.content.ITextContentDescriber;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.windup.ui.WindupUIPlugin;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

public class RulesetEditorDescriber extends org.eclipse.core.runtime.content.XMLContentDescriber implements ITextContentDescriber {
	
	public int describe(InputStream contents, IContentDescription description) throws IOException {
		super.describe(contents, description);
		contents.reset();
		String text = FileUtil.readStream(contents);
		if(text.trim().length() > 0) {
			return describe(text, description);
		}
		return INVALID;
	}
	
	public int describe(Reader contents, IContentDescription description) throws IOException {
		super.describe(contents, description);
		contents.reset();
		String text = read(contents);
		return describe(text, description);
	}
	
	private int describe(String text, IContentDescription description) {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(text));
		    Document doc = builder.parse(is);
		    NodeList rulesetList = doc.getElementsByTagName("ruleset"); //$NON-NLS-1$
		    if (rulesetList.getLength() > 0) {
		    	return VALID;
		    }
		} catch (Exception e) {
		}
		return INVALID;
	}

	static String read(Reader is) {
        StringBuffer sb = new StringBuffer(""); //$NON-NLS-1$
        try {
            char[] b = new char[4096];
            while(true) {
                int l = is.read(b, 0, b.length);
                if(l < 0) break;
                sb.append(new String(b, 0, l));
            }
            is.close();
        } catch (IOException e) {
        }
        return sb.toString();
    }
}
