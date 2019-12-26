package GraphG;


import java.text.NumberFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;


import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.junit.Assert;
import org.junit.Test;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;


import java.awt.*;
import java.io.IOException;



public class SIS {
	Factory<Node> vertexFactory;
	Factory<Integer> edgeFactory;
	static Random rand = new Random();
	static NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
	static {format.setMaximumFractionDigits(10); format.setMinimumFractionDigits(10);	}



public static Graph<Node, Integer> loadGraphPajek(String file) {
	Factory<Node> vertexFactory = new Factory<Node>() {
			public Node create() {return new Node();}
	};
	Factory<Integer> edgeFactory = new Factory<Integer>() {
			int n = 0;
			public Integer create() {return new Integer(n++);}
	};
	Graph<Node, Integer> g = new SparseGraph<Node, Integer>();
	PajekNetReader<Graph<Node, Integer>, Node, Integer> pnr;
	try {
	pnr = new PajekNetReader<Graph<Node, Integer>, Node, Integer>(vertexFactory, edgeFactory);
	pnr.load(file, g);
	} catch (IOException e5) {}
	return g;}

public static double[] useVarLyambda(Graph<Node, Integer> graph, double from_lyambda, double to_lyambda, double step_lyambda, int totalStep, int missedStep) {
	  double[] infVertDensity = new double[1+(int) Math.ceil((to_lyambda-from_lyambda) /step_lyambda)];
	  int j = 0;
	  for (double lyambda = from_lyambda; lyambda <= to_lyambda; lyambda = lyambda + step_lyambda) {
				double[] d = modelingLyamda(graph, totalStep, missedStep, lyambda);
				double sum = 0;
				for (int i = 0; i < d.length; i++) {	sum = sum + d[i];}
				infVertDensity[j] = sum / ((double) (totalStep - missedStep));
				j++;
			}
	return infVertDensity;
}

public static void frame(Graph g) {

    Layout<Node, String> layout = new CircleLayout(g);
    layout.setSize(new Dimension(300,300));
    VisualizationViewer<Node,String> vv = new VisualizationViewer<Node,String>(layout);
    vv.setPreferredSize(new Dimension(350,350));        // Show vertex and edge labels
    vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());
    
    Transformer<Node, Paint> vertexPaint = new Transformer<Node, Paint>() {
        @Override
        public Paint transform(Node i) {
            return i.infected?Color.BLUE:Color.GREEN;
        }
    };

    
    
        // Create a graph mouse and add it to the visualization component
      DefaultModalGraphMouse gm = new DefaultModalGraphMouse();
      gm.setMode(ModalGraphMouse.Mode.TRANSFORMING);
      vv.setGraphMouse(gm);
      vv.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
      JFrame frame = new JFrame("Interactive Graph View 1");
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.getContentPane().add(vv);
      frame.pack();
      frame.setVisible(true);
      frame.getContentPane().repaint();
}



public static void main(String[] args) {
	Graph<Node, Integer> graph = loadGraphPajek("C:\\eclipse\\usr\\AnsCalT.net");
	frame(graph);
	double[] d = useVarLyambda(graph, 0.1, 1., 0.1, 5500, 5000);
	//вывод плотности инфицированных вершин
	for (int i = 0; i < d.length; i++) {System.out.println(format.format(d[i]));}
}


private static double[] modelingLyamda(Graph<Node, Integer> graph, int totalStep, int missedStep,
	     double lyambda) {
		double mass[] = new double[totalStep - missedStep];
		Collection<Node> list = graph.getVertices();
		for (Node n1 : graph.getVertices()) { // 1 - инициируем узлы
			if (rand.nextDouble() < 0.2) {
				n1.setInfected(true);
			}
			 else {n1.setInfected(false);
		}}
		for (int i = 0; i < totalStep; i++) { // 2  - процесс моделирования
			for (Node n1 : list) {	if (n1.isInfected()) {
				n1.new_infected = false;
				Iterator<Node> it = graph.getNeighbors(n1).iterator();
				while (it.hasNext()) {
				  Node n = it.next();
				  if (!n.isInfected()) if (Math.random() < lyambda) n.new_infected = true;}
			}
		}// 3  - изменением состояния вершин на новые и подсчитываем плотность инфицированных вершин 
			int inf = 0; int wel = 0;
				for (Node n1 : graph.getVertices()) {
					n1.setInfected(n1.new_infected);
					if (n1.isInfected()) inf++; else wel++;}
				if (i >= missedStep) mass[i - missedStep] =  (inf) / (1.0 * (inf + wel));
			}
	return mass;
	}



}