package com.app.model.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.graphstream.algorithm.generator.DorogovtsevMendesGenerator;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.MultiGraph;
import org.graphstream.graph.implementations.MultiNode;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.fx_viewer.*;
import org.graphstream.ui.javafx.FxGraphRenderer;

import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GraphStreamTest extends Application{
	
	protected String styleSheet = "graph {padding: 60px;}";


	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ignored) {

		MultiGraph graph = new MultiGraph("graph");
		System.setProperty("org.graphstream.ui", "javafx"); // set ui library
	
		
		Map<String, Double> variablesMap = new HashMap<String, Double>();
		variablesMap.put("x", 0d);
		variablesMap.put("y", 1d);
		variablesMap.put("reset", 0d);
		
		Node n = graph.addNode("A");
		n.setAttribute("varialbesMap", variablesMap);
		
		graph.nodes().forEach(node -> node.setAttribute("ui.label", node.getId()));
		graph.nodes().forEach(node -> node.setAttribute("ui.style", "text-alignment: at-right; text-size: 20; text-mode: normal;"));
		
		
		FxViewer v = new FxViewer(graph, FxViewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
		
		v.enableAutoLayout();
		FxViewPanel panel = (FxViewPanel)v.addDefaultView(false, new FxGraphRenderer());
		
		graph.setAttribute("ui.antialias");
		graph.setAttribute("ui.quality");
		graph.setAttribute("ui.stylesheet", styleSheet);
		
		
		Button nodes = new Button("Nodes");
		nodes.setOnAction((event)-> {

			List<Node> nodeList = new ArrayList<>();
			graph.nodes().forEach(nodeList::add);
			for(Node n1 : nodeList) {
				System.out.println(n1 + n.getAttribute("varialbesMap").toString());
			}
			;});
		
		Button incrementX = new Button("x=x+1");
		incrementX.setOnAction((event)-> {

			graph.nodes().forEach(x->{
				
				Map<String, Double> map = (Map<String, Double>) x.getAttribute("varialbesMap");
				map.put("x", map.get("x")+ 1);
				
				x.setAttribute("varialbesMap" ,map);});
			});
		
		FlowPane flowPane = new FlowPane();
		flowPane.setPadding(new Insets(10, 0, 0, 0));
		flowPane.getChildren().addAll(nodes, incrementX);
		flowPane.setHgap(2);

		Label title = new Label("Kripke structure:");
		title.setPadding(new Insets(10, 10, 10, 10));

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(10, 10, 10, 10));
		root.setTop(title);
		root.setCenter(panel);
		root.setBottom(flowPane);

		Scene scene = new Scene(root, 1024, 768);
		Stage stage = new Stage(StageStyle.DECORATED);
		stage.setTitle("JavaFXGraph Visualization");
		stage.setScene(scene);
		stage.show();
	}
}