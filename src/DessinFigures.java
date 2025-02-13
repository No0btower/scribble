import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * 
 * @author Corentin
 *
 */

public class DessinFigures extends JPanel {

	/**
	 * Param�tre priv�e permettant de sauvegarder la position X du curseur
	 */
	private int lastX;

	/**
	 * Param�tre priv�e permettant de sauvegarder la position y du curseur
	 */
	private int lastY;

	/**
	 * Cr�ation d'un ManipulateurFormes pour pouvoir d�sactiver son listener
	 * facilement
	 */
	private ManipulateurFormes manipul;

	/**
	 * Param�tre priv�e permettant de d�finir la couleur des dessins
	 */
	protected Color couleur;

	/**
	 * Tableau pouvant contenir jusqu'� 100 figures � visualiser et manipuler
	 */
	FigureColoree[] figures;

	/**
	 * Objet "listener" pour les manipulations et transformations de figures via
	 * la souris
	 */

	private ManipulateurFormes mf;

	/**
	 * Nombre effectif de figures apparaissant dans ce dessin
	 */
	private int nbf;

	/**
	 * Indice de la figure actuellement selectionn�e (-1 si aucune figure
	 * selectionnee)
	 */
	private int sel;

	/**
	 * Constructeur vide : dessin ne contenant aucune figure
	 */
	public DessinFigures() {
		super();
		this.figures = new FigureColoree[100];
		this.nbf = 0;
		this.sel = -1;
	}

	/**
	 * Methode activant les manipulations des formes geometriques � la souris
	 */
	public void activeManipulationsSouris() {
		manipul = new ManipulateurFormes();
		this.addMouseListener(manipul);
		this.addMouseMotionListener(manipul);
		repaint();
	}

	/**
	 * Cette methode permet d'ajouter une nouvelle figure au dessin
	 * 
	 * @param fc
	 *            - figure � ajouter au dessin
	 */
	public void ajoute(FigureColoree fc) {
		int i = 0;
		while (this.figures[i] != null) {
			i = i + 1;
		}
		this.figures[i] = fc;
		this.nbf = this.nbf + 1;
	}

	/**
	 * Cette methode permet d'initier le mecanisme evenementiel de fabrication
	 * des figures � la souris (ajout du listener)
	 * 
	 * @param fc
	 *            -forme � construire point par point � la souris
	 */
	public void construit(FigureColoree fc) {
		FabricantFigures fabricant = new FabricantFigures(fc);
		this.addMouseListener(fabricant);
	}

	/**
	 * Methode desactivant les manipulations des formes geometriques a la souris
	 */
	public void desactiveManipulationsSouris() {
		this.removeMouseListener(manipul);
		this.removeMouseMotionListener(manipul);
		repaint();
	}

	/**
	 * Cette methode retourne la figure actuellement selectionne
	 * 
	 * @return la figure selectionee
	 */
	public FigureColoree figureSelection() {
		if (sel == -1) {
			return null;
		}
		return this.figures[sel];
	}

	/**
	 * Cette methode retourne le nombre de figures apparaissant dans ce dessin
	 * 
	 * @return nombre de figures sur le dessin
	 */
	public int nbFigures() {
		return nbf;
	}

	/**
	 * Cette methode selectionne la prochaine figure dans le tableau des
	 * figures.
	 */
	public void selectionProchaineFigure() {
		this.sel = this.sel + 1;
	}

	/**
	 * Cette methode permet d'initier le mecanisme evenementiel de trace
	 * quelconque a la souris (definition de la couleur du trace et ajout des
	 * listeners)
	 * 
	 * @param couleur
	 *            - couleur du trait
	 */
	public void trace(Color couleur) {
		this.couleur = couleur;
		final MouseMotionListener mml = new MouseMotionListener() {

			public void mouseMoved(MouseEvent e) {
				lastX = e.getX();
				lastY = e.getY();
			}

			public void mouseDragged(MouseEvent e) {
				Graphics g = getGraphics();
				dessiner(g, e);
				lastX = e.getX();
				lastY = e.getY();
			}

		};
		this.addMouseMotionListener(mml);
		MouseListener ml = new MouseListener() {

			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					removeMouseMotionListener(mml);
					repaint();
				}
				if (SwingUtilities.isMiddleMouseButton(e)) {
					repaint();
				}
			}

