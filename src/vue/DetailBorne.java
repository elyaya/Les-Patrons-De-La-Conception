package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import modele.Borne.TypeBorne;
import modele.RapportEvent;


public class DetailBorne extends JFrame implements ListSelectionListener {

	private TypeBorne type;
	private int numero;
	private PanelBorne panel;
	private JList L_liste;
	private int compteur;
	private ArrayList<RapportEvent> rapports;
	private String vehicules[];
	
	
	public DetailBorne(TypeBorne t, int i, ArrayList<RapportEvent> r) {
		super("Détail de la borne " + (i + 1));
		
		rapports = new ArrayList<RapportEvent>();
		type = t;
		numero = i;
		
		panel = new PanelBorne();
		
		// On remplir l'arraylist avec uniquement les rapports de cette voie
		Iterator<RapportEvent> it = r.iterator();
		while (it.hasNext()) {
		       RapportEvent re = it.next();
		       if(re.get_numeroVoie() == i) {
			       rapports.add(re);
		       }
		}
		majInterface();
	}
	
	public void ajouterRapport(RapportEvent r) {
		rapports.add(r);
	}
	
	public int getNumeroVoie() {
		return numero;
	}
	
	public void majInterface() {
		vehicules = new String[rapports.size()];
		
		Iterator<RapportEvent> it = rapports.iterator();
		int compteur = 0;
		while(it.hasNext()) {
		       RapportEvent re = it.next();
		       vehicules[compteur] = "" + compteur + ": " + re.get_typeVehicule();
		       compteur++;
		}
		L_liste = new JList<String>(vehicules);
		L_liste.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		L_liste.setLayoutOrientation(JList.VERTICAL);
		L_liste.setVisibleRowCount(-1);
		L_liste.addListSelectionListener(this);
		JScrollPane listScroller = new JScrollPane(L_liste);
		listScroller.setPreferredSize(new Dimension(150, 250));
		
		//this.getContentPane().setLayout(new BorderLayout(10, 10));
		this.getContentPane().removeAll();
		this.getContentPane().add(listScroller, BorderLayout.WEST);
		this.getContentPane().add(panel, BorderLayout.CENTER);
		this.pack();
		this.setVisible(true);
		this.setSize(450, 250);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList liste = (JList) e.getSource();
		ListSelectionModel lsm = liste.getSelectionModel();
		int minIndex = lsm.getMinSelectionIndex();
        int maxIndex = lsm.getMaxSelectionIndex();
        for(int i = minIndex; i <= maxIndex; i++) {
            if(lsm.isSelectedIndex(i)) {
                int n = Integer.parseInt(((String) L_liste.getModel().getElementAt(i)).split(":")[0]);
                panel.mettreAJour(rapports.get(n));
            }
        }
	}
}
