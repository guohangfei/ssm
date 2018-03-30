// Copyright (c) 2003-present, Jodd Team (http://jodd.org)
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions are met:
//
// 1. Redistributions of source code must retain the above copyright notice,
// this list of conditions and the following disclaimer.
//
// 2. Redistributions in binary form must reproduce the above copyright
// notice, this list of conditions and the following disclaimer in the
// documentation and/or other materials provided with the distribution.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
// POSSIBILITY OF SUCH DAMAGE.

package utils.lagarto.dom;

import jodd.lagarto.Doctype;
import jodd.lagarto.Tag;
import jodd.lagarto.TagType;
import jodd.lagarto.TagVisitor;
import jodd.lagarto.dom.*;
import jodd.lagarto.dom.CData;
import jodd.lagarto.dom.Comment;
import jodd.lagarto.dom.Document;
import jodd.lagarto.dom.DocumentType;
import jodd.lagarto.dom.Element;
import jodd.lagarto.dom.HtmlFosterRules;
import jodd.lagarto.dom.HtmlImplicitClosingRules;
import jodd.lagarto.dom.HtmlVoidRules;
import jodd.lagarto.dom.LagartoDOMBuilder;
import jodd.lagarto.dom.Node;
import jodd.lagarto.dom.Text;
import jodd.lagarto.dom.XmlDeclaration;
import jodd.log.Logger;
import jodd.log.LoggerFactory;
import jodd.util.CharSequenceUtil;
import jodd.util.StringPool;
import jodd.util.Util;

/**
 * Lagarto tag visitor that builds DOM tree.
 * It (still) does not build the tree <i>fully</i> by the HTML specs,
 * however, it works good enough for any sane HTML out there.
 * In the default mode, the tree builder does <b>not</b> change
 * the order of the elements, so the returned tree reflects
 * the input. So if the input contains crazy stuff, the tree will
 * be weird, too :)
 * <p>
 * In experimental <i>html-plus</i> mode we do have some
 * further HTML5 rules implemented, that according to some rules
 * may change the node position. However, not all rules are
 * implemented (yet) and this is still just experimental.
 */
public class LagartoDOMBuilderTagVisitor implements TagVisitor {

	private static final Logger log = LoggerFactory.getLogger(LagartoDOMBuilderTagVisitor.class);

	protected final jodd.lagarto.dom.LagartoDOMBuilder domBuilder;
	protected final jodd.lagarto.dom.HtmlImplicitClosingRules implRules = new HtmlImplicitClosingRules();
	protected jodd.lagarto.dom.HtmlVoidRules htmlVoidRules;

	protected jodd.lagarto.dom.Document rootNode;
	protected jodd.lagarto.dom.Node parentNode;

	/**
	 * While enabled, nodes will be added to the DOM tree.
	 * Useful for skipping some tags.
	 */
	protected boolean enabled;

	public LagartoDOMBuilderTagVisitor(final LagartoDOMBuilder domBuilder) {
		this.domBuilder = domBuilder;
	}

	/**
	 * Returns root {@link jodd.lagarto.dom.Document document} node of parsed DOM tree.
	 */
	public jodd.lagarto.dom.Document getDocument() {
		return rootNode;
	}

	// ---------------------------------------------------------------- start/end

	/**
	 * Starts with DOM building.
	 * Creates root {@link jodd.lagarto.dom.Document} node.
	 */
	@Override
	public void start() {
		log.debug("DomTree builder started");

		if (rootNode == null) {
			rootNode = new Document(domBuilder.config);
		}
		parentNode = rootNode;
		enabled = true;

		if (domBuilder.config.isEnabledVoidTags()) {
			htmlVoidRules = new HtmlVoidRules();
		}
	}

	/**
	 * Finishes the tree building. Closes unclosed tags.
	 */
	@Override
	public void end() {
		if (parentNode != rootNode) {

			jodd.lagarto.dom.Node thisNode = parentNode;

			while (thisNode != rootNode) {
				if (domBuilder.config.isImpliedEndTags()) {
					if (implRules.implicitlyCloseTagOnEOF(thisNode.getNodeName())) {
						thisNode = thisNode.getParentNode();
						continue;
					}
				}

				error("Unclosed tag closed: <" + thisNode.getNodeName() + ">");

				thisNode = thisNode.getParentNode();
			}
		}

		// remove whitespaces
		if (domBuilder.config.isIgnoreWhitespacesBetweenTags()) {
			removeLastChildNodeIfEmptyText(parentNode, true);
		}

		// foster
		if (domBuilder.config.isUseFosterRules()) {
			jodd.lagarto.dom.HtmlFosterRules fosterRules = new HtmlFosterRules();
			fosterRules.fixFosterElements(rootNode);
		}

		// elapsed
		rootNode.end();

		if (log.isDebugEnabled()) {
			log.debug("LagartoDom tree created in " + rootNode.getElapsedTime() + " ms");
		}
	}

	// ---------------------------------------------------------------- tag