			public void mouseEntered(MouseEvent e) {
			}

			public void mouseExited(MouseEvent e) {
			}

			public void mousePressed(MouseEvent e) {
			}

			public void mouseReleased(MouseEvent e) {
			}

		};
		this.addMouseListener(ml);
	}

	/**
	 * Methode qui va afficher les figures
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int i = 0; i < nbf; i++) {
			figures[i].affiche(g);
		}
	}

	/**
	 * Methode qui va afficher les traits lorsque la methode est appelee
	 * 
	 * @param g
	 *            Graphic du DessinFigures
	 * @param e
	 *            MouseEvente du trace
	 */
	public void dessiner(Graphics g, MouseEvent e) {
		g.setColor(getCouleur());
		g.drawLine(lastX, lastY, e.getX(), e.getY());
	}

	/**
	 * Methode qui applique � la couleur de DessinFigures une nouvelles couleur
	 * 
	 * @param a
	 *            nouvelle couleur
	 */
	public void setCouleur(Color a) {
		this.couleur = a;

	}

	/**
	 * Methode qui permet de connaitre la couleur du DessinFigures
	 * 
	 * @return couleur du DessinFigures
	 */
	public Color getCouleur() {
		return this.couleur;
	}

	public class ManipulateurFormes extends java.lang.Object implements
			java.awt.event.MouseListener, java.awt.event.MouseMotionListener {

		/**
		 * Attribut priv� qui permet de sauvegarder les positions en x du curseur
		 */
		private int last_x;

		/**
		 * Attribut priv� qui permet de sauvegarder les positions en y du curseur
		 */
		private int last_y;

		/**
		 * Methode qui permet de deplacer la figure avec le clic gauche, et deplacer les carr�s rouge avec le clic droit
		 */
		@Override
		public void mouseDragged(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				if (sel != -1
						&& figures[sel].carreDeSelection(e.getX(), e.getY()) != -1) {
					int carre = figures[sel].carreDeSelection(e.getX(),
							e.getY());
					figures[sel].translation(e.getX()
							- figures[sel].tab_mem[carre].rendreX(), e.getY()
							- figures[sel].tab_mem[carre].rendreY());
				}
			}
			if (SwingUtilities.isRightMouseButton(e)) {
				if (sel != -1
						&& figures[sel].carreDeSelection(e.getX(), e.getY()) != -1) {
					int carre = figures[sel].carreDeSelection(e.getX(),
							e.getY());
					figures[sel].transformation(e.getX(), e.getY(), carre);
				}
			}
			repaint();
		}

		@Override
		public void mouseMoved(MouseEvent e) {

		}

		@Override
		public void mouseClicked(MouseEvent e) {

		}

		/**
		 * Methode qui permet lorsqu'on fait un clic gauche de selectionner une figure
		 */
		@Override
		public void mousePressed(MouseEvent e) {
			if (SwingUtilities.isLeftMouseButton(e)) {
				int x = e.getX();
				int y = e.getY();
				for (int i = 0; i < nbf; i++) {
					if (figures[i].estDedans(x, y)) {
						if (sel != -1) {
							figures[i].deSelectionner();
						}
						sel = i;
						Graphics g = getGraphics();
						figures[sel].selectionne();
						figures[sel].affiche(g);
					} else {
						figures[i].deSelectionner();
					}
				}
				repaint();
			}

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		/**
		 * Methode qui permet d'avoir la position x du curseur
		 * @return
		 */
		public int getLastX() {
			return last_x;
		}

		/**
		 * Methode qui permet d'appliquer une nouvelle position x 
		 * @param last_x la nouvelle position 
		 */
		public void setLastX(int last_x) {
			this.last_x = last_x;
		}

		/**
		 * Methode qui permet d'avoir la position y du curseur
		 * @return
		 */
		public int getLastY() {
			return last_y;
		}

		/**
		 * Methode qui permet d'appliquer une nouvelle position
		 * @param last_y la nouvelle position
		 */
		public void setLastY(int last_y) {
			this.last_y = last_y;
		}
	}

}