// Copyright (c) 2003-present, utils Team (http://utils.org)
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

import utils.csselly.CSSelly;
import utils.csselly.Combinator;
import utils.csselly.CssSelector;
import utils.lagarto.dom.Node;
import utils.lagarto.dom.NodeFilter;
import utils.util.collection.utilsArrayList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Node selector selects DOM nodes using {@link CSSelly CSS3 selectors}.
 * Group of queries are supported.
 */
public class NodeSelector {

	protected final utils.lagarto.dom.Node rootNode;

	public NodeSelector(final utils.lagarto.dom.Node rootNode) {
		this.rootNode = rootNode;
	}

	// ---------------------------------------------------------------- selector

	/**
	 * Selects nodes using CSS3 selector query.
	 */
	public List<utils.lagarto.dom.Node> select(final String query) {
		Collection<List<CssSelector>> selectorsCollection = CSSelly.parse(query);
		return select(selectorsCollection);
	}

	/**
	 * Selected nodes using pre-parsed CSS selectors. Take in consideration
	 * collection type for results grouping order.
	 */
	public List<utils.lagarto.dom.Node> select(final Collection<List<CssSelector>> selectorsCollection) {
		List<utils.lagarto.dom.Node> results = new ArrayList<>();
		for (List<CssSelector> selectors : selectorsCollection) {
			processSelectors(results, selectors);
		}
		return results;
	}

	/**
	 * Process selectors and keep adding results.
	 */
	protected void processSelectors(final List<utils.lagarto.dom.Node> results, final List<CssSelector> selectors) {
		List<utils.lagarto.dom.Node> selectedNodes = select(rootNode, selectors);

		for (utils.lagarto.dom.Node selectedNode : selectedNodes) {
			if (!results.contains(selectedNode)) {
				results.add(selectedNode);
			}
		}
	}

	/**
	 * Selects nodes using CSS3 selector query and returns the very first one.
	 */
	public utils.lagarto.dom.Node selectFirst(final String query) {
		List<utils.lagarto.dom.Node> selectedNodes = select(query);
		if (selectedNodes.isEmpty()) {
			return null;
		}
		return selectedNodes.get(0);
	}

	/**
	 * Selects nodes using {@link utils.lagarto.dom.NodeFilter node filter}.
	 */
	public List<utils.lagarto.dom.Node> select(final utils.lagarto.dom.NodeFilter nodeFilter) {
		List<utils.lagarto.dom.Node> nodes = new ArrayList<>();
		walk(rootNode, nodeFilter, nodes);
		return nodes;
	}

	/**
	 * Selects nodes using {@link utils.lagarto.dom.NodeFilter node filter} and return the very first one.
	 */
	public utils.lagarto.dom.Node selectFirst(final utils.lagarto.dom.NodeFilter nodeFilter) {
		List<utils.lagarto.dom.Node> selectedNodes = select(nodeFilter);
		if (selectedNodes.isEmpty()) {
			return null;
		}
		return selectedNodes.get(0);
	}

	// ---------------------------------------------------------------- internal

	protected void walk(final utils.lagarto.dom.Node rootNode, final NodeFilter nodeFilter, final List<utils.lagarto.dom.Node> result) {
		int childCount = rootNode.getChildNodesCount();
		for (int i = 0; i < childCount; i++) {
			utils.lagarto.dom.Node node = rootNode.getChild(i);
			if (nodeFilter.accept(node)) {
				result.add(node);
			}
			walk(node, nodeFilter, result);
		}
	}

	protected List<utils.lagarto.dom.Node> select(final utils.lagarto.dom.Node rootNode, final List<CssSelector> selectors) {

		// start with the root node
		List<utils.lagarto.dom.Node> nodes = new ArrayList<>();
		nodes.add(rootNode);

		// iterate all selectors
		for (CssSelector cssSelector : selectors) {

			// create new set of results for current css selector
			List<utils.lagarto.dom.Node> selectedNodes = new ArrayList<>();
			for (utils.lagarto.dom.Node node : nodes) {
				walk(node, cssSelector, selectedNodes);
			}

			// post-processing: filter out the results
			List<utils.lagarto.dom.Node> resultNodes = new ArrayList<>();
			int index = 0;
			for (utils.lagarto.dom.Node node : selectedNodes) {
				boolean match = filter(selectedNodes, node, cssSelector, index);
				if (match) {
					resultNodes.add(node);
				}
				index++;
			}

			// continue with results
			nodes = resultNodes;
		}

		return nodes;
	}

	/**
	 * Walks over the child notes, maintaining the tree order and not using recursion.
	 */
	protected void walkDescendantsIteratively(final utilsArrayList<utils.lagarto.dom.Node> nodes, final CssSelector cssSelector, final List<utils.lagarto.dom.Node> result) {
		while (!nodes.isEmpty()) {
			utils.lagarto.dom.Node node = nodes.removeFirst();
			selectAndAdd(node, cssSelector, result);

			// append children in walking order to be processed right after this node
			int childCount = node.getChildNodesCount();
			for (int i = childCount - 1; i >= 0; i--) {
				nodes.addFirst(node.getChild(i));
			}
		}
	}

	/**
	 * Finds nodes in the tree that matches single selector.
	 */
	protected void walk(final utils.lagarto.dom.Node rootNode, final CssSelector cssSelector, final List<utils.lagarto.dom.Node> result) {

		// previous combinator determines the behavior
		CssSelector previousCssSelector = cssSelector.getPrevCssSelector();

		Combinator combinator = previousCssSelector != null ?
				previousCssSelector.getCombinator() :
				Combinator.DESCENDANT;

		switch (combinator) {
			case DESCENDANT:
				utilsArrayList<utils.lagarto.dom.Node> nodes = new utilsArrayList<>();
				int childCount = rootNode.getChildNodesCount();
				for (int i = 0; i < childCount; i++) {
					nodes.add(rootNode.getChild(i));
					// recursive
//					selectAndAdd(node, cssSelector, result);
//					walk(node, cssSelector, result);
				}
				walkDescendantsIteratively(nodes, cssSelector, result);
				break;
			case CHILD:
				childCount = rootNode.getChildNodesCount();
				for (int i = 0; i < childCount; i++) {
					utils.lagarto.dom.Node node = rootNode.getChild(i);
					selectAndAdd(node, cssSelector, result);
				}
				break;
			case ADJACENT_SIBLING:
				utils.lagarto.dom.Node node = rootNode.getNextSiblingElement();
				if (node != null) {
					selectAndAdd(node, cssSelector, result);
				}
				break;
			case GENERAL_SIBLING:
				node = rootNode;
				while (true) {
					node = node.getNextSiblingElement();
					if (node == null) {
						break;
					}
					selectAndAdd(node, cssSelector, result);
				}
				break;
		}	}

	/**
	 * Selects single node for single selector and appends it to the results.
	 */
	protected void selectAndAdd(final utils.lagarto.dom.Node node, final CssSelector cssSelector, final List<utils.lagarto.dom.Node> result) {
		// ignore all nodes that are not elements
		if (node.getNodeType() != utils.lagarto.dom.Node.NodeType.ELEMENT) {
			return;
		}
		boolean matched = cssSelector.accept(node);
		if (matched) {
			// check for duplicates
			if (result.contains(node)) {
				return;
			}
			// no duplicate found, add it to the results
			result.add(node);
		}
	}

	/**
	 * Filter nodes.
	 */
	protected boolean filter(final List<utils.lagarto.dom.Node> currentResults, final Node node, final CssSelector cssSelector, final int index) {
		return cssSelector.accept(currentResults, node, index);
	}

}