	/**
	 * Creates new element with correct configuration.
	 */
	protected jodd.lagarto.dom.Element createElementNode(final Tag tag) {
		boolean hasVoidTags = htmlVoidRules != null;

		boolean isVoid = false;
		boolean selfClosed = false;

		if (hasVoidTags) {
			isVoid = htmlVoidRules.isVoidTag(tag.getName());

			// HTML and XHTML
			if (isVoid) {
				// it's void tag, lookup the flag
				selfClosed = domBuilder.config.isSelfCloseVoidTags();
			}
		} else {
			// XML, no voids, lookup the flag
			selfClosed = domBuilder.config.isSelfCloseVoidTags();
		}

		return new jodd.lagarto.dom.Element(rootNode, tag, isVoid, selfClosed);
	}

	/**
	 * Visits tags.
	 */
	@Override
	public void tag(final Tag tag) {
		if (!enabled) {
			return;
		}

		TagType tagType = tag.getType();
		jodd.lagarto.dom.Element node;

		switch (tagType) {
			case START:
				if (domBuilder.config.isIgnoreWhitespacesBetweenTags()) {
					removeLastChildNodeIfEmptyText(parentNode, false);
				}

				node = createElementNode(tag);

				if (domBuilder.config.isImpliedEndTags()) {
					while (true) {
						String parentNodeName = parentNode.getNodeName();
						if (!implRules.implicitlyCloseParentTagOnNewTag(parentNodeName, node.getNodeName())) {
							break;
						}
						parentNode = parentNode.getParentNode();

						if (log.isDebugEnabled()) {
							log.debug("Implicitly closed tag <" + node.getNodeName() + "> ");
						}
					}
				}

				parentNode.addChild(node);

				if (!node.isVoidElement()) {
					parentNode = node;
				}
				break;

			case END:
				if (domBuilder.config.isIgnoreWhitespacesBetweenTags()) {
					removeLastChildNodeIfEmptyText(parentNode, true);
				}

				String tagName = tag.getName().toString();

				jodd.lagarto.dom.Node matchingParent = findMatchingParentOpenTag(tagName);

				if (matchingParent == parentNode) {		// regular situation
					parentNode = parentNode.getParentNode();
					break;
				}

				if (matchingParent == null) {			// matching open tag not found, remove it
					error("Orphan closed tag ignored: </" + tagName + "> " + tag.getTagPosition());
					break;
				}

				// try to close it implicitly
				if (domBuilder.config.isImpliedEndTags()) {
					boolean fixed = false;

					while (implRules.implicitlyCloseParentTagOnTagEnd(parentNode.getNodeName(), tagName)) {
						parentNode = parentNode.getParentNode();

						if (log.isDebugEnabled()) {
							log.debug("Implicitly closed tag <" + tagName + ">");
						}

						if (parentNode == matchingParent) {
							parentNode = matchingParent.parentNode;
							fixed = true;
							break;
						}
					}
					if (fixed) {
						break;
					}
				}


				// matching tag found, but it is not a regular situation
				// therefore close all unclosed tags in between
				fixUnclosedTagsUpToMatchingParent(tag, matchingParent);

				break;

			case SELF_CLOSING:
				if (domBuilder.config.isIgnoreWhitespacesBetweenTags()) {
					removeLastChildNodeIfEmptyText(parentNode, false);
				}

				node = createElementNode(tag);
				parentNode.addChild(node);
				break;
		}
	}

	// ---------------------------------------------------------------- util

	/**
	 * Removes last child node if contains just empty text.
	 */
	protected void removeLastChildNodeIfEmptyText(final jodd.lagarto.dom.Node parentNode, final boolean closedTag) {
		if (parentNode == null) {
			return;
		}

		jodd.lagarto.dom.Node lastChild = parentNode.getLastChild();
		if (lastChild == null) {
			return;
		}
		
		if (lastChild.getNodeType() != jodd.lagarto.dom.Node.NodeType.TEXT) {
			return;
		}

		if (closedTag) {
			if (parentNode.getChildNodesCount() == 1) {
				return;
			}
		}

		jodd.lagarto.dom.Text text = (jodd.lagarto.dom.Text) lastChild;

		if (text.isBlank()) {
			lastChild.detachFromParent();
		}
	}

	/**
	 * Finds matching parent open tag or <code>null</code> if not found.
	 */
	protected jodd.lagarto.dom.Node findMatchingParentOpenTag(String tagName) {
		jodd.lagarto.dom.Node parent = parentNode;
		
		if (!rootNode.config.isCaseSensitive()) {
			tagName = tagName.toLowerCase();
		}
		
		while (parent != null) {
			String parentNodeName = parent.getNodeName();

			if (parentNodeName != null) {
				if (!rootNode.config.isCaseSensitive()) {
					parentNodeName = parentNodeName.toLowerCase();
				}
			}
			
			if (tagName.equals(parentNodeName)) {
				return parent;
			}
			parent = parent.getParentNode();
		}
		return null;
	}

