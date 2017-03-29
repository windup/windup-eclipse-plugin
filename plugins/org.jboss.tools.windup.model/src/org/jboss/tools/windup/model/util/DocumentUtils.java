/*******************************************************************************
 * Copyright (c) 2016 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.windup.model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import org.apache.commons.io.FileUtils;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IRegion;
import org.jboss.tools.windup.model.Activator;

import com.google.common.collect.Lists;

/**
 * Utility for editing document.
 */
public class DocumentUtils {

	/**
	 * Returns the line of text from the specified resource at the specified line number.
	 */
	public static String getLine(IResource resource, int lineNumber) {
		try {
			String contents = FileUtils.readFileToString(resource.getLocation().toFile());
			Document document = new Document(contents);
			IRegion region = document.getLineInformation(lineNumber);
			return document.get(region.getOffset(), region.getLength());
		} catch (IOException | BadLocationException e) {
			Activator.log(e);
		}
		return "";
	}

	public static int getLineNumbers(IResource resource) {
		try {
			String contents = FileUtils.readFileToString(resource.getLocation().toFile());
			Document document = new Document(contents);
			return document.getNumberOfLines();
		} catch (Exception e) {
			Activator.log(e);
		}
		return 0;
	}
	
	/**
	 * Returns true if the line of text within the specified resource differs from that of
	 * the specified line of text.
	 */
	public static boolean differs(IResource resource, int lineNumber, String text) {
		String line = DocumentUtils.getLine(resource, lineNumber);
		return !Objects.equals(line, text);
	}
	
	/**
	 * Replaces the chunk of text represented as <code>searchString</code> with the specified <code>replacement</code>
	 * text at the given line number in the specified resource. 
	 */
	public static Document replace(IResource resource, int lineNumber, String searchString, String replacement) {
		try {
			String contents = FileUtils.readFileToString(resource.getLocation().toFile());
			Document document = new Document(contents);
			IRegion info = document.getLineInformation(lineNumber);
			FindReplaceDocumentAdapter adapter = new FindReplaceDocumentAdapter(document);
			IRegion search = adapter.find(info.getOffset(), searchString, true, true, true, false);
			if (search != null) {
				document.replace(search.getOffset(), search.getLength(), replacement);
			}
			return document;
		} catch (IOException | BadLocationException e) {
			Activator.log(e);
		}
		return null;
	}
	
	/**
	 * Deletes the line of text in the specified resource at the specified line number.
	 */
	public static Document deleteLine(IResource resource, int lineNumber) {
		try {
			String contents = FileUtils.readFileToString(resource.getLocation().toFile());
			Document document = new Document(contents);
			IRegion info = document.getLineInformation(lineNumber);
			document.replace(info.getOffset(), info.getLength()+1, null);
			return document;
		} catch (IOException | BadLocationException e) {
			Activator.log(e);
		}
		return null;
	}
	
	/**
	 * Inserts the provided chunk of text represented as <code>newLine</code> in the given
	 * resource at the specified line.
	 */
	public static Document insertLine(IResource resource, int lineNumber, String newLine) {
		try {
			String contents = FileUtils.readFileToString(resource.getLocation().toFile());
			Document document = new Document(contents);
			// TODO: This seems cumbersome. Find a cleaner way of performing insert.
			IRegion previousLine = document.getLineInformation(lineNumber);
			List<String> indentChars = DocumentUtils.getLeadingChars(document, previousLine);
			
			StringBuilder builder = new StringBuilder();
			builder.append(System.lineSeparator());
			for (String indentChar : indentChars) {
				builder.append(indentChar);
			}
			builder.append(newLine);
			
			int newLineOffset = previousLine.getOffset() + previousLine.getLength();
			document.replace(newLineOffset, 0, builder.toString());
			return document;
		} catch (IOException | BadLocationException e) {
			Activator.log(e);
		}
		return null;
	}
	
	/**
	 * Returns a list of characters leading up to the first non-whitespace character.
	 */
	private static List<String> getLeadingChars(Document document, IRegion line) throws BadLocationException {
		List<String> result = Lists.newArrayList();
		int pos= line.getOffset();
		int max= pos + line.getLength();
		while (pos < max) {
			char next = document.getChar(pos);
			if (!Character.isWhitespace(next))
				break;
			result.add(String.valueOf(next));
			pos++;
		}
		return result;
	}

	/**
	 * Replace text of one resource <code>left</code> with the text of the other <code>right</code>.
	 */
	public static void replace(IResource left, IResource right) {
		File leftFile = left.getLocation().toFile();
		File rightFile = right.getLocation().toFile();
		try {
			FileOutputStream outputFile = new FileOutputStream(leftFile, false);
			FileInputStream inputFile = new FileInputStream(rightFile);
			byte[] buffer = new byte[1024];
			int length;
			while((length = inputFile.read(buffer)) > 0) {
				outputFile.write(buffer, 0, length);
			}
			outputFile.close();
			inputFile.close();
			left.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (Exception e) {
			Activator.log(e);
		}
	}
}
