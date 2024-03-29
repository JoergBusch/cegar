package com.app.model.graph;

import com.brunomnsilva.smartgraph.graph.Vertex;

public class OriginalStruct extends KStruct<Tuple> {

	@Override
	public Vertex<Tuple> getNode(Tuple tuple) {
		for(Vertex<Tuple> v :vertices()) {
			Tuple t = v.element();
			if(t.equals(tuple)) {
				return v;
			}
		}
		return null;
	}
}
