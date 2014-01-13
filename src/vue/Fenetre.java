package vue;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import modele.Borne;
import modele.Borne.TypeBorne;
import modele.RapportEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 * 
 * @author Christophe Bougère
 *
 */
public class Fenetre extends JFrame implements ActionListener {
	private ArrayList<Borne> liste_bornes;
	private ArrayList<JButton> B_bornes;
	private ArrayList<JLabel> L_vehicules;
	private ArrayList<RapportEvent> R_rapports;
	private JButton B_parametres;
	private GridBagLayout GBL_layout;
	private BorderLayout BL_layout;
	private JPanel P_bornes;
	private DetailBorne db = null;
	
	public Fenetre() {
		super("Simulateur de barrière de péage");
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		B_parametres = new JButton("Modifier les paramètres");
		
		BL_layout = new BorderLayout(20, 20);
		this.setLayout(BL_layout);
		this.getContentPane().add(B_parametres, BorderLayout.SOUTH);
		razBornes();
		this.pack();
	}
	
	public String texteBouton(TypeBorne type, int numero) {
		String texte = "B";
		switch(type){
		case MANUELLE:
			texte += "M";
			break;
		case AUTOMATIQUE:
			texte += "A";
			break;
		case TELEPEAGE:
			texte += "T";
			break;
		}
		texte += "-" + numero;
		return texte;
	}
	
	public void ajouterBorne(TypeBorne type, Borne b) {
		liste_bornes.add(b);
		int numero = B_bornes.size() + 1;
		String texte = texteBouton(type, numero);
		
		GridBagConstraints GBC = new GridBagConstraints();
		GBC.gridx = 2 * numero - 2;
		GBC.insets= new Insets(20, 20, 20, 20);
		GBC.gridy = 0;
	    
		JButton bouton = new JButton(texte);
		bouton.addActionListener(this);
		bouton.setPreferredSize(new Dimension(150, 150));
		B_bornes.add(bouton);
		P_bornes.add(bouton, GBC);

		GBC.gridy = 1;
		JLabel label = new JLabel("Aucun véhicule", SwingConstants.CENTER);
		label.setPreferredSize(new Dimension(150, 150));
		L_vehicules.add(label);
		P_bornes.add(label, GBC);
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public void retirerBorne(int numero) {
		/*
		// On enlève les éléments du GridBagLayout
		P_bornes.remove(B_bornes.get(numero));
		P_bornes.remove(L_vehicules.get(numero));
		// On supprime le bouton
		B_bornes.remove(numero);
		// On décale les numéros
		for(int i = numero; i < B_bornes.size(); i++) {
			TypeBorne type = TypeBorne.MANUELLE;
			switch(B_bornes.get(i).getText().charAt(1)) {
			case 'M':
				type = TypeBorne.MANUELLE;
				break;
			case 'A':
				type = TypeBorne.AUTOMATIQUE;
				break;
			case 'T':
				type = TypeBorne.TELEPEAGE;
			}
			B_bornes.get(i).setText(texteBouton(type, i + 1));
		}
		// On supprime le label
		L_vehicules.remove(numero);
		// On repack la fenêtre
		this.pack();
		*/
	}
	
	public void razBornes() {
		liste_bornes = new ArrayList<Borne>();
		B_bornes = new ArrayList<JButton>();
		L_vehicules = new ArrayList<JLabel>();
		R_rapports = new ArrayList<RapportEvent>();
		
		GBL_layout = new GridBagLayout();
		P_bornes = new JPanel(GBL_layout);

		this.getContentPane().add(P_bornes, BorderLayout.CENTER);
		
		this.pack();
		this.setLocationRelativeTo(null);
	}
	
	public JLabel getLabel(int i) {
		return L_vehicules.get(i);
	}
	
	public JButton getButton(int i) {
		return B_bornes.get(i);
	}
	
	public JButton getB_parametres() {
		return B_parametres;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Quand on clique sur une borne, on veut les détails
		JButton b = (JButton) e.getSource();
		TypeBorne t = TypeBorne.MANUELLE;
		switch(b.getText().charAt(1)) {
		case 'A':
			t = TypeBorne.AUTOMATIQUE;
		case 'T':
			t = TypeBorne.TELEPEAGE;
		}
		int numero = Integer.parseInt(b.getText().substring(3));
		if(db != null) {
			db.dispose();
		}
		db = new DetailBorne(t, numero - 1, R_rapports);
		db.setLocationRelativeTo(null);
		db.setVisible(true);
	}
	
	public void envoiRapport(RapportEvent r) {
		R_rapports.add(r);
		if(db != null && r.get_numeroVoie() == db.getNumeroVoie()) {
			db.ajouterRapport(r);
			db.majInterface();
		}
		L_vehicules.get(r.get_numeroVoie()).setText(r.get_typeVehicule().toString().toLowerCase());
	}
}