	/**
	 * Fixes all unclosed tags up to matching parent. Missing end tags will be added
	 * just before parent tag is closed, making the whole inner content as its tag body.
	 * <p>
	 * Tags that can be closed implicitly are checked and closed.
	 * <p>
	 * There is optional check for detecting orphan tags inside the
	 * table or lists. If set, tags can be closed beyond the border of the
	 * table and the list and it is reported as orphan tag.
	 * <p>
	 * This is just a generic solutions, closest to the rules.
	 */
	protected void fixUnclosedTagsUpToMatchingParent(final Tag tag, final jodd.lagarto.dom.Node matchingParent) {
		if (domBuilder.config.isUnclosedTagAsOrphanCheck()) {
			jodd.lagarto.dom.Node thisNode = parentNode;

			if (!CharSequenceUtil.equalsIgnoreCase(tag.getName(), "table")) {

				// check if there is table or list between this node
				// and matching parent
				while (thisNode != matchingParent) {
					String thisNodeName = thisNode.getNodeName().toLowerCase();

					if (thisNodeName.equals("table") || thisNodeName.equals("ul") || thisNodeName.equals("ol")) {

						String positionString = tag.getPosition();
						if (positionString == null) {
							positionString = StringPool.EMPTY;
						}

						error("Orphan closed tag ignored: </" + tag.getName() + "> " + positionString);
						return;
					}
					thisNode = thisNode.getParentNode();
				}
			}
		}

		while (true) {
			if (parentNode == matchingParent) {
				parentNode = parentNode.getParentNode();
				break;
			}

			jodd.lagarto.dom.Node parentParentNode = parentNode.getParentNode();

			if (domBuilder.config.isImpliedEndTags()) {
				if (implRules.implicitlyCloseParentTagOnNewTag(
						parentParentNode.getNodeName(), parentNode.getNodeName())) {
					// break the tree: detach this node and append it after parent

					parentNode.detachFromParent();

					parentParentNode.getParentNode().addChild(parentNode);
				}
			}

			// debug message

			error("Unclosed tag closed: <" + parentNode.getNodeName() + ">");

			// continue looping
			parentNode = parentParentNode;
		}
	}

	// ---------------------------------------------------------------- tree

	@Override
	public void script(final Tag tag, final CharSequence body) {
		if (!enabled) {
			return;
		}

		Element node = createElementNode(tag);

		parentNode.addChild(node);

		if (body.length() != 0) {
			jodd.lagarto.dom.Node text = new jodd.lagarto.dom.Text(rootNode, body.toString());
			node.addChild(text);
		}
	}

	@Override
	public void comment(final CharSequence comment) {
		if (!enabled) {
			return;
		}

		if (domBuilder.config.isIgnoreWhitespacesBetweenTags()) {
			removeLastChildNodeIfEmptyText(parentNode, false);
		}

		if (domBuilder.config.isIgnoreComments()) {
			return;
		}

		jodd.lagarto.dom.Node node = new Comment(rootNode, comment.toString());

		parentNode.addChild(node);
	}

	@Override
	public void text(final CharSequence text) {
		if (!enabled) {
			return;
		}

		String textValue = text.toString();

		Node node = new Text(rootNode, textValue);

		parentNode.addChild(node);
	}

	@Override
	public void cdata(final CharSequence cdata) {
		if (!enabled) {
			return;
		}

		jodd.lagarto.dom.CData cdataNode = new CData(rootNode, cdata.toString());

		parentNode.addChild(cdataNode);
	}

	@Override
	public void xml(final CharSequence version, final CharSequence encoding, final CharSequence standalone) {
		if (!enabled) {
			return;
		}

		jodd.lagarto.dom.XmlDeclaration xmlDeclaration = new XmlDeclaration(rootNode, version, encoding, standalone);

		parentNode.addChild(xmlDeclaration);
	}

	@Override
	public void doctype(final Doctype doctype) {
		if (!enabled) {
			return;
		}

		jodd.lagarto.dom.DocumentType documentType = new DocumentType(rootNode,
				Util.toString(doctype.getName()),
				Util.toString(doctype.getPublicIdentifier()),
				Util.toString(doctype.getSystemIdentifier())
		);

		parentNode.addChild(documentType);
	}

	@Override
	public void condComment(final CharSequence expression, final boolean isStartingTag, final boolean isHidden, final boolean isHiddenEndTag) {
		String expressionString = expression.toString().trim();

		if (expressionString.equals("endif")) {
			enabled = true;
			return;
		}

		if (expressionString.equals("if !IE")) {
			enabled = false;
			return;
		}

		float ieVersion = domBuilder.config.getCondCommentIEVersion();

		if (htmlCCommentExpressionMatcher == null) {
			htmlCCommentExpressionMatcher = new HtmlCCommentExpressionMatcher();
		}

		enabled = htmlCCommentExpressionMatcher.match(ieVersion, expressionString);
	}

	protected HtmlCCommentExpressionMatcher htmlCCommentExpressionMatcher;


	// ---------------------------------------------------------------- error

	@Override
	public void error(final String message) {
		rootNode.addError(message);
		log.log(domBuilder.config.getParsingErrorLogLevel(), message);
	}

}