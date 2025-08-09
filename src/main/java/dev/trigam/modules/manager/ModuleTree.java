package dev.trigam.modules.manager;

import dev.trigam.modules.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ModuleTree {
	
	private final List<Node> roots = new ArrayList<>();
	
	public void addRoot ( Node root ) {
		this.roots.add( root );
	}
	public void addRoots ( List<Node> roots ) {
		this.roots.addAll( roots );
	}
	
	public List<Node> getRoots () {
		return this.roots;
	}
	public Optional<Node> getRootByName ( String name ) {
		for ( Node root : this.roots ) {
			if ( root.getName().equals( name ) ) return Optional.of( root );
		}
		return Optional.empty();
	}
	
	public void clear () {
		this.roots.clear();
	}
	
	public boolean isEmpty () {
		return this.roots.isEmpty();
	}
	
	
	
	public static class Node {
		private String name;
		private Optional<Module> value;
		private final List<Node> children = new ArrayList<>();
		private Optional<Node> parent = Optional.empty();
		
		public Node ( String name, Optional<Module> value ) {
			this.name = name;
			this.value = value;
		}
		
		public String getName () {
			return this.name;
		}
		public void setName ( String name ) {
			this.name = name;
		}
		
		public Optional<Module> getValue () {
			return this.value;
		}
		public void setValue ( Optional<Module> value ) {
			this.value = value;
		}
		
		public List<Node> getChildren () {
			return this.children;
		}
		public Optional<Node> getChildByName ( String nodeName ) {
			for ( Node child : this.children ) {
				if ( child.getName().equals( nodeName ) ) return Optional.of( child );
			}
			return Optional.empty();
		}
		public void addChild ( Node child ) {
			child.setParent( this );
			this.children.add( child );
		}
		public void addChildren ( List<Node> children ) {
			for ( Node child : children ) this.addChild( child );
		}
		
		public Optional<Node> getParent () {
			return this.parent;
		}
		private void setParent ( Node parent ) {
			this.parent = Optional.of( parent );
		}
		
		private boolean isRoot () {
			return this.parent.isEmpty();
		}
		private boolean isLastChild () {
			return this.parent.isPresent()
				&& this.equals( this.parent.get().children.getLast() );
		}
	}
	
	
	
	private static final String CHILD = "├── ";
	private static final String LAST_LEAF = "└── ";
	private static final String DIRECTORY = "│   ";
	private static final String EMPTY = "    ";
	
	// Based on https://gitlab.com/nfriend/tree-online/-/blob/master/src/lib/generate-tree.ts
	public String toString () {
		if ( this.roots.isEmpty() ) return "";
		
		List<String> tree = new ArrayList<>();
		for ( Node root : this.roots ) {
			generateTreeBranch( root, tree );
		}
		
		return String.join( "\n", tree );
	}
	private void generateTreeBranch ( Node node, List<String> tree ) {
		StringBuilder currentLine = new StringBuilder();
		
		// Add connector
		String connector = node.isLastChild() ? LAST_LEAF : CHILD;
		if ( node.getParent().isPresent() ) currentLine.append( connector );
		
		// Add node name
		String nodeName = node.getName();
		if ( node.isRoot() ) nodeName = String.format( "\u001b[0;34m%s\u001b[0m", nodeName );
		else if ( node.getValue().isPresent() ) nodeName = String.format( "\u001b[0;36m%s\u001b[0m", nodeName );
		
		currentLine.append( nodeName );
		
		// Add nesting
		Optional<Node> current = node.parent;
		while ( current.isPresent() && current.get().parent.isPresent() ) {
			String prefix = current.get().isLastChild() ? EMPTY : DIRECTORY;
			currentLine.insert( 0, prefix );
			
			current = current.get().parent;
		}
		
		// Add current line
		tree.add( currentLine.toString() );
		
		// Process children
		for ( Node child : node.getChildren() ) {
			generateTreeBranch( child, tree );
		}
	}
	
}
